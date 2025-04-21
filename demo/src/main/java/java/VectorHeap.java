import java.util.ArrayList;
import java.util.NoSuchElementException;

public class VectorHeap<E extends Comparable<E>> implements PriorityQueue<E> {
    private ArrayList<E> data;

    public VectorHeap() {
        data = new ArrayList<>();
    }

    @Override
    public boolean add(E element) {
        data.add(element);
        percolateUp(data.size() - 1);
        return true;
    }

    @Override
    public boolean offer(E element) {
        return add(element);
    }

    @Override
    public E remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        return poll();
    }

    @Override
    public E poll() {
        if (isEmpty()) {
            return null;
        }
        E min = data.get(0);
        E last = data.remove(data.size() - 1);
        if (!isEmpty()) {
            data.set(0, last);
            percolateDown(0);
        }
        return min;
    }

    @Override
    public E element() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        return peek();
    }

    @Override
    public E peek() {
        return isEmpty() ? null : data.get(0);
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public void clear() {
        data.clear();
    }

    private void percolateUp(int index) {
        E x = data.get(index);
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            E parent = data.get(parentIndex);
            if (x.compareTo(parent) >= 0) {
                break;
            }
            data.set(index, parent);
            index = parentIndex;
        }
        data.set(index, x);
    }

    private void percolateDown(int index) {
        int size = data.size();
        E x = data.get(index);
        while (index < size / 2) {
            int leftChild = 2 * index + 1;
            int rightChild = leftChild + 1;
            int smallestChild = leftChild;
            
            if (rightChild < size && 
                data.get(rightChild).compareTo(data.get(leftChild)) < 0) {
                smallestChild = rightChild;
            }
            
            if (data.get(smallestChild).compareTo(x) >= 0) {
                break;
            }
            
            data.set(index, data.get(smallestChild));
            index = smallestChild;
        }
        data.set(index, x);
    }
}