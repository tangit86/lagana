package my.than.lagana.core.bake.fields;

import my.than.lagana.core.common.GenericFieldTypeEnum;

public class BooleanField extends LaganaField<Boolean> {

    public BooleanField(String name, String desc) {
        super(name, desc, GenericFieldTypeEnum.BOOLEAN, true, false);
    }

}
