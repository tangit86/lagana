package my.than.lagana.web.config;

import my.than.lagana.core.bake.LaganaRecord;
import my.than.lagana.core.common.subpub.IConsumer;
import my.than.lagana.core.common.subpub.IPubSubProvider;

public class DummyBakeDeliPubSubProvider implements IPubSubProvider<LaganaRecord> {

    public DummyBakeDeliPubSubProvider() {
    }

    @Override
    public void publish(String stream, LaganaRecord object) {

    }

    @Override
    public void subscribe(String stream, IConsumer<LaganaRecord> consume) {

    }

    @Override
    public void publish(LaganaRecord object) {

    }

    @Override
    public void subscribe(IConsumer<LaganaRecord> consume) {

    }
}
