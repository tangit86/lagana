package my.than.lagana.core.reap.reader;

import java.io.BufferedInputStream;
import java.net.URL;

public class WebStreamReaderReader extends AbstractStreamReader implements IFeedStreamReader {

    public WebStreamReaderReader(String name, String source, ReaderFormat format) {
        super(name, source, format, ReaderType.HTTP);
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    BufferedInputStream getStream() throws Exception {
        return new BufferedInputStream(new URL(this.series).openStream());
    }
}
