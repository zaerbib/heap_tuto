package org.example.structure.tree.simpleTree;

import org.example.structure.tree.simpleTree.model.DataN;

import java.util.Comparator;

public class DataNComparator implements Comparator<DataN> {
    @Override
    public int compare(DataN obj1, DataN obj2) {
        return Integer.compare(obj1.getNPoids(), obj2.getNPoids());
    }
}
