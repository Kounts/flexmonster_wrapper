package be.looorent.flexmonster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.Boolean.parseBoolean;
import static java.lang.System.getProperty;
import static java.nio.file.Files.exists;
import static java.util.Optional.ofNullable;

class OutputConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(OutputConfiguration.class);

    private final Path outputFile;
    private final boolean writeToFile;

    public OutputConfiguration(boolean writeToFile, Path outputFile) {
        checkArguments(writeToFile, outputFile);
        this.writeToFile = writeToFile;
        this.outputFile = outputFile;
    }

    public static OutputConfiguration readFromSystemProperties() {
        Path outputFile = ofNullable(getProperty("output.path")).map(Paths::get).orElse(null);
        boolean writeToFile = parseBoolean(getProperty("output.file"));

        if (writeToFile && outputFile == null) {
            LOG.error("System property 'output.path' is not defined, which is not ok!");
        }

        if (!writeToFile && outputFile != null) {
            LOG.error("System property 'output.path' cannot be used beside 'output.file=true'");
        }

        return new OutputConfiguration(writeToFile, outputFile);
    }

    public OutputWriter createWriter() {
        if (writeToFile) {
            LOG.info("Result will be written to a file");
            return new FileOutputWriter(outputFile);
        }
        else {
            LOG.info("Result will be written to STDOUT");
            return new StdoutOutputWriter();
        }
    }

    private void checkArguments(boolean writeToFile, Path outputFile) {
        if (writeToFile) {
            if (outputFile == null) {
                throw new IllegalArgumentException("The output file should be defined.");
            }
            if (exists(outputFile)) {
                throw new IllegalArgumentException("The output file should not exist yet.");
            }
        }
        else if (outputFile != null) {
            throw new IllegalArgumentException("The output file should not be specified when you write to stdout.");
        }
    }
}