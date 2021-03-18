package my.than.lagana.core.lagana.dashboard;

import my.than.lagana.core.bake.LaganaRecord;
import my.than.lagana.core.common.StringUtils;
import my.than.lagana.core.common.subpub.IConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dashboard implements IConsumer<LaganaRecord> {

    final static Logger logger = LoggerFactory.getLogger(Dashboard.class);

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void consume(LaganaRecord object) {
        logger.debug(StringUtils.format("Received event to process {}",object.getFields().get(0)));
    }

}
