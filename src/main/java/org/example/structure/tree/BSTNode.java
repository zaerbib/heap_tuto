package org.example.structure.tree;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class BSTNode<T extends Comparable<T>> {
    private T key;
    private BSTNode<T> left;
    private BSTNode<T> right;

    private Random rnd = new Random();

    public BSTNode(T key) {
        this.key = key;
        this.left = null;
        this.right = null;
    }

    public Optional<BSTNode<T>> getLeft() {
        return Optional.ofNullable(left);
    }

    public Optional<BSTNode<T>> getRight() {
        return Optional.ofNullable(right);
    }

    public void setLeft(BSTNode<T> left) {
        this.left = left;
    }

    public void setRight(BSTNode<T> right) {
        this.right = right;
    }

    public boolean isLeaf() {
        return this.getLeft().isEmpty() && this.getRight().isEmpty();
    }

    public T getKey() {
        return this.key;
    }

    public int size() {
        return 1 + this.getLeft().map(BSTNode::size).orElse(0)
                + this.getRight().map(BSTNode::size).orElse(0);
    }

    public int height() {
        return 1 + Math.max(this.getLeft().map(BSTNode::height).orElse(0),
                this.getRight().map(BSTNode::height).orElse(0));
    }

    public T min() {
        return this.getLeft().map(BSTNode::min).orElse(key);
    }

    public T max() {
        return this.getRight().map(BSTNode::max).orElse(key);
    }

    public Optional<BSTNode<T>> search(T targetKey) {
        if(targetKey.equals(key)) {
            return Optional.of(this);
        }

        Optional<BSTNode<T>> result = Optional.empty();

        if(targetKey.compareTo(key) <= 0) {
            result = this.getLeft().flatMap(lN -> lN.search(targetKey));
        }

        if(targetKey.compareTo(key) > 0) {
            result = this.getRight().flatMap(rN -> rN.search(targetKey));
        }

        return result;
    }

    public Optional<BSTNode<T>> add(T key) {
        if(key.compareTo(this.getKey()) <= 0) {
            this.setLeft(this.getLeft()
                             .flatMap(node -> node.add(key))
                                .orElse(new BSTNode<>(key)));
        } else {
            this.setRight(this.getRight()
                              .flatMap(node -> node.add(key))
                                .orElse(new BSTNode<>(key)));
        }

        return Optional.of(this);
    }

    public Optional<BSTNode<T>> remove(T targetKey, AtomicBoolean wasRemoved) {
        if(targetKey.equals(key)) {
            wasRemoved.set(true);
            if(this.isLeaf()) {
                return Optional.empty();
            } else if(this.getRight().isEmpty()  || (this.getLeft().isPresent() && rnd.nextBoolean())) {
                this.getLeft().map(BSTNode::max).ifPresent(prevKey -> {
                    this.key = prevKey;
                    this.setLeft(this.getLeft().flatMap(lN -> lN.remove(prevKey, wasRemoved))
                            .orElse(null));
                });
            } else {
                this.getRight().map(BSTNode::min).ifPresent(nextKey -> {
                    this.key = nextKey;
                    this.setRight(this.getRight().flatMap(rN -> rN.remove(nextKey, wasRemoved))
                            .orElse(null));
                });
            }
            return Optional.of(this);
        }

        if(targetKey.compareTo(key) <= 0) {
            this.setLeft(this.getLeft().flatMap(lN -> lN.remove(targetKey, wasRemoved))
                    .orElse(null));
        } else {
            this.setRight(this.getRight().flatMap(rN -> rN.remove(targetKey, wasRemoved))
                    .orElse(null));
        }

        return Optional.of(this);
    }

    public void cleanUp() {
        this.getLeft().ifPresent(BSTNode::cleanUp);
        this.setLeft(null);
        this.getRight().ifPresent(BSTNode::cleanUp);
        this.setRight(null);
    }

    public boolean checkBSTInvariant() {
        if(this.getLeft().map(n -> n.getKey().compareTo(this.key) > 0).orElse(false)
                || this.getRight().map(n -> n.getKey().compareTo(key) <= 0).orElse(false)) {
            return false;
        }

        return this.getLeft().map(BSTNode::checkBSTInvariant).orElse(true)
                && this.getRight().map(BSTNode::checkBSTInvariant).orElse(true);
    }
}
