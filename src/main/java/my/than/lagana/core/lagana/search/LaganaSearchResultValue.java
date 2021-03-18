package my.than.lagana.core.lagana.search;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LaganaSearchResultValue {
    @JsonProperty("fn")
    public String fieldName;

    @JsonProperty("vl")
    public String value;

    @JsonProperty("sp")
    public int startPos;

    @JsonProperty("ep")
    public int endPos;

    public LaganaSearchResultValue(String fieldName, String value, int startPos, int endPos) {
        this.fieldName = fieldName;
        this.value = value;
        this.startPos = startPos;
        this.endPos = endPos;
    }
}
