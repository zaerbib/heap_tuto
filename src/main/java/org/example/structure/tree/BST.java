package org.example.structure.tree;

import org.example.structure.tree.contract.Stree;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BST<T extends Comparable<T>> implements Stree<T> {

    private Optional<BSTNode<T>> root;

    private ReentrantReadWriteLock.ReadLock readLock;
    private ReentrantReadWriteLock.WriteLock writeLock;

    public BST() {
        root = Optional.empty();
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    @Override
    public Optional<T> min() {
        readLock.lock();
        try {
            return root.map(BSTNode::min);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Optional<T> max() {
        readLock.lock();
        try {
            return root.map(BSTNode::max);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Optional<T> search(T element) {
        readLock.lock();
        try {
            return root.flatMap(r -> r.search(element)).map(BSTNode::getKey);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        readLock.lock();
        try {
            return root.isEmpty();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public int size() {
        readLock.lock();
        try {
            return root.map(BSTNode::size).orElse(0);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public int height() {
        readLock.lock();
        try {
            return root.map(BSTNode::height).orElse(0);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean add(T element) {
        writeLock.lock();
        try {
            root = root.flatMap(r -> r.add(element)).or(() -> Optional.of(new BSTNode<>(element)));
            // Always add entries, allowing duplicates
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean remove(T element) {
        writeLock.lock();
        try {
            AtomicBoolean elementRemoved = new AtomicBoolean(false);
            root = root.flatMap(node -> node.remove(element, elementRemoved));
            return elementRemoved.get();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void clear() {
        Optional<BSTNode<T>> oldRoot = Optional.empty();
        writeLock.lock();
        try {
            oldRoot = root;
            root = Optional.empty();
        } finally {
            writeLock.unlock();
            oldRoot.ifPresent(BSTNode::cleanUp);
        }
    }
}
