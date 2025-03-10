package org.example.structure.graph;

import lombok.Getter;
import lombok.Setter;
import org.example.structure.graph.model.Vertex;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Getter
@Setter
public class Graph<T> {
    private Map<Vertex<T>, List<Vertex<T>>> graph;

    public Graph() {
        this.graph = new HashMap<>();
    }

    public void addVertex(T add) {
        graph.putIfAbsent(new Vertex<>(add), new ArrayList<>());
    }

    public void removeVertex(T remove) {
        Vertex<T> vertex = new Vertex<T>(remove);
        graph.values().forEach(item -> item.remove(vertex));
        graph.remove(new Vertex<>(remove));
    }

    public void addEdgeDirect(T first, T second) {
        Vertex<T> firstVertex = new Vertex<T>(first);
        Vertex<T> secondVertex = new Vertex<T>(second);

        graph.get(firstVertex).add(secondVertex);
    }

    public void addEdgeUnDirect(T first, T second) {
        Vertex<T> firstVertex = new Vertex<T>(first);
        Vertex<T> secondVertex = new Vertex<T>(second);

        graph.get(firstVertex).add(secondVertex);
        graph.get(secondVertex).add(firstVertex);
    }

    public void removeEdge(T first, T second) {
        Vertex<T> firstVertex = new Vertex<>(first);
        Vertex<T> secondVertex = new Vertex<>(second);

        List<Vertex<T>> firstEdges = graph.get(firstVertex);
        List<Vertex<T>> secondEdges = graph.get(secondVertex);

        if(Objects.nonNull(firstEdges) && !firstEdges.isEmpty()) {
            firstEdges.remove(firstVertex);
        }

        if(Objects.nonNull(secondEdges) && !secondEdges.isEmpty()) {
            secondEdges.remove(secondVertex);
        }
    }

    public List<Vertex<T>> getAdjVerticles(T elt) {
        return graph.get(new Vertex<>(elt));
    }

    public static <T> Graph<T> generateGraph(int nbVerticles,
                                             int nbEdges,
                                             Supplier<T> generate) {
        Graph<T> tGraph = new Graph<>();
        Random rand = new Random();

        for(int i = 0; i < nbVerticles; i++) {
            tGraph.addVertex(generate.get());
        }

        Set<String> edges = new HashSet<>();
        List<Vertex<T>> listKeys = tGraph.getGraph().keySet().stream().toList();
        while(nbEdges > 0) {
            int source = rand.nextInt(nbVerticles);
            int destination = rand.nextInt(nbEdges);
            if(source != destination) {
                String edge = source + " " + destination;
               if(!edges.contains(edge)) {
                   tGraph.addEdgeDirect(listKeys.get(source).getT(), listKeys.get(destination).getT());
                   edges.add(edge);
                   nbEdges--;
               }
            }
        }

        return tGraph;
    }
}
