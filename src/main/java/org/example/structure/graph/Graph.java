package org.example.structure.graph;

import lombok.Getter;
import lombok.Setter;
import org.example.structure.graph.model.Vertex;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Getter
@Setter
public class Graph<T> {
    private Map<Vertex<T>, List<Vertex<T>>> graph;

    public Graph() {
        this.graph = new ConcurrentHashMap<>();
    }

    public void addVertex(T add) {
        graph.putIfAbsent(new Vertex<>(add), new ArrayList<>());
    }

    public void removeVertex(T remove) {
        Vertex<T> vertex = new Vertex<>(remove);
        graph.values().forEach(item -> item.remove(vertex));
        graph.remove(vertex, graph.get(vertex));
    }

    public void addEdgeDirect(T first, T second) {
        Vertex<T> firstVertex = new Vertex<>(first);
        Vertex<T> secondVertex = new Vertex<>(second);

        graph.get(firstVertex).add(secondVertex);
    }

    public void addEdgeUnDirect(T first, T second) {
        Vertex<T> firstVertex = new Vertex<>(first);
        Vertex<T> secondVertex = new Vertex<>(second);

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
                                             boolean direct,
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
            int destination = rand.nextInt(nbVerticles);
            if(source != destination) {
                String edge = source + " " + destination;
               if(!edges.contains(edge)) {
                   if(direct) {
                       tGraph.addEdgeDirect(listKeys.get(source).getT(),
                               listKeys.get(destination).getT());
                   } else {
                       tGraph.addEdgeUnDirect(listKeys.get(source).getT(),
                               listKeys.get(destination).getT());
                   }
                   edges.add(edge);
                   nbEdges--;
               }
            }
        }

        return tGraph;
    }

    public static <T> Graph<T> generateGraphFromList(List<T> list, boolean direct) {
        Graph<T> graph = new Graph<>();
        Random rand = new Random();
        int nbVerticles = list.size();
        int nbEdges = 4 * list.size();

        for (T t : list) {
            graph.addVertex(t);
        }

        Set<String> edges = new HashSet<>();

        while(nbEdges > 0) {
            int source = rand.nextInt(nbVerticles);
            int destination = rand.nextInt(nbVerticles);
            if(source != destination) {
                String edge = source + " " + destination;
                if(!edges.contains(edge)) {
                    if(direct) {
                        graph.addEdgeDirect(list.get(source),
                                list.get(destination));
                    } else {
                        graph.addEdgeUnDirect(list.get(source),
                                list.get(destination));
                    }
                    edges.add(edge);
                    nbEdges--;
                }
            }
        }

        return graph;
    }

}
