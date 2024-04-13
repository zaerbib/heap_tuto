package org.example.structure.tree.simpleTree.impl;

import org.example.structure.tree.simpleTree.BinaryTree;
import org.example.structure.tree.simpleTree.Node;

public class BaseBinaryTree implements BinaryTree {
    protected Node root;
    @Override
    public Node getRoot() {
        return root;
    }
}
