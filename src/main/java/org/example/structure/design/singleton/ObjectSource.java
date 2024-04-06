package org.example.structure.design.singleton;

import java.util.concurrent.atomic.AtomicInteger;

public class ObjectSource {
    private static final AtomicInteger atomic = new AtomicInteger();
    private int id;

    public ObjectSource() {
        this.id = atomic.incrementAndGet();
    }

    public int getId() {
        return this.id;
    }

    public String toString() {
        return String.format("ObjectSource numerous %d", this.id);
    }
}
