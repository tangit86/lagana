package my.than.lagana.core.lagana.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import my.than.lagana.core.common.GenericFieldTypeEnum;


public class LaganaSearchResultField {

    public String fieldName;
    public String alias;
    public GenericFieldTypeEnum typeEnum;

    public LaganaSearchResultField(String fieldName, String alias, GenericFieldTypeEnum typeEnum) {
        this.fieldName = fieldName;
        this.alias = alias;
        this.typeEnum = typeEnum;
    }


}
