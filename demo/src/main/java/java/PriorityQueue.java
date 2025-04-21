package java;

public interface PriorityQueue<E extends Comparable<E>> {
    void add(E element);
    E remove();
    E peek();
    boolean isEmpty();
    int size();
}