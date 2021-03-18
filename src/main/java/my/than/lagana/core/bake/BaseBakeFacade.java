package my.than.lagana.core.bake;

import my.than.lagana.core.bake.conf.BakeConfiguration;
import my.than.lagana.core.bake.fields.*;
import my.than.lagana.core.bake.filters.LaganaFilter;
import my.than.lagana.core.common.GenericFieldTypeEnum;
import my.than.lagana.core.common.StringUtils;
import my.than.lagana.core.common.subpub.IPubSubProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static my.than.lagana.core.common.Constants.*;

public class BaseBakeFacade implements IBakeFacade {

    private final static Logger logger = LoggerFactory.getLogger(BaseBakeFacade.class);

    private BakeConfiguration bakeConfiguration;

    private Map<String, LaganaField> fields = new HashMap<>();

    private ConcurrentMap<Long, LaganaRecord> eventLog = new ConcurrentHashMap<>();

    public BaseBakeFacade(BakeConfiguration bakeConfiguration, IPubSubProvider<LaganaRecord> pubSubProvider) {
        this.bakeConfiguration = bakeConfiguration;
    }

    @Override
    public List<LaganaRecord> get(List<Long> ids) {
        return ids.stream().map(id -> this.eventLog.getOrDefault(id, null)).collect(Collectors.toList());
    }

    @Override
    public Paginated<LaganaRecord> filter(List<String> select, List<LaganaFilter> filters, int page, int total) throws Exception {
        return filter(filters, page, total);
    }

    @Override
    public Paginated<LaganaRecord> filter(List<LaganaFilter> filters, int page, int pageSize) throws Exception {

        Set<Long> resultIds = new HashSet<>();

        if (filters == null || filters.isEmpty()) {
            if (this.fields.containsKey(TIMESTAMP_FIELDNAME)) {
                resultIds.addAll(this.fields.get(TIMESTAMP_FIELDNAME).filter(Arrays.asList(), new HashSet<>()));
            } else {
                throw new Exception(StringUtils.format("no filters and no {} found", TIMESTAMP_FIELDNAME));
            }
        } else {
            Map<String, List<LaganaFilter>> filtersByFieldNames = filters.stream().collect(Collectors.groupingBy(LaganaFilter::getFieldName));
            Set<Long> prevMatches = new HashSet<>();
            for (String laganaFieldKey : this.fields.keySet()) {
                List<LaganaFilter> filtersOfField = filtersByFieldNames.getOrDefault(laganaFieldKey, null);
                if (filtersOfField == null) {
                    continue;
                }
                LaganaField field = this.fields.get(laganaFieldKey);

                Set<Long> curRes = field.filter(filtersOfField.stream().map(o -> new Pair(castValue(o.getValue(),field.getFieldType()), o.getOp())).collect(Collectors.toList()),prevMatches);
                //TODO: take away hard-coded AND logic
                if (curRes.isEmpty()) {
                    resultIds.clear();
                    break;
                }
                resultIds.addAll(curRes);
            }
        }
        return getPaginatedResponse(new ArrayList<>(resultIds), page, pageSize);
    }

    private Paginated<LaganaRecord> getPaginatedResponse(List<Long> resultIds, int page, int pageSize) {
        Paginated<LaganaRecord> pg = new Paginated<>();
        pg.totalResults = resultIds.size();
        pg.pageSize = Math.min(pageSize, pg.totalResults);
        pg.totalPages = pg.totalResults % pageSize > 0 ? pg.totalResults/pageSize + 1 : pg.totalResults/pageSize;
        pg.page = pg.totalPages > 1 ? (page > pg.totalPages ? pg.totalPages - 1 : page) : 1;
        // to fetch latest in time, since they are ordered in ascending
        int start = (pg.totalPages-1) * pg.pageSize;
        if (start < 0) {
            start = 0;
        }
        int end = pg.totalResults-pg.pageSize*(page-1);

        if (!resultIds.isEmpty()) {
            pg.results.addAll(resultIds.subList(start, end).stream().map(this.eventLog::get).sorted(new Comparator<LaganaRecord>() {
                @Override
                public int compare(LaganaRecord prev, LaganaRecord next) {
                    return next.getTimestamp()-prev.getTimestamp() > 0 ? -1 : 1;
                }
            }).collect(Collectors.toList()));
        }
        return pg;
    }

