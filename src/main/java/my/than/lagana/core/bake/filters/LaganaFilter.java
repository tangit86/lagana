package my.than.lagana.core.bake.filters;

public class LaganaFilter {

    private String fieldName;
    private OperatorEnum op;
    private String value;

    public LaganaFilter(String fieldName, OperatorEnum op, String value) {
        this.fieldName = fieldName;
        this.op = op;
        this.value = value;
    }

    public OperatorEnum getOp() {
        return op;
    }

    public String getValue() {
        return value;
    }

    public String getFieldName() {
        return fieldName;
    }
}
