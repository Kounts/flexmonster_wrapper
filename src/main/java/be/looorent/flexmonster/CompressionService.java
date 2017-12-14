package be.looorent.flexmonster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.flexmonster.compressor.Compressor.compressDb;

public class CompressionService {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseConfiguration.class);

    private final InputRepository repository;
    private final OutputWriter writer;

    CompressionService(InputRepository repository, OutputWriter writer) {
        this.repository = repository;
        this.writer = writer;
    }

    public void compressResultsFrom(String query) {
        try {
            repository.findAndExecute(query, this::compressAndWrite);
        } catch (IllegalAccessException | InstantiationException | SQLException | ClassNotFoundException e) {
            LOG.error("An error occurred when querying the database", e);
            throw new RuntimeException(e);
        }
    }

    private void compressAndWrite(ResultSet resultSet) {
        try (InputStream compressedStream = compressDb(resultSet)) {
            this.writer.write(compressedStream);
        } catch (IOException e) {
            LOG.error("An error occurred when compressing or writing the result", e);
            throw new RuntimeException(e);
        }
    }
}