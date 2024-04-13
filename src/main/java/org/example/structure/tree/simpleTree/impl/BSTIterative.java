package org.example.structure.tree.simpleTree.impl;

import org.example.structure.tree.simpleTree.BinarySearchTree;
import org.example.structure.tree.simpleTree.Node;
import org.example.structure.tree.simpleTree.model.DataN;

import java.util.Objects;

public class BSTIterative extends BaseBinaryTree implements BinarySearchTree {
    @Override
    public Node searchNode(DataN key) {
        Node node = root;

        while(node != null) {
            if(key.getNPoids() == node.getKey().getNPoids()) {
                return node;
            } else if(key.getNPoids() < node.getKey().getNPoids()) {
                node = node.getLeft();
            } else {
                node = node.getRight();
            }
        }
        return null;
    }

    @Override
    public void insertNode(DataN key) {
        Node newNode = new Node(key);

        if(Objects.isNull(root)) {
            root = newNode;
            return;
        }

        Node node = root;
        while(true) {
            if(key.getNPoids() < node.getKey().getNPoids()) {
                if(Objects.nonNull(node.getLeft())) {
                    node = node.getLeft();
                } else {
                    node.setLeft(newNode);
                    return;
                }
            } else if(key.getNPoids() > node.getKey().getNPoids()) {
                if(Objects.nonNull(node.getRight())) {
                    node = node.getRight();
                } else {
                    node.setRight(newNode);
                    return;
                }
            } else {
                throw new IllegalArgumentException("BST already contains this node");
            }
        }
    }

    @Override
    public void deleteNode(DataN key) {
        Node node = root;
        Node parent = null;

        while(Objects.nonNull(node) && node.getKey().getNPoids() != key.getNPoids()) {
            parent = node;
            if(key.getNPoids() < node.getKey().getNPoids()) {
                node = node.getLeft();
            } else {
                node = node.getRight();
            }
        }

        if(Objects.isNull(node)) {
            return;
        }

        if(Objects.isNull(node.getLeft()) || Objects.isNull(node.getRight())) {
            deleteNodeWithZeroOrOneChild(key, node, parent);
        } else {
            deleteNodeWithTwoChildren(node);
        }

    }

    private void deleteNodeWithZeroOrOneChild(DataN dataN, Node node, Node parent) {
        Node singleChild = Objects.nonNull(node.getLeft()) ? node.getLeft(): node.getRight();

        if(node.getKey().getNPoids() == root.getKey().getNPoids()) {
            root = singleChild;
        } else if(dataN.getNPoids() < parent.getKey().getNPoids()) {
            parent.setLeft(singleChild);
        } else {
            parent.setRight(singleChild);
        }
    }

    private void deleteNodeWithTwoChildren(Node node) {
        Node inOrderSuccessor = node.getRight();
        Node inOrderSuccessorParent = node;

        while(Objects.nonNull(inOrderSuccessor.getLeft())) {
            inOrderSuccessorParent = inOrderSuccessor;
            inOrderSuccessor = inOrderSuccessor.getLeft();
        }

        node.setKey(inOrderSuccessor.getKey());

        if(inOrderSuccessor.getKey().getNPoids() == node.getRight().getKey().getNPoids()) {
            node.setRight(inOrderSuccessor.getRight());
        } else {
            inOrderSuccessorParent.setLeft(inOrderSuccessor.getRight());
        }
    }
}
