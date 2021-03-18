package my.than.lagana.core.bake.fields;

import my.than.lagana.core.bake.RecordField;
import my.than.lagana.core.bake.filters.OperatorEnum;
import my.than.lagana.core.common.GenericFieldTypeEnum;
import my.than.lagana.core.common.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public abstract class LaganaField<T> {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String name;
    private String desc;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            if (o.getClass().equals(String.class)) {
                return this.name.equals(o.toString());
            }
            if (o.getClass().equals(RecordField.class)) {
                return ((RecordField) o).getName().equals(this.name);
            }
        }
        LaganaField<?> that = (LaganaField<?>) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    private GenericFieldTypeEnum fieldType;
    private boolean isIndexed;
    private boolean isSorted;

    protected Map<Long, T> scanIds;
    protected Map<T, Set<Long>> indexedItems;

    public LaganaField(String name, String desc, GenericFieldTypeEnum fieldType, boolean isIndexed, boolean isSorted) {
        this.name = name;
        this.desc = desc;
        this.fieldType = fieldType;
        this.isIndexed = isIndexed;
        this.isSorted = isSorted;

        if (isIndexed || isSorted) {
            if (isSorted) {
                this.isIndexed = true;
            }
            indexedItems = isSorted ? new TreeMap<T, Set<Long>>() : new HashMap<T, Set<Long>>();
        } else {
            scanIds = new HashMap<>();
        }
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public GenericFieldTypeEnum getFieldType() {
        return fieldType;
    }

    public boolean isIndexed() {
        return isIndexed || isSorted;
    }

    public void put(Long id, T value) {
        if (this.isIndexed) {
            if (!this.indexedItems.containsKey(value)) {
                this.indexedItems.put(value, new HashSet<>());
            }
            this.indexedItems.get(value).add(id);
        } else {
            this.scanIds.put(id, value);
        }
    }

    public List<T> getKeys(){
        return this.isIndexed ? new ArrayList(this.indexedItems.keySet()): Arrays.asList();
    }

    public Set<Long> filter(List<Pair<T, OperatorEnum>> filters, Set<Long> prevMatches) throws Exception {
        Set<Long> result = new HashSet<>();

        if (this.isIndexed) {
            result.addAll(filterValueInd(filters, prevMatches));
        } else {
            this.scanIds.entrySet().stream()
                    .filter(o -> prevMatches.isEmpty() || prevMatches.contains(o.getKey()))
                    .filter(o -> filters.isEmpty() || applyFiltersOnValue(o.getValue(), filters))
                    .forEach(o -> result.add(o.getKey()));
        }
        return result;
    }

    private boolean applyFiltersOnValue(T current, List<Pair<T, OperatorEnum>> filters) {
        boolean state = true;
        for (Pair<T, OperatorEnum> filter : filters) {
            state &= filterValue(current, filter.fst, filter.snd);
        }
        return state;
    }

    protected Set<Long> filterValueInd(List<Pair<T, OperatorEnum>> filters, Set<Long> prevMatches) throws Exception {
        Set<Long> result = new HashSet<>();
        if (filters.isEmpty()) {
            return this.indexedItems.values().stream().flatMap(x -> x.stream()).collect(Collectors.toSet());
        } else if (filters.size() == 1 && filters.get(0).snd.equals(OperatorEnum.EQ)) {
            return this.indexedItems.get(filters.get(0).fst);
            //TODO: for EQ and IN , make it more optimized, do not go scanning
        } else {
            for (T key : this.indexedItems.keySet()) {
                if (applyFiltersOnValue(key, filters)) {
                    result.addAll(this.indexedItems.get(key).stream()
                            .filter(o -> prevMatches.isEmpty() || prevMatches.contains(o))
                            .collect(Collectors.toList()));
                }
            }
        }
        return result;
    }

    protected boolean filterValue(T currentValue, T filterValue, OperatorEnum op) {
        try {
            if (op.equals(OperatorEnum.EQ)) {
                return isEQ(currentValue, filterValue);
            }
            if (op.equals(OperatorEnum.NOTEQ)) {
                return isNOTEQ(currentValue, filterValue);
            }
            if (op.equals(OperatorEnum.IN)) {
                return isIN(currentValue, filterValue);
            }
            if (op.equals(OperatorEnum.NOTIN)) {
                return isNOTIN(currentValue, filterValue);
            }
            if (op.equals(OperatorEnum.EMPTY)) {
                return isEMPTY(currentValue, filterValue);
            }
            if (op.equals(OperatorEnum.NOTEMPTY)) {
                return isNOTEMPTY(currentValue, filterValue);
            }
            if (op.equals(OperatorEnum.CONTAINS)) {
                return isCONTAINS(currentValue, filterValue);
            }
            if (op.equals(OperatorEnum.NOTCONTAINS)) {
                return isNOTCONTAINS(currentValue, filterValue);
            }
            if (op.equals(OperatorEnum.LT)) {
                return isLT(currentValue, filterValue);
            }
            if (op.equals(OperatorEnum.GT)) {
                return isGT(currentValue, filterValue);
            }
            if (op.equals(OperatorEnum.BETWEEN)) {
                return isBETWEEN(currentValue, filterValue);
            }
            if (op.equals(OperatorEnum.REGEX)) {
                return isREGEX(currentValue, filterValue);
            }
        } catch (Exception e) {
            logger.error(StringUtils.format("Could perform {} match {}", op));
        }
        return false;
    }

    protected boolean isBETWEEN(T currentValue, T filterValue) throws Exception {
        return false;
    }

    protected boolean isEQ(T currentValue, T filterValue) {
        return currentValue.equals(filterValue);
    }

    protected boolean isNOTEQ(T currentValue, T filterValue) {
        return !isEQ(currentValue, filterValue);
    }

    protected boolean isEMPTY(T currentValue, T filterValue) {
        return currentValue == null;
    }

    protected boolean isNOTEMPTY(T currentValue, T filterValue) {
        return !isEMPTY(currentValue, filterValue);
    }

    protected boolean isIN(T currentValue, T filterValue) throws Exception {
        throw new Exception("Not Implemented");
    }

    protected boolean isNOTIN(T currentValue, T filterValue) throws Exception {
        throw new Exception("Not Implemented");
    }

    protected boolean isCONTAINS(T currentValue, T filterValue) throws Exception {
        throw new Exception("Not Implemented");
    }

    protected boolean isNOTCONTAINS(T currentValue, T filterValue) throws Exception {
        throw new Exception("Not Implemented");
    }

    protected boolean isLT(T currentValue, T filterValue) throws Exception {
        throw new Exception("Not Implemented");
    }

    protected boolean isGT(T currentValue, T filterValue) throws Exception {
        throw new Exception("Not Implemented");
    }

    protected boolean isREGEX(T currentValue, T filterValue) throws Exception {
        throw new Exception("Not Implemented");
    }

}
