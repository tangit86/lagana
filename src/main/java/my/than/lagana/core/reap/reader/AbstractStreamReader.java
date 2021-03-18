package my.than.lagana.core.reap.reader;

import my.than.lagana.core.common.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;

public abstract class AbstractStreamReader {

    final  Logger logger = LoggerFactory.getLogger(getClass());

    String name;
    String series;
    ReaderFormat format;
    ReaderType type;

    protected AbstractStreamReader(String name, String series, ReaderFormat format, ReaderType type) {
        this.name = name;
        this.series = series;
        this.format = format;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getSeries() {
        return series;
    }

    public ReaderFormat getFormat() {
        return format;
    }

    public ReaderType getType() {
        return type;
    }


    public InputStreamReader get() throws StreamReaderInitiationException {
        InputStreamReader inputStreamReader = null;
        try{
            inputStreamReader = new InputStreamReader(getStream());
        } catch (Exception e) {
            String msg = StringUtils.format("StreamReader {} closed, reason: {}",this.name,e);
            logger.error(msg);
            throw new StreamReaderInitiationException(msg,e);
        }
        return inputStreamReader;
    }

    abstract BufferedInputStream getStream() throws Exception;
}
