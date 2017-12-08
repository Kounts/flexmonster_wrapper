package be.looorent.flexmonster;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Function;

import static java.sql.DriverManager.getConnection;

class InputRepository {

    private final DatabaseConfiguration configuration;

    public InputRepository(DatabaseConfiguration configuration) {
        this.configuration = configuration;
    }

    public <T> T findAndExecute(String query, Function<ResultSet, T> process) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        try (Connection connection = createConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            return process.apply(resultSet);
        }
    }

    private Connection createConnection() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        Class.forName(configuration.getDriver()).newInstance();
        String connectionString = new StringBuilder()
                .append("jdbc:")
                .append(configuration.getProvider())
                .append("://")
                .append(configuration.getHost())
                .append(":")
                .append(configuration.getPort())
                .append("/")
                .append(configuration.getDatabaseName())
                .toString();
        return getConnection(connectionString, configuration.getUser(), String.valueOf(configuration.getPassword()));
    }
}