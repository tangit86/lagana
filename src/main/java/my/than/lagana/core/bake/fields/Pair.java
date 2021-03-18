package my.than.lagana.core.bake.fields;

public class Pair<T,E> {
    public Pair(T fst, E snd) {
        this.fst = fst;
        this.snd = snd;
    }

    public T fst;
    public E snd;
}
