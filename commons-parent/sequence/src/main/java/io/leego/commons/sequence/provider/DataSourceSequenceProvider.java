package io.leego.commons.sequence.provider;

import io.leego.commons.sequence.exception.SequenceObtainErrorException;

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
    protected final DataSource dataSource;

    public DataSourceSequenceProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSourceSequenceProvider(DataSource dataSource, int retries) {
        super(retries);
        this.dataSource = dataSource;
    }

    @Override
    public boolean create(String key, Long value, Integer increment) {
        PreparedStatement query = null;
        PreparedStatement insert = null;
        try (Connection connection = getConnection()) {
            query = prepareStatement(connection, "select 1 from seq where id = ? limit 1", key);
            ResultSet resultSet = query.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            insert = prepareStatement(connection, "insert into seq (id, value, increment, version) values (?, ?, ?, ?)", key, value, increment, 0);
            int affectedRows = insert.executeUpdate();
            connection.commit();
            return affectedRows > 0;
        } catch (Exception e) {
            throw new SequenceObtainErrorException("Failed to create sequence '" + key + "'", e);
        } finally {
            close(query, insert);
        }
    }

    @Override
    public boolean update(String key, Integer increment) {
        PreparedStatement update = null;
        try (Connection connection = getConnection()) {
            update = prepareStatement(connection, "update seq set increment = ?, version = version + 1 where id = ?", increment, key);
            int affectedRows = update.executeUpdate();
            connection.commit();
            return affectedRows > 0;
        } catch (Exception e) {
            throw new SequenceObtainErrorException("Failed to update sequence '" + key + "'", e);
        } finally {
            close(update);
        }
    }

    @Override
    protected Sequence find(String key) {
        PreparedStatement query = null;
        try (Connection connection = getConnection()) {
            query = prepareStatement(connection, "select id, value, increment, version from seq where id = ? limit 1", key);
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
            throw new SequenceObtainErrorException("Failed to obtain sequence '" + key + "'", e);
        } finally {
            close(query);
        }
    }

    @Override
    protected boolean compareAndSet(String key, long expectedValue, long newValue, int version) {
        PreparedStatement update = null;
        try (Connection connection = getConnection()) {
            update = prepareStatement(connection, "update seq set value = ? where id = ? and value = ? and version = ?", newValue, key, expectedValue, version);
            int affectedRows = update.executeUpdate();
            connection.commit();
            return affectedRows > 0;
        } catch (Exception e) {
            throw new SequenceObtainErrorException("Failed to modify sequence '" + key + "'", e);
        } finally {
            close(update);
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
            } catch (SQLException ignored) {
            }
        }
    }

    protected void close(Statement s1, Statement s2) {
        close(s1);
        close(s2);
    }
}
