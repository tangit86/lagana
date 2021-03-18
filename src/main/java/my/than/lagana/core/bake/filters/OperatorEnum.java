package my.than.lagana.core.bake.filters;

public enum OperatorEnum {

    EQ("EQ"),
    NOTEQ("NOT EQ"),
    CONTAINS("CONTAINS"),
    NOTCONTAINS("NOT CONTAINS"),
    IN("IN"),
    NOTIN("NOT IN"),
    EMPTY("EMPTY"),
    NOTEMPTY("NOT EMPTY"),
    LT("LT"),
    GT("GT"),
    BETWEEN("BETWEEN"),
    REGEX("REGEX");

    OperatorEnum(String s) {
    }
}
