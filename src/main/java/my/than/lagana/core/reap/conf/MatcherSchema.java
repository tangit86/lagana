package my.than.lagana.core.reap.conf;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import my.than.lagana.core.common.GenericFieldTypeEnum;

import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class MatcherSchema {
    public String name;
    @JsonIdentityReference(alwaysAsId = true)
    public FieldSchema field;
    public String regex;
    public String formatMatchValue;
    public String defaultMatchValue;
    public boolean multilineAndinvert;
    @JsonIdentityReference(alwaysAsId = true)
    public List<MatcherSchema> anyOf = new ArrayList<>();
    @JsonIdentityReference(alwaysAsId = true)
    public List<MatcherSchema> allOf = new ArrayList<>();
}
