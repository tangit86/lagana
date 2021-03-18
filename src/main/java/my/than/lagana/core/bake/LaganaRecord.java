package my.than.lagana.core.bake;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LaganaRecord {
    Long id;

    public Long getId() {
        return id;
    }

    String source;
    String series;
    Long timestamp;
    List<RecordField> fields;

    public String getSeries() {
        return series;
    }

    public String getSource() {
        return source;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public List<RecordField> getFields() {
        return fields;
    }

    private static AtomicInteger seq = new AtomicInteger();

    public LaganaRecord(String source, String series, Long timestamp, List<RecordField> fields) {
        this.id = (long) seq.incrementAndGet();
        this.source = source;
        this.series = series;
        this.timestamp = timestamp;
        this.fields = fields;
    }
}
