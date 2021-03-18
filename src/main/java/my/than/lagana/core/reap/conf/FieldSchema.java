package my.than.lagana.core.reap.conf;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import my.than.lagana.core.common.GenericFieldTypeEnum;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class FieldSchema {
    public String name;
    public GenericFieldTypeEnum type;
    public boolean isIndex;
    public boolean isSorted;
}
