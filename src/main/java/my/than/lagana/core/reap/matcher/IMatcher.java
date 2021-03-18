package my.than.lagana.core.reap.matcher;

import my.than.lagana.core.reap.feeder.FeederMessage;


public interface IMatcher {
    String getName();
    FeederMessage match(String term);
    FeederMessage match(String term,FeederMessage message);
}
