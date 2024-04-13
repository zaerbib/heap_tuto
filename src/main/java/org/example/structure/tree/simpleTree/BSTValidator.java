package org.example.structure.tree.simpleTree;

import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class BSTValidator {

    public boolean isBstWithoutDuplicates(BinaryTree tree) {
        return false;
    }

    private boolean isBstWithoutDuplicates(Node node, int minAllowedKey, int maxAllowedKey) {
        if(Objects.isNull(node)) {
            return true;
        }

        if(node.getKey().getNPoids() < minAllowedKey || node.getKey().getNPoids() > maxAllowedKey) {
            return false;
        }

        return isBstWithoutDuplicates(node.getLeft(), minAllowedKey, node.getKey().getNPoids() - 1)
                && isBstWithoutDuplicates(node.getRight(), node.getKey().getNPoids() + 1, maxAllowedKey);
    }
}
