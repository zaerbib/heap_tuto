package org.example.structure.tree.simpleTree;

import org.example.structure.tree.simpleTree.model.DataN;

public interface BinarySearchTree extends BinaryTree {
    Node searchNode(DataN key);
    void insertNode(DataN key);
    void deleteNode(DataN key);
}
