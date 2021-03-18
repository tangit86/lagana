package my.than.lagana.core.reap.reader;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.InputStreamReader;

public interface IFeedStreamReader {

    @JsonIgnore
    boolean isAlive();
    String getName();
    String getSeries();
    ReaderFormat getFormat();
    ReaderType getType();
    @JsonIgnore
    InputStreamReader get() throws StreamReaderInitiationException;
}
