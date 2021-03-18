package my.than.lagana.core.reap.feeder;

import my.than.lagana.core.common.Constants;
import my.than.lagana.core.common.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class FeederMessage {
    public Map<String, MatchRecord> matchRecords = new HashMap<>();

    public String getContent() {
        return this.get(Constants.CONTENT_FIELDNAME);
    }

    public String getSource() {
        return this.get(Constants.SOURCE_FIELDNAME);
    }

    private String get(String fieldName) {
        if (this.matchRecords.containsKey(fieldName)) {
            return this.matchRecords.get(fieldName).value;
        }
        return null;
    }

    public String getTimestamp() {
        return this.get(Constants.TIMESTAMP_FIELDNAME);
    }


    public String getSeries() {
        return this.get(Constants.SERIES_FIELDNAME);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (String k : this.matchRecords.keySet()) {
            MatchRecord r = this.matchRecords.get(k);
            sb.append(StringUtils.format("|name={} |value={} |startPos={}|endPos={}\n", r.name, r.value, r.startPos, r.endPos));
        }
        return sb.toString();
    }
}
