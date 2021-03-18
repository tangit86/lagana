package my.than.lagana.core.common.subpub;

public interface IConsumer<T> {
    String getId();

    void consume(T object);
}
