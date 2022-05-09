package io.leego.commons.sequence.provider;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Leego Yih
 */
public class DataSourceSequenceProvider extends DatabaseSequenceProvider {
    protected final DataSource dataSource;

    public DataSourceSequenceProvider(DataSource dataSource, int retries) {
        super(retries);
        this.dataSource = dataSource;
    }

    @Override
    protected int save(Sequence sequence) throws Exception {
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("insert into seq (id, value, increment, version) values (?, ?, ?, ?)");
        statement.setString(1, sequence.getId());
        statement.setLong(2, sequence.getValue());
        statement.setInt(3, sequence.getIncrement());
        statement.setInt(4, sequence.getVersion());
        int affectedRows = statement.executeUpdate();
        statement.close();
        connection.close();
        return affectedRows;
    }

    @Override
    protected int update(Sequence sequence) throws Exception {
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("update seq set increment = ?, version = version + 1 where id = ? and version = ?");
        statement.setLong(1, sequence.getIncrement());
        statement.setString(2, sequence.getId());
        statement.setLong(3, sequence.getVersion());
        int affectedRows = statement.executeUpdate();
        statement.close();
        connection.close();
        return affectedRows;
    }

    @Override
    protected int compareAndUpdateValue(String id, long expectedValue, long newValue, int version) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("update seq set value = ? where id = ? and value = ? and version = ?");
        statement.setLong(1, newValue);
        statement.setString(2, id);
        statement.setLong(3, expectedValue);
        statement.setInt(4, version);
        int affectedRows = statement.executeUpdate();
        statement.close();
        connection.close();
        return affectedRows;
    }

    @Override
    protected Sequence findById(String id) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("select id, value, increment, version from seq where id = ? limit 1");
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();
        statement.close();
        connection.close();
        if (resultSet.first()) {
            return new Sequence(resultSet.getString(1), resultSet.getLong(2), resultSet.getInt(3), resultSet.getInt(4));
        } else {
            return null;
        }
    }
}
