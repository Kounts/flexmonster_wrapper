package be.looorent.flexmonster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static java.nio.file.Files.copy;
import static java.nio.file.Files.deleteIfExists;

class FileOutputWriter implements OutputWriter {

    private static final Logger LOG = LoggerFactory.getLogger(FileOutputWriter.class);

    private final Path outputFile;

    FileOutputWriter(Path outputFile) {
        this.outputFile = outputFile;
    }

    @Override
    public void write(InputStream stream) throws IOException {
        try {
            copy(stream, outputFile);
            LOG.info("File successfully written at: {}", outputFile.toAbsolutePath());
        }
        catch (IOException e) {
            LOG.error("An error occurred when writing or compressing the file to {}", outputFile, e);
            try {
                deleteIfExists(outputFile);
            } catch (IOException e1) {
                LOG.warn("The partially created file could not be deleted.", e1);
            }
            throw e;
        }
    }
}
