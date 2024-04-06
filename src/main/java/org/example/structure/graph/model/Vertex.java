package org.example.structure.graph.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Vertex<T> {
    private T t;

    public Vertex(T t) {
        this.t = t;
    }
}
