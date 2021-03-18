package my.than.lagana.web.config;

import my.than.lagana.core.bake.IBakeFacade;
import my.than.lagana.core.bake.RecordField;
import my.than.lagana.core.common.subpub.IConsumer;
import my.than.lagana.core.common.subpub.IPubSubProvider;
import my.than.lagana.core.reap.feeder.FeederMessage;
import my.than.lagana.core.reap.feeder.MatchRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DummyReapBakePubSubProvider implements IPubSubProvider<FeederMessage> {

    final static Logger logger = LoggerFactory.getLogger(DummyReapBakePubSubProvider.class);

    public String name = DummyReapBakePubSubProvider.class.getName();
    private IBakeFacade bakeFacade;

    public DummyReapBakePubSubProvider(IBakeFacade bakeFacade) {

        this.bakeFacade = bakeFacade;
    }

    @Override
    public void publish(String stream, FeederMessage object) {

    }

    @Override
    public void subscribe(String stream, IConsumer<FeederMessage> consume) {

    }

    @Override
    public void publish(FeederMessage feederMessage) {
        List<RecordField> recordFields = new ArrayList<>();
        feederMessage.matchRecords.values().stream().forEach(rec -> {
            recordFields.add(mapRecordField(rec));
        });
        bakeFacade.put(feederMessage.getSource(), feederMessage.getSeries(),Long.parseLong(feederMessage.getTimestamp()), recordFields);
    }

    private RecordField mapRecordField(MatchRecord rec) {
        return new RecordField(rec.getName(), rec.getFieldType(), rec.getValue(), rec.getStartPos(), rec.getEndPos());
    }

    @Override
    public void subscribe(IConsumer<FeederMessage> consume) {

    }
}
