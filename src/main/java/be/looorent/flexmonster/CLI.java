package be.looorent.flexmonster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.System.exit;

public class CLI {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseConfiguration.class);

    public static void main(String... args) {
        try {
            String query = readQuery(args);
            InputRepository repository = new InputRepository(DatabaseConfiguration.readFromSystemProperties());
            OutputConfiguration outputConfiguration = OutputConfiguration.readFromSystemProperties();
            CompressionService service = new CompressionService(repository, outputConfiguration.createWriter());
            service.compressResultsFrom(query);
            exit(0);
        }
        catch(Exception e) {
            LOG.error("An error occurred", e);
            exit(1);
        }
    }

    private static String readQuery(String... args) {
        if (args.length < 1 || args[0] == null || args[0].isEmpty()) {
            String error = "The first argument must be an SQL query";
            LOG.error(error);
            throw new IllegalArgumentException(error);
        }
        else {
            return args[0];
        }
    }

}
