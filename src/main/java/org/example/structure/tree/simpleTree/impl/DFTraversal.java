package org.example.structure.tree.simpleTree.impl;

import org.example.structure.tree.simpleTree.BinaryTree;
import org.example.structure.tree.simpleTree.DepthFirstTraversal;
import org.example.structure.tree.simpleTree.Node;

import java.util.Objects;
import java.util.function.Consumer;

public final class DFTraversal implements DepthFirstTraversal {
    private final BinaryTree tree;

    public DFTraversal(BinaryTree tree) {
        this.tree = tree;
    }

    @Override
    public void traversePreOrder(Consumer<Node> vistor) {
        traversePreOrder(tree.getRoot(), vistor);
    }

    private static void traversePreOrder(Node node, Consumer<Node> visitor) {
        if(Objects.isNull(node)) {
            return;
        }

        visitor.accept(node);
        traversePreOrder(node.getLeft(), visitor);
        traversePreOrder(node.getRight(), visitor);
    }

    @Override
    public void traversePostOrder(Consumer<Node> visitor) {
        traversePostOrder(tree.getRoot(), visitor);
    }

    private static void traversePostOrder(Node node, Consumer<Node> visitor) {
        if(Objects.isNull(node)) {
            return;
        }

        traversePostOrder(node.getLeft(), visitor);
        traversePostOrder(node.getRight(), visitor);
        visitor.accept(node);
    }

    @Override
    public void traverseInOrder(Consumer<Node> visitor) {

    }

    private static void traverseInOrder(Node node, Consumer<Node> visitor) {
        if(Objects.isNull(node)) {
            return;
        }

        traverseInOrder(node.getLeft(), visitor);
        visitor.accept(node);
        traverseInOrder(node.getRight(), visitor);
    }
}
