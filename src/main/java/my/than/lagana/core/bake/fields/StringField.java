package my.than.lagana.core.bake.fields;


import my.than.lagana.core.common.GenericFieldTypeEnum;

import java.util.regex.Pattern;

public class StringField extends LaganaField<String> {

    public StringField(String name, String desc, GenericFieldTypeEnum fieldType, boolean isIndexed,boolean isSorted) {
        super(name, desc, fieldType, isIndexed,isSorted);
    }

    @Override
    public boolean isCONTAINS(String value, String filter) {
        return value.toLowerCase().contains(filter.toLowerCase());
    }

    @Override
    public boolean isNOTCONTAINS(String value, String filter) {
        return !isCONTAINS(value,filter);
    }

    @Override
    public boolean isIN(String value, String filter) {
        String[] terms = filter.split(",");
        for (String t : terms) {
            if (t.equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isNOTIN(String value, String filter) {
        return !isIN(value,filter);
    }

    @Override
    protected boolean isREGEX(String currentValue, String filterValue) throws Exception {
        Pattern pattern = Pattern.compile(filterValue);
        return  pattern.matcher(currentValue).find();
    }
}
