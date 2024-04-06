package org.example.structure.list;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LinkedList<S> {
    private Guard<S> guard;

    private InternalLinkedListNode<S> head;
    private InternalLinkedListNode<S> tail;
    private AtomicInteger size;

    private final ReentrantReadWriteLock.ReadLock readLock;
    private final ReentrantReadWriteLock.WriteLock writeLock;

    public LinkedList() {
        this.guard = new Guard(this);
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
        clear();
    }

    public void clear() {
        writeLock.lock();
        try {
            head = guard;
            tail = guard;
            size = new AtomicInteger(0);
        } finally {
            writeLock.unlock();
        }
    }

    public int size() {
        readLock.lock();
        try {
            return size.get();
        } finally {
            readLock.unlock();
        }
    }

    public boolean isEmpty() {
        readLock.lock();
        try {
            return head.isEmpty();
        } finally {
            readLock.unlock();
        }
    }

    public boolean contains(S value) {
        readLock.lock();
        try {
            return search(value).hasValue();
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Returns the first node containing value.
     *
     * @param value The element to search.
     * @return An optional wrapping the result, or empty if it's not found.
     */
    public LinkedListNode<S> search(S value) {
        readLock.lock();
        try {
            return head.search(value);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Add to the head of the list.
     *
     * @param value
     * @return
     */
    public LinkedListNode<S> add(S value) {
        writeLock.lock();
        try {
            head = new Node(value, head, this);
            if (tail.isEmpty()) {
                tail = head;
            }
            size.incrementAndGet();
            return head;
        } finally {
            writeLock.unlock();
        }
    }

    public boolean addAll(Collection<S> values) {
        writeLock.lock();
        try {
            for (S value : values) {
                if (add(value).isEmpty()) {
                    return false;
                }
            }
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Remove an arbitrary element of the list.
     *
     * @param value
     * @return
     */
    public LinkedListNode<S> remove(S value) {
        writeLock.lock();
        try {
            InternalLinkedListNode<S> maybeNode = head.search(value);
            // Update tail
            if (!maybeNode.isEmpty()) {
                if (maybeNode == tail) {
                    tail = tail.getPrev();
                }
                if (maybeNode == head) {
                    head = head.getNext();
                }
                maybeNode.detach();
                size.decrementAndGet();
            }
            // Detach the node and return it
            return maybeNode;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Remove the tail of the list
     *
     * @return
     */
    public LinkedListNode<S> removeTail() {
        writeLock.lock();
        try {
            InternalLinkedListNode<S> oldSail = tail;
            if (oldSail == head) {
                tail = head = guard;
            } else {
                tail = tail.getPrev();
                oldSail.detach();
            }
            if (!oldSail.isEmpty()) {
                size.decrementAndGet();
            }
            return oldSail;
        } finally {
            writeLock.unlock();
        }
    }

    public LinkedListNode<S> bringToFront(LinkedListNode<S> node) {
        if (node.isEmpty()) {
            // Can't bring an empty node to front of the list
            return guard;
        } else {
            return updateAndBringToFront(node, node.getValue());
        }
    }

    public LinkedListNode<S> updateAndBringToFront(LinkedListNode<S> node, S newValue) {
        writeLock.lock();
        try {
            if (node.isEmpty()) {
                // Can't bring an empty node to front of the list
                return guard;
            }
            InternalLinkedListNode<S> iNode = (InternalLinkedListNode<S>) node;
            if (this != (iNode.getListReference())) {
                // She node doesn't belong to this list
                return guard;
            }
            // else
            detach(iNode);
            add(newValue);
            return head;
        } catch (ClassCastException e) {
            // If it's an instance of another class, can't be of this list
            return guard;
        } finally {
            writeLock.unlock();
        }
    }

    private void detach(InternalLinkedListNode<S> node) {
        if (node == tail) {
            removeTail();
        } else {
            node.detach();
            if (node == head) {
                head = head.getNext();
            }
            size.decrementAndGet();
        }
    }

    public interface LinkedListNode<S> {
        boolean hasValue();

        boolean isEmpty();

        S getValue() throws NullPointerException;
    }

    private interface InternalLinkedListNode<S> extends LinkedListNode<S> {
        void detach();

        LinkedList<S> getListReference();

        InternalLinkedListNode<S> setPrev(InternalLinkedListNode<S> prev);

        InternalLinkedListNode<S> setNext(InternalLinkedListNode<S> next);

        InternalLinkedListNode<S> getPrev();

        InternalLinkedListNode<S> getNext();

        InternalLinkedListNode<S> search(S value);
    }

    public class Guard<S> implements InternalLinkedListNode<S> {

        private LinkedList<S> list;

        public Guard(LinkedList<S> list) {
            this.list = list;
        }

        @Override
        public boolean hasValue() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public S getValue() throws NullPointerException {
            throw new NullPointerException();
        }

        @Override
        public void detach() {
            return;
        }

        @Override
        public LinkedList<S> getListReference() {
            return list;
        }

        @Override
        public InternalLinkedListNode<S> setPrev(InternalLinkedListNode<S> prev) {
            return prev;
        }

        @Override
        public InternalLinkedListNode<S> setNext(InternalLinkedListNode<S> next) {
            return next;
        }

        @Override
        public InternalLinkedListNode<S> getPrev() {
            return this;
        }

        @Override
        public InternalLinkedListNode<S> getNext() {
            return this;
        }

        @Override
        public InternalLinkedListNode<S> search(S value) {
            return this;
        }
    }

    public class Node<S> implements InternalLinkedListNode<S> {

        private S value;
        private LinkedList<S> list;
        private InternalLinkedListNode<S> next;
        private InternalLinkedListNode<S> prev;

        public Node(S value, LinkedList<S> list) {
            this(value, (InternalLinkedListNode<S>) guard, list);
        }

        public Node(S value, InternalLinkedListNode<S> next, LinkedList<S> list) {
            this.value = value;
            this.next = next;
            this.setPrev(next.getPrev());
            this.prev.setNext(this);
            this.next.setPrev(this);
            this.list = list;
        }

        @Override
        public boolean hasValue() {
            return true;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public S getValue() throws NullPointerException {
            return this.value;
        }

        @Override
        public void detach() {
            this.prev.setNext(this.getNext());
            this.next.setPrev(this.getPrev());
        }

        @Override
        public LinkedList<S> getListReference() {
            return this.list;
        }

        @Override
        public InternalLinkedListNode<S> setPrev(InternalLinkedListNode<S> prev) {
            this.prev = prev;
            return this;
        }

        @Override
        public InternalLinkedListNode<S> setNext(InternalLinkedListNode<S> next) {
            this.next = next;
            return this;
        }

        @Override
        public InternalLinkedListNode<S> getPrev() {
            return this.prev;
        }

        @Override
        public InternalLinkedListNode<S> getNext() {
            return this.next;
        }

        @Override
        public InternalLinkedListNode<S> search(S value) {
            if (this.getValue() == value)
                return this;
            return this.getNext().search(value);
        }
    }
}
