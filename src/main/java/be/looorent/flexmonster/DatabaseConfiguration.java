package be.looorent.flexmonster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static be.looorent.flexmonster.StringUtils.assertNotNull;
import static be.looorent.flexmonster.StringUtils.isEmpty;
import static java.util.Optional.ofNullable;

class DatabaseConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseConfiguration.class);
    private static final String DEFAULT_DRIVER = "org.postgresql.Driver";
    private static final String DEFAULT_PROVIDER = "postgresql";

    private final String user;
    private final char[] password;
    private final String host;
    private final int port;
    private final String driver;
    private final String provider;
    private final String databaseName;

    public DatabaseConfiguration(String user,
                                 String password,
                                 String host,
                                 Integer port,
                                 String databaseName,
                                 String driver,
                                 String provider) {
        assertNotNull(user, "user");
        assertNotNull(host, "host");
        assertNotNull(port, "port");
        assertNotNull(databaseName, "databaseName");

        this.user = user;
        this.password = ofNullable(password).orElse("").toCharArray();
        this.host = host;
        this.port = port;
        this.driver = ofNullable(driver).orElse(DEFAULT_DRIVER);
        this.provider = ofNullable(provider).orElse(DEFAULT_PROVIDER);
        this.databaseName = databaseName;
    }

    public static DatabaseConfiguration readFromSystemProperties() {
        String user = readSystemProperty("database.user", true);
        String password = readSystemProperty("database.password", false);
        String host = readSystemProperty("database.host", true);
        String port = readSystemProperty("database.port", true);
        String databaseName = readSystemProperty("database.databaseName", true);
        String driver = readSystemProperty("database.driver", false);
        String provider = readSystemProperty("database.provider", false);

        return new DatabaseConfiguration(user,
                password,
                host,
                ofNullable(port).map(Integer::parseInt).orElse(null),
                databaseName,
                driver,
                provider);
    }

    private static String readSystemProperty(String propertyName, boolean mandatory) {
        String value = System.getProperty(propertyName);
        if (isEmpty(value)) {
            if (mandatory) {
                LOG.info("System property '{}' is not defined, which is not ok!", propertyName);
            }
            else {
                LOG.info("System property '{}' is not defined, which is fine.", propertyName);
            }
        }
        return value;
    }

    public String getUser() {
        return user;
    }

    public char[] getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDriver() {
        return driver;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getProvider() {
        return provider;
    }
}