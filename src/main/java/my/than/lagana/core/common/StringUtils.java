package my.than.lagana.core.common;

import java.text.MessageFormat;
import java.util.regex.Pattern;

public class StringUtils {

    public static String format(String s, Object... arg) {
        int i = 0;
        while (s.contains("{}")) {
            s = s.replaceFirst(Pattern.quote("{}"), "{" + i++ + "}");
        }
        return MessageFormat.format(s, arg);
    }
}
