package be.looorent.flexmonster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import static java.lang.System.out;

class StdoutOutputWriter implements OutputWriter {

    private static final Logger LOG = LoggerFactory.getLogger(FileOutputWriter.class);

    @Override
    public void write(InputStream stream) throws IOException {
        int length;
        byte[] buffer = new byte[10240];
        while ((length = stream.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        out.flush();
        LOG.info("Compressed result written to STDOUT");
    }
}
