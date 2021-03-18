package my.than.lagana.core.reap;

import my.than.lagana.core.reap.conf.ReapSchema;
import my.than.lagana.core.reap.feeder.IFeeder;

import java.util.List;

public interface IReapFacade {

    void init() throws Exception;

    void start(String feeder);

    void stop(String feeder);

    void clear(String feeder);

    void register(IFeeder feeder);

    void register(String feeder);

    void unregister(String feederName);

    List<IFeederOverview> getOverview(List<String> feeders);

    ReapSchema getSchema();

    void saveSchema(ReapSchema reapSchema);
}
