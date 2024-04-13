package org.example.structure.tree.simpleTree;

import java.util.function.Consumer;

public interface DepthFirstTraversal {
    void traversePreOrder(Consumer<Node> vistor);
    void traversePostOrder(Consumer<Node> vistor);
    void traverseInOrder(Consumer<Node> vistor);
}
