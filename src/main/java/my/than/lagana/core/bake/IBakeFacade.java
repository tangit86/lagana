package my.than.lagana.core.bake;

import my.than.lagana.core.bake.fields.LaganaField;
import my.than.lagana.core.bake.filters.LaganaFilter;

import java.util.Arrays;
import java.util.List;

public interface IBakeFacade {

    List<LaganaRecord> get(List<Long> ids);

    Paginated<LaganaRecord> filter(List<String> select, List<LaganaFilter> filters, int page, int total) throws Exception;

    Paginated<LaganaRecord> filter(List<LaganaFilter> filters, int page, int total) throws Exception;

    <E> Arrays filter(List<E> es);

    void put(String source, String series, Long timestamp, List<RecordField> recordFields);

    List<Object> getSuggestedFieldValues(String fieldName);

    List<ILaganaFieldOut> getFields();
}
