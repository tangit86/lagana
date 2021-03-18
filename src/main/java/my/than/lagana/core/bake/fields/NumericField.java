package my.than.lagana.core.bake.fields;

import my.than.lagana.core.common.GenericFieldTypeEnum;

public class NumericField extends LaganaField<Long> {
    public NumericField(String name, String desc, GenericFieldTypeEnum fieldType, boolean isIndexed,boolean isSorted) {
        super(name, desc, fieldType, isIndexed,isSorted);
    }

    @Override
    public boolean isLT(Long value, Long filter) {
        Long v = value != null ? value : 0L;
        return !value.equals(filter) && value < filter;
    }

    @Override
    public boolean isGT(Long value, Long filter) {
        return !isLT(value, filter);
    }


}
