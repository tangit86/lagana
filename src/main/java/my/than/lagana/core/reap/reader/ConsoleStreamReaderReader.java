package my.than.lagana.core.reap.reader;

import java.io.*;

public class ConsoleStreamReaderReader extends AbstractStreamReader implements IFeedStreamReader{

    Process p;

    public ConsoleStreamReaderReader(String name, String series, ReaderFormat format) {
        super(name, series, format, ReaderType.CONSOLE_STREAM);
    }

    @Override
    public boolean isAlive() {
        return p.isAlive();
    }

    @Override
    BufferedInputStream getStream() throws Exception {
        BufferedInputStream stream = null;
        p = Runtime.getRuntime().exec(series.split(","));
        stream = new BufferedInputStream(p.getInputStream());
        return stream;
    }
}
