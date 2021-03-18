package my.than.lagana.core.bake;

import my.than.lagana.core.common.GenericFieldTypeEnum;

import java.util.Objects;

public class RecordField {
    String name;
    GenericFieldTypeEnum type;

    public RecordField(String name, GenericFieldTypeEnum type, String value, int startPos, int endPos,boolean isIndexed, boolean isSorted) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.isIndexed = isIndexed;
        this.isSorted = isSorted;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    String value;
    boolean isIndexed;
    boolean isSorted;
    int startPos;
    int endPos;

    public String getName() {
        return name;
    }

    public GenericFieldTypeEnum getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getStartPos() {
        return startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public RecordField(String name, GenericFieldTypeEnum type, String value, int startPos, int endPos) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o.getClass().equals(String.class)){
            return name.equals(o.toString());
        }
        RecordField that = (RecordField) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
