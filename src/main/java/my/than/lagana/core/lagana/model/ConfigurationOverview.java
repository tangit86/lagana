package my.than.lagana.core.lagana.model;

import my.than.lagana.core.bake.filters.OperatorEnum;
import my.than.lagana.core.common.GenericFieldTypeEnum;
import my.than.lagana.core.reap.conf.ReapSchema;

import java.util.List;
import java.util.Map;

public class ConfigurationOverview {

    public ReapSchema reapSchema;

    public Map<GenericFieldTypeEnum, List<OperatorEnum>> allowedTypeOpMap;
}
