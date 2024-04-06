package org.example.structure;

import org.example.structure.graph.Graph;
import org.example.structure.graph.model.Vertex;
import org.example.utils.DataFlow;
import org.example.utils.DataFlowGenerate;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class GraphTest {
    private Graph<DataFlow> testGraph;
    private static final int VERTICLES_SIZE = 1000;
    private static final int EDGES_SIZE = 2000;
    private static final int REMOVE_SIZE = 500;

    @Before
    public void setUp() {
        testGraph = Graph.generateGraph(VERTICLES_SIZE, EDGES_SIZE, true,
                DataFlowGenerate::generateDataFlow);
    }

    @Test
    public void testNbVerticles() {
        assertEquals(testGraph.getGraph().keySet().size(), VERTICLES_SIZE);
    }

    @Test
    public void testNbEdges() {
        assertEquals(EDGES_SIZE, testGraph.getGraph()
                .values()
                .stream()
                .map(List::size)
                .reduce(0, Integer::sum)
                .intValue());
    }

    @Test
    public void testRemoveVertex() {
        List<Vertex<DataFlow>> dataflows = testGraph.getGraph().keySet().stream().toList();
        Random rand = new Random();

        for (int i = 0; i < 5_000; i++) {
            testGraph.removeVertex(dataflows.get(rand.nextInt(REMOVE_SIZE)).getT());
        }
        assertEquals(VERTICLES_SIZE - REMOVE_SIZE, testGraph.getGraph().keySet().size());
    }
}
