package be.looorent.flexmonster;

import java.io.IOException;
import java.io.InputStream;

interface OutputWriter {

    void write(InputStream stream) throws IOException;

}
