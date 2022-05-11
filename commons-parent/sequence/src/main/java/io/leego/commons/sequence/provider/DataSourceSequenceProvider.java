package io.leego.commons.sequence.provider;

import io.leego.commons.sequence.Segment;
import io.leego.commons.sequence.exception.SequenceErrorException;
import io.leego.commons.sequence.exception.SequenceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Leego Yih
 */
public class DataSourceSequenceProvider extends AbstractSequenceProvider {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceSequenceProvider.class);
    protected static final String QUERY_SQL = "select id, value, increment, version from seq where id = ? limit 1";
    protected static final String EXISTS_SQL = "select 1 from seq where id = ? limit 1";
    protected static final String INSERT_SQL = "insert into seq (id, value, increment, version) values (?, ?, ?, ?)";
    protected static final String UPDATE_SQL = "update seq set increment = ?, version = version + 1 where id = ?";
    protected static final String CAS_SQL = "update seq set value = ? where id = ? and value = ? and version = ?";
    protected final DataSource dataSource;
    protected final int retries;

    public DataSourceSequenceProvider(DataSource dataSource) {
        this.retries = 10;
        this.dataSource = dataSource;
    }

    public DataSourceSequenceProvider(DataSource dataSource, int retries) {
        this.retries = Math.max(retries, 1);
        this.dataSource = dataSource;
    }

    @Override
    public Segment next(String key, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("The size must be greater than zero");
        }
        long timestamp = System.currentTimeMillis();
        int i = 1;
        for (; i <= retries; i++) {
            Sequence sequence = this.find(key);
            if (sequence == null) {
                throw new SequenceNotFoundException("There is no sequence '" + key + "'");
            }
            int increment = sequence.getIncrement();
            long expectedValue = sequence.getValue();
            long newValue = expectedValue + (long) increment * size;
            int version = sequence.getVersion();
            if (this.compareAndSet(key, expectedValue, newValue, version)) {
                return new Segment(expectedValue + increment, newValue, increment);
            }
        }
        throw new SequenceErrorException("Failed to modify sequence '" + key + "', after retrying " + i + " time(s) in " + (System.currentTimeMillis() - timestamp) + " ms");
    }

    @Override
    public boolean create(String key, Long value, Integer increment) {
        PreparedStatement exists = null;
        PreparedStatement insert = null;
        try (Connection connection = getConnection()) {
            exists = prepareStatement(connection, EXISTS_SQL, key);
            ResultSet resultSet = exists.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            insert = prepareStatement(connection, INSERT_SQL, key, value, increment, 0);
            int affectedRows = insert.executeUpdate();
            connection.commit();
            return affectedRows > 0;
        } catch (Exception e) {
            throw new SequenceErrorException("Failed to create sequence '" + key + "'", e);
        } finally {
            close(exists, insert);
        }
    }

    @Override
    public boolean update(String key, Integer increment) {
        PreparedStatement update = null;
        try (Connection connection = getConnection()) {
            update = prepareStatement(connection, UPDATE_SQL, increment, key);
            int affectedRows = update.executeUpdate();
            connection.commit();
            return affectedRows > 0;
        } catch (Exception e) {
            throw new SequenceErrorException("Failed to update sequence '" + key + "'", e);
        } finally {
            close(update);
        }
    }

    protected Sequence find(String key) {
        PreparedStatement query = null;
        try (Connection connection = getConnection()) {
            query = prepareStatement(connection, QUERY_SQL, key);
            ResultSet resultSet = query.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return new Sequence(
                    resultSet.getString(1),
                    resultSet.getLong(2),
                    resultSet.getInt(3),
                    resultSet.getInt(4));
        } catch (Exception e) {
            throw new SequenceErrorException("Failed to query sequence '" + key + "'", e);
        } finally {
            close(query);
        }
    }

    protected boolean compareAndSet(String key, long expectedValue, long newValue, int version) {
        PreparedStatement cas = null;
        try (Connection connection = getConnection()) {
            cas = prepareStatement(connection, CAS_SQL, newValue, key, expectedValue, version);
            int affectedRows = cas.executeUpdate();
            connection.commit();
            return affectedRows > 0;
        } catch (Exception e) {
            throw new SequenceErrorException("Failed to modify sequence '" + key + "'", e);
        } finally {
            close(cas);
        }
    }

    protected Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        return connection;
    }

    protected PreparedStatement prepareStatement(Connection connection, String sql, Object... params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
        }
        return statement;
    }

    protected void close(Statement s) {
        if (s != null) {
            try {
                s.close();
            } catch (SQLException e) {
                logger.error("Failed to close statement", e);
            }
        }
    }

    protected void close(Statement s1, Statement s2) {
        close(s1);
        close(s2);
    }

    protected static class Sequence {
        private String key;
        private Long value;
        private Integer increment;
        private Integer version;

        public Sequence() {
        }

        public Sequence(String key, Long value, Integer increment, Integer version) {
            this.key = key;
            this.value = value;
            this.increment = increment;
            this.version = version;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Long getValue() {
            return value;
        }

        public void setValue(Long value) {
            this.value = value;
        }

        public Integer getIncrement() {
            return increment;
        }

        public void setIncrement(Integer increment) {
            this.increment = increment;
        }

        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }
    }
}
