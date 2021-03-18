package my.than.lagana.core.reap.conf;

import java.util.ArrayList;
import java.util.List;

public class ReapSchema {

    public List<FieldSchema> fields = new ArrayList<>();

    public List<FeederSchema> feeders = new ArrayList<>();

    public List<MatcherSchema> matchers = new ArrayList<>();

    public List<ReaderSchema> readers = new ArrayList<>();
}
