package my.than.lagana.core.reap.conf;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class FeederSchema {
    public String name;
    public String description;
    public String targetSystem;
    @JsonIdentityReference(alwaysAsId = true)
    public ReaderSchema reader;
    @JsonIdentityReference(alwaysAsId = true)
    public MatcherSchema matcher;
    public boolean isRegistered;
}
