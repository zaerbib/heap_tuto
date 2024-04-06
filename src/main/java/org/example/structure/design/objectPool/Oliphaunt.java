package org.example.structure.design.objectPool;

import java.util.concurrent.atomic.AtomicInteger;

public class Oliphaunt {
    private static final AtomicInteger counter = new AtomicInteger();
    private int id;

    public Oliphaunt() {
        id = counter.incrementAndGet();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("Oliphaunt id=%d", id);
    }
}
