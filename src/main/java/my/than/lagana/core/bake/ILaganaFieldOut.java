package my.than.lagana.core.bake;

import my.than.lagana.core.bake.filters.OperatorEnum;
import my.than.lagana.core.common.GenericFieldTypeEnum;

import java.util.List;

public interface ILaganaFieldOut {
    String getName();
    GenericFieldTypeEnum getType();
    List<OperatorEnum> getSupportedOps();
    boolean isIndexed();
}
