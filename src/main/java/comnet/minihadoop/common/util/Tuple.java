package comnet.minihadoop.common.util;

public class Tuple<E1, E2> {
    private E1 first;
    private E2 second;
    public Tuple(E1 first, E2 second) {
        this.first = first;
        this.second = second;
    }

    public E2 getSecond() {
        return second;
    }

    public E1 getFirst() {
        return first;
    }
}
