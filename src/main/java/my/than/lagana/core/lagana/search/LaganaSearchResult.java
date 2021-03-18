package my.than.lagana.core.lagana.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Map;

public class LaganaSearchResult {

//    @JsonProperty("fieldsMap")
//    public List<LaganaSearchResultField> fieldsMap;

    @JsonProperty("values")
    public Map<Long, List<LaganaSearchResultValue>> values;

    public LaganaSearchResult(List<LaganaSearchResultField> fieldsMap, Map<Long, List<LaganaSearchResultValue>> values) {
//        this.fieldsMap = fieldsMap;
        this.values = values;
    }


}
