package org.example.structure.simpleTree;

import org.example.structure.tree.simpleTree.Node;
import org.example.structure.tree.simpleTree.impl.BaseBinaryTree;

public class TestTree extends BaseBinaryTree {
    public TestTree(Node root) {
        this.root = root;
    }

    public static TestTree emptyTree() {
        return new TestTree(null);
    }
}
