package my.than.lagana.core.reap.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.than.lagana.core.common.GenericFieldTypeEnum;
import my.than.lagana.core.common.LaganaConfiguration;
import my.than.lagana.core.common.subpub.IPubSubProvider;
import my.than.lagana.core.reap.feeder.BaseFeeder;
import my.than.lagana.core.reap.feeder.IFeeder;
import my.than.lagana.core.reap.matcher.IMatcher;
import my.than.lagana.core.reap.matcher.MultilineInvertedMatcher;
import my.than.lagana.core.reap.matcher.StandardRegexMatcher;
import my.than.lagana.core.reap.matcher.TimestampMatcher;
import my.than.lagana.core.reap.reader.ConsoleStreamReaderReader;
import my.than.lagana.core.reap.reader.FileStreamReaderReader;
import my.than.lagana.core.reap.reader.IFeedStreamReader;
import my.than.lagana.core.reap.reader.WebStreamReaderReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReapConfiguration extends LaganaConfiguration {

    private IPubSubProvider pubSubProvider;

    ObjectMapper mapper = new ObjectMapper();

    public ReapConfiguration(String appConfPath, IPubSubProvider pubSubProvider) throws IOException {
        super(appConfPath);
        this.pubSubProvider = pubSubProvider;
    }

    public List<IFeeder> loadFeeders() {
        List<IFeeder> feeders = new ArrayList<>();

        ReapSchema schema = this.getSchema();
        for (FeederSchema feederSchema : schema.feeders) {
            if(!feederSchema.isRegistered){
                continue;
            }
            IFeedStreamReader reader = getReader(feederSchema.reader, schema);
            IMatcher matcher = getMatcher(feederSchema.matcher, schema);
            IFeeder feeder = new BaseFeeder(feederSchema.name, feederSchema.description, feederSchema.targetSystem, reader, matcher, pubSubProvider);
            feeders.add(feeder);
        }
        return feeders;
    }

    private IMatcher getMatcher(MatcherSchema sMatcher, ReapSchema schema) {
        IMatcher matcher = null;
        List<IMatcher> allOf = new ArrayList<>();
        List<IMatcher> anyOf = new ArrayList<>();

        if (sMatcher.allOf != null) {
            sMatcher.allOf.stream().forEach(sm -> {
                allOf.add(getMatcher(sm, schema));
            });
        }
        if (sMatcher.anyOf != null) {
            sMatcher.anyOf.stream().forEach(sm -> {
                anyOf.add(getMatcher(sm, schema));
            });
        }
        switch (sMatcher.field.type) {
            case TIMESTAMP:
                matcher = new TimestampMatcher(sMatcher.name, sMatcher.field.name, GenericFieldTypeEnum.TIMESTAMP, sMatcher.regex, sMatcher.formatMatchValue, anyOf, allOf, null);
                break;
            default:
                if (sMatcher.multilineAndinvert) {
                    matcher = new MultilineInvertedMatcher(sMatcher.name, sMatcher.field.name, sMatcher.field.type, sMatcher.regex, sMatcher.formatMatchValue, anyOf, allOf, sMatcher.defaultMatchValue);
                } else {
                    matcher = new StandardRegexMatcher(sMatcher.name, sMatcher.field.name, sMatcher.field.type, sMatcher.regex, sMatcher.formatMatchValue, anyOf, allOf, sMatcher.defaultMatchValue);
                }
                break;
        }
        return matcher;
    }

    private IFeedStreamReader getReader(ReaderSchema rSchema, ReapSchema schema) {
        IFeedStreamReader reader = null;

        switch (rSchema.type) {
            case FILE:
                reader = new FileStreamReaderReader(rSchema.name, rSchema.series, rSchema.format);
                break;
            case HTTP:
                reader = new WebStreamReaderReader(rSchema.name, rSchema.series, rSchema.format);
                break;
            case CONSOLE_STREAM:
                reader = new ConsoleStreamReaderReader(rSchema.name, rSchema.series, rSchema.format);
                break;
        }
        return reader;
    }


    private File getFile() {
        return new File(getSchemaPath());
    }

    private String getSchemaPath() {
        return this.confMap.get(ConfKeys.ROOT) +  this.confMap.get(ConfKeys.REAP_SCHEMA);
    }

    public ReapSchema getSchema() {
        try {
            return mapper.readValue(getFile(), ReapSchema.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void persistSchema(ReapSchema schema) {
        try {
            mapper.writeValue(getFile(), schema);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
