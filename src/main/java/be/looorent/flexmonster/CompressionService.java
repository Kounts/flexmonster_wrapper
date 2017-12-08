package be.looorent.flexmonster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.flexmonster.compressor.Compressor.compressDb;
import static java.lang.System.exit;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.deleteIfExists;

public class CompressionService {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseConfiguration.class);

    private final InputRepository repository;

    public CompressionService(InputRepository repository) {
        this.repository = repository;
    }

    public Path compressResultsFrom(String query, Path outputFile) {
        try {
            return repository.findAndExecute(query, (resultSet) -> writeCompressedStream(resultSet, outputFile));
        } catch (IllegalAccessException | InstantiationException | SQLException | ClassNotFoundException e) {
            LOG.error("An error occurred when querying the database", e);
            throw new RuntimeException(e);
        }
    }

    private Path writeCompressedStream(ResultSet resultSet, Path outputFile) {
        try (InputStream compressedStream = compressDb(resultSet)) {
            copy(compressedStream, outputFile);
            return outputFile;
        }
        catch (IOException e) {
            LOG.error("An error occurred when writing or compressing the file to {}", outputFile, e);
            try {
                deleteIfExists(outputFile);
            } catch (IOException e1) {
                LOG.warn("The partially created file could not be deleted.", e1);
            }
            throw new RuntimeException(e);
        }
    }

    public static void main(String... args) {
        try {
            String query = readQuery(args);
            InputRepository repository = new InputRepository(DatabaseConfiguration.readFromSystemProperties());
            OutputConfiguration outputConfiguration = OutputConfiguration.readFromSystemProperties();
            Path resultFile = new CompressionService(repository).compressResultsFrom(query, outputConfiguration.getOutputFile());
            LOG.info("File successfully written at: {}", resultFile);
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