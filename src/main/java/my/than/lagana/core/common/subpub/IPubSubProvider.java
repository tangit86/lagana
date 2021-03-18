package my.than.lagana.core.common.subpub;

public interface IPubSubProvider<T> {

    void publish(String stream, T object);

    void subscribe(String stream, IConsumer<T> consume);


    void publish(T object);

    void subscribe(IConsumer<T> consume);
}
