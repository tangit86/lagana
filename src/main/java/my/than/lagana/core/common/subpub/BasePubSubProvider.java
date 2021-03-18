package my.than.lagana.core.common.subpub;

import java.util.*;

public class BasePubSubProvider implements IPubSubProvider {

    Map<String, Set<IConsumer>> consumersMap = new HashMap<>();

    @Override
    public void publish(String stream, Object object) {
        if (!consumersMap.containsKey(stream)) {
            return;
        }
        consumersMap.get(stream).forEach(tiConsumer -> tiConsumer.consume(object));
    }

    @Override
    public void subscribe(String stream, IConsumer consumer) {
        if (!consumersMap.containsKey(stream)) {
            consumersMap.put(stream, new HashSet<>());
        }
        consumersMap.get(stream).add(consumer);
    }


    @Override
    public void publish(Object object) {
        publish("", object);
    }

    @Override
    public void subscribe(IConsumer consumer) {
        subscribe("", consumer);
    }

    //TODO: thread safety (not that I will care)
}