    @Override
    public <E> Arrays filter(List<E> es) {
        return null;
    }



    @Override
    public void put(String source, String series, Long timestamp, List<RecordField> recordFields) {
        LaganaRecord record = new LaganaRecord(source, series, timestamp, recordFields);
        record.id  = checkForExistingOrNot(source, timestamp,record.getId());
        handleFields(record, recordFields);
        this.eventLog.put(record.id, record);
    }

    @Override
    public List<Object> getSuggestedFieldValues(String fieldName) {
        return this.fields.get(fieldName).getKeys();
    }

    @Override
    public List<ILaganaFieldOut> getFields() {
        return this.fields.values().stream().map(LaganaFieldOutImpl::new).collect(Collectors.toList());
    }


    private Map<String, Map<Long,Long>> track = new HashMap<>();

    private Long checkForExistingOrNot(String source, Long timestamp, Long id) {
        Long resultKey = id;
        if (track.containsKey(source)) {
            if (track.get(source).containsKey(timestamp)) {
                resultKey = track.get(source).get(timestamp);
            }
        } else {
            track.put(source, new HashMap<>());
        }
        track.get(source).put(timestamp,resultKey);
        return resultKey;
    }

    private void handleFields(LaganaRecord record, List<RecordField> recordFields) {
        //put these fields in this order so they have higher priority in search
        //TODO: fix the equals stuff in RecordField and do not use the dummy matcher
        RecordField matcher = new RecordField(TIMESTAMP_FIELDNAME, null, null, 0, 0, true, true);
        if (recordFields.contains(matcher) && !this.fields.containsKey(TIMESTAMP_FIELDNAME)) {
            RecordField f = recordFields.get(recordFields.indexOf(matcher));
            this.fields.put(TIMESTAMP_FIELDNAME, new TimestampField(TIMESTAMP_FIELDNAME, ""));
        }

        matcher = new RecordField(CONTENT_FIELDNAME, null, null, 0, 0, true, true);
        if (recordFields.contains(matcher) && !this.fields.containsKey(CONTENT_FIELDNAME)) {
            RecordField f = recordFields.get(recordFields.indexOf(matcher));
            this.fields.put(CONTENT_FIELDNAME, new StringField(CONTENT_FIELDNAME, "", f.type, false, false));
        }

        matcher = new RecordField(SOURCE_FIELDNAME, null, null, 0, 0, true, true);
        if (recordFields.contains(matcher) && !this.fields.containsKey(SOURCE_FIELDNAME)) {
            RecordField f = recordFields.get(recordFields.indexOf(matcher));
            this.fields.put(SOURCE_FIELDNAME, new StringField(SOURCE_FIELDNAME, "", f.type, true, false));
        }

//        matcher = new RecordField(SERIES_FIELDNAME, null, null, 0, 0, true, true);
//        if (recordFields.contains(matcher) && !this.fields.containsKey(SERIES_FIELDNAME)) {
//            RecordField f = recordFields.get(recordFields.indexOf(matcher));
//            this.fields.put(SERIES_FIELDNAME, new StringField(SERIES_FIELDNAME, "", f.type, true, false));
//        }

        for (RecordField rf : recordFields) {
            if (!this.fields.containsKey(rf.name)) {
                this.fields.put(rf.name, mapProperField(rf));
            }
            this.fields.get(rf.name).put(record.id, castValue(rf.value,rf.type));
        }
    }

    private Object castValue(String value, GenericFieldTypeEnum type) {
        switch (type){
            case TIMESTAMP:
            case NUMERIC:
                return Long.parseLong(value);
            case BOOLEAN:
                return Boolean.parseBoolean(value);
            default:
                return value;
        }
    }

    private LaganaField mapProperField(RecordField rf) {
        switch (rf.type) {
            case TIMESTAMP:
            case NUMERIC:
                return new NumericField(rf.name, "", rf.type, rf.isIndexed, rf.isSorted);
            case BOOLEAN:
                return new BooleanField(rf.name, "");
            default:
                return new StringField(rf.name, "", rf.type, rf.isIndexed, rf.isSorted);
        }
    }

}
