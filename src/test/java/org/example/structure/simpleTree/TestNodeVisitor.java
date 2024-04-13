package org.example.structure.simpleTree;

import org.example.structure.tree.simpleTree.Node;
import org.example.structure.tree.simpleTree.model.DataN;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TestNodeVisitor implements Consumer<Node> {
    private List<DataN> dataList = new ArrayList<>();

    @Override
    public void accept(Node node) {
        dataList.add(node.getKey());
    }

    public List<DataN> getDataList() {
        return dataList;
    }
}
