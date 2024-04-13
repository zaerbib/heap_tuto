package org.example.utils;

import org.example.structure.simpleTree.TestNodeVisitor;
import org.example.structure.tree.simpleTree.BSTValidator;
import org.example.structure.tree.simpleTree.BinaryTree;
import org.example.structure.tree.simpleTree.Node;
import org.example.structure.tree.simpleTree.impl.DFTraversal;
import org.example.structure.tree.simpleTree.model.DataN;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BinaryTreeAssert {

    private final BinaryTree tree;

    public BinaryTreeAssert(BinaryTree tree) {
        this.tree = tree;
    }

    public BinaryTreeAssert assertThatTree(BinaryTree tree) {
        return new BinaryTreeAssert(tree);
    }

    public BinaryTreeAssert isValid(BinaryTree tree) {
        if (!BSTValidator.isBstWithoutDuplicates(tree)) {
            throw new AssertionError("Tree is node valid BST");
        }

        return this;
    }

    public BinaryTreeAssert hasKeyinGivenOrder(List<DataN> keys) {
        TestNodeVisitor visitor = new TestNodeVisitor();
        new DFTraversal(tree).traverseInOrder(visitor);
        assertThat(visitor.getDataList(), is(keys));
        return this;
    }
}
