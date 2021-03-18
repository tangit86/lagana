package my.than.lagana.core.reap.matcher;

import my.than.lagana.core.common.GenericFieldTypeEnum;
import my.than.lagana.core.reap.feeder.FeederMessage;
import my.than.lagana.core.reap.feeder.MatchRecord;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class MultilineInvertedMatcher extends StandardRegexMatcher implements IMatcher {

    public MultilineInvertedMatcher(String name, String fieldName, GenericFieldTypeEnum fieldType, String regex, String formatMatchValue, List<IMatcher> anyOf, List<IMatcher> allOf, String defaultMatchValue) {
        super(name, fieldName, fieldType, regex, formatMatchValue, anyOf, allOf, defaultMatchValue);
    }

    FeederMessage prev;
    StringBuilder sb = new StringBuilder();
    @Override
    public String getName() {
        return null;
    }

    @Override
    public FeederMessage match(String term, FeederMessage message) {
        boolean match = false;
        try {
            match = super.doMatch(term,message);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //match
        if(!match){
            this.sb.append(term);
            return null;
        }

        FeederMessage result = getCopy(this.prev, this.sb.toString());
        this.sb = new StringBuilder();
        this.sb.append(term);
        this.prev = message;
        return result == null ? null : super.matchChildren(result);
    }

    private FeederMessage getCopy(FeederMessage prev, String fullContent) {
        FeederMessage result = new FeederMessage();
        if(prev==null){
            return null;
        }
        for (String key : prev.matchRecords.keySet()) {
            MatchRecord value = prev.matchRecords.get(key);
            if (key.equals(this.fieldName)) {
                value = getMatchRecordCopy(value, fullContent);
            } else {
                value = getMatchRecordCopy(value, null);
            }
            result.matchRecords.put(key, value);
        }
        return result;
    }

    private MatchRecord getMatchRecordCopy(MatchRecord value, String fullContent) {
        return new MatchRecord(value.getName(), value.getFieldType(), fullContent != null ? fullContent : value.getValue(), value.getStartPos(), value.getEndPos());
    }


    private void append(StringBuilder sb,String line){
        sb.append(line);
        sb.append("\r\n");
    }
}
