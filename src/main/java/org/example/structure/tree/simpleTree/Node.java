package org.example.structure.tree.simpleTree;

import lombok.Getter;
import lombok.Setter;
import org.example.structure.tree.simpleTree.model.DataN;

@Getter
@Setter
public class Node {
    private DataN key;
    private Node left;
    private Node right;
    private int height;

    public Node(DataN key) {
        this.key = key;
        this.left = null;
        this.right = null;
    }
}
