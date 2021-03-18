package my.than.lagana.core.reap.matcher;

import my.than.lagana.core.common.GenericFieldTypeEnum;
import my.than.lagana.core.common.StringUtils;
import my.than.lagana.core.reap.feeder.FeederMessage;
import my.than.lagana.core.reap.feeder.MatchRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StandardRegexMatcher implements IMatcher {

    final static Logger logger = LoggerFactory.getLogger(StandardRegexMatcher.class);

    public String name;
    public String fieldName;
    public GenericFieldTypeEnum fieldType;
    public String regex;
    public String formatMatchValue;
    public Pattern pattern;
    public String defaultMatchValue;
    public MatchRecord matchRecord;
    public List<IMatcher> anyOf = new ArrayList<>();
    public List<IMatcher> allOf = new ArrayList<>();


    public String getName() {
        return this.name;
    }


    public String getFieldName() {
        return this.fieldName;
    }


    public String getRegex() {
        return this.regex;
    }


    public StandardRegexMatcher(String name, String fieldName, GenericFieldTypeEnum fieldType, String regex, String formatMatchValue, List<IMatcher> anyOf, List<IMatcher> allOf, String defaultMatchValue) {
        this.name = name;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.anyOf.addAll(anyOf);
        this.allOf.addAll(allOf);
        this.regex = null;
        this.pattern = null;
        if (regex != null) {
            this.regex = regex;
            this.pattern = Pattern.compile(regex);
        }
        this.defaultMatchValue = defaultMatchValue;
        this.formatMatchValue = formatMatchValue;
    }

    @Override
    public FeederMessage match(String term) {
        return this.match(term, new FeederMessage());
    }

    public FeederMessage match(String term, FeederMessage message)  {
        try {
            boolean found = false;

            if (this.regex == null) {
                message.matchRecords.put(this.fieldName, new MatchRecord(this.fieldName, this.fieldType, this.defaultMatchValue, -1, -1));
                found = true;
            } else {
                found = doMatch(term,message);
            }

            if (found) {
                //enforcing that only when root matcher is fulifilled we proceed
                return matchChildren(message);
            } else {
                logger.warn(StringUtils.format("{} could not match {}", this.name, term));
            }
        } catch (Exception e) {
            logger.error(StringUtils.format("Matcher {} failed when processing {}", this.name, term), e);
        }

        return null;
    }

    protected boolean doMatch(String term,FeederMessage message) throws ParseException {
        Matcher matcher = pattern.matcher(term);
        String match = null;
        if (matcher.find()) {
            for (int i = 0; i < matcher.groupCount()+1; i++) {
                match = formatMatch(this.formatMatchValue, i, matcher.group(i));
            }

            this.matchRecord = new MatchRecord(this.fieldName, this.fieldType, match, matcher.start(), matcher.end());
            message.matchRecords.put(this.matchRecord.getName(), this.matchRecord);
            return true;
        }
        return false;
    }

    private int getTemplVarsCount(String formatMatchValue) {
        int charCount = 0; //resetting character count
        for(char ch: formatMatchValue.toCharArray()){
            if(ch == '$'){
                charCount++;
            }
        }
        return charCount;
    }

    protected FeederMessage matchChildren(FeederMessage message) {
        for (IMatcher allOfMatcher : this.allOf) {
            if (allOfMatcher.match(message.getContent(), message) == null) {
                return null;
            }
        }
        for (IMatcher anyOfMatcher : this.anyOf) {
            anyOfMatcher.match(message.getContent(), message);
        }
        return message;
    }

    protected String formatMatch(String template, int i, String match) throws ParseException {
        return template.replace("$" + i, match);
    }
}


