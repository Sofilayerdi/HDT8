package java;

import java.util.ArrayList;

public class VectorHeap<E extends Comparable<E>> implements PriorityQueue<E> {
    private ArrayList<E> data;

    public VectorHeap() {
        data = new ArrayList<>();
    }

    @Override
    public void add(E element) {
        data.add(element);
        percolateUp(data.size() - 1);
    }

    @Override
    public E remove() {
        if (isEmpty()) {
            return null;
        }
        E minVal = data.get(0);
        data.set(0, data.get(data.size() - 1));
        data.remove(data.size() - 1);
        if (!isEmpty()) {
            percolateDown(0);
        }
        return minVal;
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

    private void percolateUp(int leaf) {
        int parent = (leaf - 1) / 2;
        E value = data.get(leaf);
        while (leaf > 0 && value.compareTo(data.get(parent)) < 0) {
            data.set(leaf, data.get(parent));
            leaf = parent;
            parent = (leaf - 1) / 2;
        }
        data.set(leaf, value);
    }

    private void percolateDown(int root) {
        int heapSize = data.size();
        E value = data.get(root);
        
        while (root < heapSize) {
            int childPos = 2 * root + 1;
            if (childPos >= heapSize) {
                break;
            }
            if (childPos + 1 < heapSize && data.get(childPos + 1).compareTo(data.get(childPos)) < 0) {
                childPos++;
            }
            if (value.compareTo(data.get(childPos)) <= 0) {
                break;
            }
            data.set(root, data.get(childPos));
            root = childPos;
        }
        data.set(root, value);
    }
}