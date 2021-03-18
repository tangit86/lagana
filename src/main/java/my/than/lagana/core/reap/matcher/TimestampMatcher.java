package my.than.lagana.core.reap.matcher;

import my.than.lagana.core.common.GenericFieldTypeEnum;
import my.than.lagana.core.common.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TimestampMatcher extends StandardRegexMatcher {

    public TimestampMatcher(String name, String fieldName, GenericFieldTypeEnum fieldType, String regex, String formatMatchValue, List<IMatcher> anyOf, List<IMatcher> allOf, String defaultMatchValue) {
        super(name, fieldName,fieldType, regex, formatMatchValue, anyOf, allOf, defaultMatchValue);
    }

    @Override
    protected String formatMatch(String template, int i, String match) throws ParseException {
        DateFormat df = new SimpleDateFormat(this.formatMatchValue, Locale.ENGLISH);
        long millis = df.parse(match).getTime();
        logger.debug(StringUtils.format("found date {} , using templ: {} -> millis: {}",match,this.formatMatchValue,millis));
        return  Long.toString(millis);
    }
}
