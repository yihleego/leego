package io.leego.commons.seq.provider;

import io.leego.commons.seq.exception.SeqErrorException;
import io.leego.commons.seq.exception.SeqNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Leego Yih
 */
public class DataSourceSeqProvider implements SeqProvider {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceSeqProvider.class);
    private static final String TABLE_NAME = "seq";
    private static final String INIT_SQL = """
            create table if not exists %s (
            id        varchar(64) primary key not null,
            value     bigint default 0        not null,
            increment int    default 1        not null,
            version   int    default 1        not null)""";
    private static final String QUERY_SQL = "select id, value, increment, version from %s where id = ?";
    private static final String EXISTS_SQL = "select 1 from %s where id = ?";
    private static final String INSERT_SQL = "insert into %s (id, value, increment, version) values (?, ?, ?, ?)";
    private static final String UPDATE_SQL = "update %s set increment = ?, version = version + 1 where id = ?";
    private static final String CAS_SQL = "update %s set value = ? where id = ? and value = ? and version = ?";
    private final DataSource dataSource;
    private final int retries;
    private final String tableName;
    private final String initSql;
    private final String querySql;
    private final String existsSql;
    private final String insertSql;
    private final String updateSql;
    private final String casSql;

    public DataSourceSeqProvider(DataSource dataSource) {
        this(dataSource, TABLE_NAME, 10);
    }

    public DataSourceSeqProvider(DataSource dataSource, int retries) {
        this(dataSource, TABLE_NAME, retries);
    }

    public DataSourceSeqProvider(DataSource dataSource, String tableName, int retries) {
        if (retries < 1) {
            throw new IllegalArgumentException();
        }
        this.dataSource = dataSource;
        this.retries = retries;
        this.tableName = tableName;
        this.initSql = INIT_SQL.formatted(tableName);
        this.querySql = QUERY_SQL.formatted(tableName);
        this.existsSql = EXISTS_SQL.formatted(tableName);
        this.insertSql = INSERT_SQL.formatted(tableName);
        this.updateSql = UPDATE_SQL.formatted(tableName);
        this.casSql = CAS_SQL.formatted(tableName);
        this.init();
    }

    /**
     * Should not be used
     *
     * @deprecated Use {@link #next(String, int)} with {@link io.leego.commons.seq.client.CachedSeq}
     */
    @Override
    @Deprecated
    public long next(String key) {
        return this.next(key, 1).getValue();
    }

    @Override
    public Segment next(String key, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Invalid size");
        }
        int i = 0;
        long timestamp = System.currentTimeMillis();
        try (Connection connection = getConnection()) {
            while (i < retries) {
                PreparedStatement query = prepareStatement(connection, querySql, key);
                ResultSet rs = query.executeQuery();
                if (!rs.next()) {
                    throw new SeqNotFoundException("Missing '" + key + "'");
                }
                Seq seq = new Seq(rs.getString(1), rs.getLong(2), rs.getInt(3), rs.getInt(4));
                query.close();
                int increment = seq.increment;
                long expectedValue = seq.value;
                long newValue = expectedValue + (long) increment * size;
                int version = seq.version;
                PreparedStatement cas = prepareStatement(connection, casSql, newValue, key, expectedValue, version);
                int affectedRows = cas.executeUpdate();
                cas.close();
                if (affectedRows > 0) {
                    return new Segment(expectedValue + increment, increment, size);
                }
                i++;
                if (logger.isDebugEnabled()) {
                    logger.debug("Could not CAS seq '{}' {} {}, expectedValue: {}, newValue: {}, increment: {}, version: {}",
                            key, i, (i == 1 ? "time" : "times"), expectedValue, newValue, increment, version);
                }
            }
        } catch (SQLException e) {
            throw new SeqErrorException("Failed to modify seq '" + key + "'", e);
        }
        throw new SeqErrorException("Failed to modify seq '%s' after retrying %d %s in %d ms".formatted(key, i, (i == 1 ? "time" : "times"), System.currentTimeMillis() - timestamp));
    }

    @Override
    public boolean create(String key, long value, int increment) {
        try (Connection connection = getConnection()) {
            PreparedStatement exists = prepareStatement(connection, existsSql, key);
            if (exists.executeQuery().next()) {
                return true;
            }
            PreparedStatement insert = prepareStatement(connection, insertSql, key, value, increment, 1);
            return insert.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SeqErrorException("Failed to create seq '" + key + "'", e);
        }
    }

    @Override
    public boolean update(String key, int increment) {
        try (Connection connection = getConnection()) {
            PreparedStatement update = prepareStatement(connection, updateSql, increment, key);
            return update.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SeqErrorException("Failed to update seq '" + key + "'", e);
        }
    }

    @Override
    public boolean contains(String key) {
        try (Connection connection = getConnection()) {
            PreparedStatement exists = prepareStatement(connection, existsSql, key);
            return exists.executeQuery().next();
        } catch (SQLException e) {
            throw new SeqErrorException("Failed to query seq '" + key + "'", e);
        }
    }

    private Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(true);
        return connection;
    }

    private PreparedStatement prepareStatement(Connection connection, String sql, Object... params) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(sql);
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
        }
        return stmt;
    }

    private void init() {
        try (Connection connection = getConnection()) {
            PreparedStatement update = prepareStatement(connection, initSql);
            update.executeUpdate();
        } catch (SQLException e) {
            throw new SeqErrorException("Failed to init", e);
        }
    }

    private class Seq {
        String key;
        long value;
        int increment;
        int version;

        public Seq(String key, long value, int increment, int version) {
            this.key = key;
            this.value = value;
            this.increment = increment;
            this.version = version;
        }
    }
}
