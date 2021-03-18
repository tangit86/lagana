package my.than.lagana.core.reap.conf;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import my.than.lagana.core.reap.reader.ReaderFormat;
import my.than.lagana.core.reap.reader.ReaderType;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class ReaderSchema {
    public String name;
    public String series;
    public ReaderFormat format;
    public ReaderType type;
}
