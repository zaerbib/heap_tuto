package org.example.structure.tree.simpleTree.impl;

import org.example.structure.tree.simpleTree.BinaryTree;
import org.example.structure.tree.simpleTree.Node;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Consumer;

public final class BFTraversal {
    private final BinaryTree tree;

    public BFTraversal(BinaryTree tree) {
        this.tree = tree;
    }

    public void traverseLevelOrder(Consumer<Node> visitor) {
        traverseLevelOrder(tree.getRoot(), visitor);
    }

    private static void traverseLevelOrder(Node root, Consumer<Node> visitor) {
        if(Objects.isNull(root)) {
            return;
        }

        Queue<Node> queue = new ArrayDeque<>();
        queue.add(root);

        while(!queue.isEmpty()) {
            Node node = queue.poll();
            visitor.accept(node);

            if(Objects.nonNull(node.getLeft())) {
                queue.add(node.getLeft());
            }
            if(Objects.nonNull(node.getRight())) {
                queue.add(node.getRight());
            }
        }
    }
}
