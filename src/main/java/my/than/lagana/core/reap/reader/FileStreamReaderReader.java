package my.than.lagana.core.reap.reader;

import java.io.*;

public class FileStreamReaderReader extends AbstractStreamReader implements IFeedStreamReader {

    public FileStreamReaderReader(String name, String source, ReaderFormat format) {
        super(name, source, format, ReaderType.FILE);
    }

    @Override
    public boolean isAlive() {
        try {
            return stream.available()>0;
        } catch (IOException e) {
            return false;
        }
    }

    BufferedInputStream stream;

    @Override
    BufferedInputStream getStream() throws Exception {
        stream = new BufferedInputStream(new FileInputStream(new File(this.series)));
        return stream;
    }

}
