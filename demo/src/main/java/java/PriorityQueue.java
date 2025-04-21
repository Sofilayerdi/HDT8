public interface PriorityQueue<E extends Comparable<E>> {
    boolean add(E element);
    boolean offer(E element);
    E remove();
    E poll();
    E element();
    E peek();
    boolean isEmpty();
    int size();
    void clear();
}