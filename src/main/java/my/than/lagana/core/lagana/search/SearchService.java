package my.than.lagana.core.lagana.search;

import my.than.lagana.core.bake.IBakeFacade;
import my.than.lagana.core.bake.ILaganaFieldOut;
import my.than.lagana.core.bake.LaganaRecord;
import my.than.lagana.core.bake.Paginated;
import my.than.lagana.core.lagana.model.FetchRequest;
import my.than.lagana.core.lagana.model.FetchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchService {
    IBakeFacade bakeFacade;

    public SearchService(IBakeFacade bakeFacade) {
        this.bakeFacade = bakeFacade;
    }

    public FetchResponse fetch(FetchRequest request) {
        FetchResponse response = new FetchResponse();
        try {
            response.payload = mapResults(bakeFacade.filter(request.filters, request.page, request.pageSize), request.select);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private Paginated<LaganaSearchResult> mapResults(Paginated<LaganaRecord> laganaRecordPaginated, Map<String, String> select) {
        Paginated<LaganaSearchResult> payload = new Paginated<>();
        payload.page = laganaRecordPaginated.page;
        payload.pageSize = laganaRecordPaginated.pageSize;
        payload.totalPages = laganaRecordPaginated.totalPages;
        payload.totalResults = laganaRecordPaginated.totalResults;
        payload.results = laganaRecordPaginated.results.stream().map(r -> mapFieldAndValue(r, select)).collect(Collectors.toList());
        return payload;
    }

    private LaganaSearchResult mapFieldAndValue(LaganaRecord laganaRecord, Map<String, String> select) {
        Map<String, LaganaSearchResultField> fieldsMap = new HashMap<>();
        Map<Long, List<LaganaSearchResultValue>> values = new HashMap();
        laganaRecord.getFields().stream()
                .forEach(r ->
                        {
                            String fldkey = r.getName();
                            if (!fieldsMap.containsKey(fldkey)) {
                                fieldsMap.put(fldkey, new LaganaSearchResultField(fldkey, select.getOrDefault(fldkey, ""), r.getType()));
                            }
                            if (!values.containsKey(laganaRecord.getId())) {
                                values.put(laganaRecord.getId(), new ArrayList<>());
                            }
                            values.get(laganaRecord.getId()).add(new LaganaSearchResultValue(fldkey, r.getValue(), r.getStartPos(), r.getEndPos()));
                        }
                );
        return new LaganaSearchResult(new ArrayList<>(fieldsMap.values()), values);
    }


    public List<ILaganaFieldOut> getSearchSchema() {
        return bakeFacade.getFields().stream().collect(Collectors.toList());
    }

    public List<Object> getFieldSuggestions(String fieldId) {
        return bakeFacade.getSuggestedFieldValues(fieldId);
    }
}
