package be.looorent.flexmonster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

import static be.looorent.flexmonster.StringUtils.isEmpty;
import static java.nio.file.Files.exists;

class OutputConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseConfiguration.class);

    private final Path outputFile;

    public OutputConfiguration(Path outputFile) {
        if (outputFile == null) {
            throw new IllegalArgumentException("The output file should be defined.");
        }
        if (exists(outputFile)) {
            throw new IllegalArgumentException("The output file should not exist yet.");
        }
        this.outputFile = outputFile;
    }

    public static OutputConfiguration readFromSystemProperties() {
        String outputFile = System.getProperty("output.path");
        if (isEmpty(outputFile)) {
            LOG.info("System property 'output.path' is not defined, which is not ok!");
        }
        return new OutputConfiguration(Paths.get(outputFile));
    }

    public Path getOutputFile() {
        return outputFile;
    }
}