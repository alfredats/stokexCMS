package visa.vttp.paf.stokexCMS.model;

public class Tuple<T,V> {
    private T first;
    private V second;

    public Tuple(T first, V second) {
        this.first = first;
        this.second = second;
    }    

    @Override
    public String toString() {
        return "Tuple [first=" + first + ", second=" + second + "]";
    }

    /**
     * @return T return the first
     */
    public T getFirst() {
        return first;
    }

    /**
     * @param first the first to set
     */
    public void setFirst(T first) {
        this.first = first;
    }

    /**
     * @return V return the second
     */
    public V getSecond() {
        return second;
    }

    /**
     * @param second the second to set
     */
    public void setSecond(V second) {
        this.second = second;
    }
}    

