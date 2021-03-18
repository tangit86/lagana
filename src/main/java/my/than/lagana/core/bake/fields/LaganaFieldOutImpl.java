package my.than.lagana.core.bake.fields;

import my.than.lagana.core.bake.ILaganaFieldOut;
import my.than.lagana.core.bake.filters.OperatorEnum;
import my.than.lagana.core.common.GenericFieldTypeEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaganaFieldOutImpl implements ILaganaFieldOut {

    private Map<GenericFieldTypeEnum, List<OperatorEnum>> getAllowedTypeOpMap(){
        HashMap<GenericFieldTypeEnum,List<OperatorEnum>> map = new HashMap<>();
        map.put(GenericFieldTypeEnum.BOOLEAN, Arrays.asList(OperatorEnum.EQ,OperatorEnum.EQ));
        map.put(GenericFieldTypeEnum.NUMERIC, Arrays.asList(OperatorEnum.EQ,OperatorEnum.EQ,OperatorEnum.GT,OperatorEnum.LT,OperatorEnum.IN,OperatorEnum.NOTIN));
        map.put(GenericFieldTypeEnum.TIMESTAMP,Arrays.asList(OperatorEnum.EQ,OperatorEnum.EQ,OperatorEnum.GT,OperatorEnum.LT));
        map.put(GenericFieldTypeEnum.STRING,Arrays.asList(OperatorEnum.CONTAINS,OperatorEnum.NOTCONTAINS,OperatorEnum.EMPTY,OperatorEnum.NOTEMPTY,OperatorEnum.IN,OperatorEnum.NOTIN,OperatorEnum.REGEX));
        map.put(GenericFieldTypeEnum.JSON,Arrays.asList(OperatorEnum.CONTAINS,OperatorEnum.NOTCONTAINS,OperatorEnum.EMPTY,OperatorEnum.NOTEMPTY,OperatorEnum.IN,OperatorEnum.NOTIN,OperatorEnum.REGEX));
        map.put(GenericFieldTypeEnum.XML,Arrays.asList(OperatorEnum.CONTAINS,OperatorEnum.NOTCONTAINS,OperatorEnum.EMPTY,OperatorEnum.NOTEMPTY,OperatorEnum.IN,OperatorEnum.NOTIN,OperatorEnum.REGEX));
        return map;
    }

    private final LaganaField laganaField;

    public LaganaFieldOutImpl(LaganaField field) {
        this.laganaField = field;
    }

    @Override
    public String getName() {
        return this.laganaField.getName();
    }

    @Override
    public GenericFieldTypeEnum getType() {
        return this.laganaField.getFieldType();
    }

    @Override
    public List<OperatorEnum> getSupportedOps() {
        return this.getAllowedTypeOpMap().get(this.laganaField.getFieldType());
    }

    @Override
    public boolean isIndexed() {
        return this.laganaField.isIndexed();
    }
}
