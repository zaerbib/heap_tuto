package org.example.structure.priorityqueue.heap;

import com.google.common.annotations.VisibleForTesting;
import org.example.structure.priorityqueue.PriorityQueue;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Heap<T extends Comparable<T>> implements PriorityQueue<T> {

    private static final int DEFAULT_BRANCHING_FACTOR = 2;
    public static final int MAX_BRANCHING_FACTOR = 10;
    private List<T> elements;

    private Map<T, Integer> elementPositions;

    private ReentrantReadWriteLock.ReadLock readLock;
    private ReentrantReadWriteLock.WriteLock writeLock;

    private int branchingFactor;

    public Heap() {
        this(DEFAULT_BRANCHING_FACTOR);
    }

    public Heap(int branchingFactor) throws IllegalArgumentException {
        validateBranchingFactor(branchingFactor);
        this.branchingFactor = branchingFactor;
        elements = new ArrayList<>();
        elementPositions = new HashMap<>();
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    public Heap(List<T> elements, int branchingFactor) throws IllegalArgumentException {
        validateBranchingFactor(branchingFactor);
        this.branchingFactor = branchingFactor;
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();

        if(elements == null) {
            throw new NullPointerException("Null arguments(s)");
        }

        int n = elements.size();
        this.elements = new ArrayList<>(elements);
        elementPositions = new HashMap<>();

        for(int i = getParentIndex(n - 1) + 1; i < n; i++) {
            elementPositions.put(this.elements.get(i), i);
        }

        for(int i = getParentIndex(n - 1); i >= 0; i--) {
            pushDown(i);
        }
    }

    @Override
    public boolean add(T element) {
        writeLock.lock();
        try {
            if(this.contains(element)) {
                return false;
            }

            elements.add(element);
            this.bubbleUp(elements.size() - 1);
        } finally {
            writeLock.unlock();
        }
        return true;
    }

    @Override
    public boolean updatePriority(T oldElement, T newElement) {
        writeLock.lock();
        try {
            if(this.isEmpty() || !this.contains(oldElement)) {
                return false;
            }

            int position = elementPositions.get(oldElement);
            elements.set(position, newElement);

            if(hasHigherPriority(newElement, oldElement)) {
                bubbleUp(position);
            } else {
                pushDown(position);
            }

            return true;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean remove(T element) {
        writeLock.lock();
        try {
            if(this.isEmpty() || !this.contains(element)) {
                return false;
            }

            int n = this.size();
            int position = elementPositions.get(element);
            if(position == (n - 1)) {
                elements.remove(position);
                elementPositions.remove(element);
            } else {
                elements.set(position, elements.get(n - 1));
                elements.remove(n - 1);
                elementPositions.remove(element);
                this.pushDown(position);
            }
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Optional<T> top() {
        writeLock.lock();
        try {
            if(this.isEmpty()) {
                return Optional.empty();
            }

            int n = elements.size();
            T top = elements.get(0);

            if(n > 1) {
                elements.set(0, elements.get(n - 1));
                elements.remove(n - 1);
                this.pushDown(0);
            } else {
                elements.remove(0);
            }
            elementPositions.remove(top);

            return Optional.of(top);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Optional<T> peek() {
        readLock.lock();
        try {
            return elements.isEmpty() ? Optional.empty() : Optional.of(elements.get(0));
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean contains(T element) {
        readLock.lock();
        try {
            return elementPositions.containsKey(element);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public int size() {
        readLock.lock();
        try {
            return elements.size();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void clear() {
        writeLock.lock();
        try {
            elements.clear();
            elementPositions.clear();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * compare priority
     *
     * @param element
     * @param withRespectToElement
     * @return
     */
    protected boolean hasHigherPriority(T element, T withRespectToElement) {
        return element.compareTo(withRespectToElement) < 0;
    }

    public int getFirstChildIndex(int index) {
        return branchingFactor * index + 1;
    }

    public int getParentIndex(int index) {
        return (index - 1) / branchingFactor;
    }

    private void validateBranchingFactor(int branchingFactor)
            throws IllegalArgumentException {
        if (branchingFactor < DEFAULT_BRANCHING_FACTOR
                || branchingFactor > MAX_BRANCHING_FACTOR) {
            throw new IllegalArgumentException(
                    String.format("Branching factor needs to be an int between {} and {}"
                            , DEFAULT_BRANCHING_FACTOR, MAX_BRANCHING_FACTOR));
        }
    }

    private void pushDown(int index) {
        int n = elements.size();
        int smallestChildrenIndex = getFirstChildIndex(index);
        T element = elements.get(index);

        while (smallestChildrenIndex < n) {
            int lastChilddrenIndexGuard = Math.min(getFirstChildIndex(index) + branchingFactor, n);

            for (int childrenIndex = smallestChildrenIndex;
                 childrenIndex < lastChilddrenIndexGuard;
                 childrenIndex++) {
                if (hasHigherPriority(elements.get(childrenIndex), elements.get(smallestChildrenIndex))) {
                    smallestChildrenIndex = childrenIndex;
                }
            }
            T child = elements.get(smallestChildrenIndex);

            if (hasHigherPriority(child, element)) {
                elements.set(index, child);
                elementPositions.put(child, index);
                index = smallestChildrenIndex;
                smallestChildrenIndex = getFirstChildIndex(index);
            } else {
                break;
            }
        }
        elements.set(index, element);
        elementPositions.put(element, index);
    }

    private void bubbleUp(int index) {
        int parentIndex;
        T element = elements.get(index);

        while (index > 0) {
            parentIndex = getParentIndex(index);
            T parent = elements.get(parentIndex);

            if (hasHigherPriority(element, parent)) {
                elements.set(index, parent);
                elementPositions.put(parent, index);
                index = parentIndex;
            } else {
                break;
            }
        }
        elements.set(index, element);
        elementPositions.put(element, index);
    }

    @VisibleForTesting
    public boolean checkHeapInvariants() {
        readLock.lock();
        try {
            for (int i = 0, n = size(); i < n; i++) {
                T parent = elements.get(i);
                for (int j = getFirstChildIndex(i), last = getFirstChildIndex(i + 1); j < last; j++) {
                    if (j < n && hasHigherPriority(elements.get(j), parent)) {
                        return false;
                    }
                }
            }
            return true;
        } finally {
            readLock.unlock();
        }
    }
}
