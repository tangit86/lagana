package my.than.lagana.core.reap.feeder;

import my.than.lagana.core.common.GenericFieldTypeEnum;

public class MatchRecord {
    String name;
    GenericFieldTypeEnum fieldType;
    String value;
    int startPos;
    int endPos;

    public MatchRecord(String name, GenericFieldTypeEnum fieldType, String value, int startPos, int endPos) {
        this.name = name;
        this.fieldType = fieldType;
        this.value = value;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public String getName() {
        return name;
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

    public GenericFieldTypeEnum getFieldType(){return this.fieldType;}
}
