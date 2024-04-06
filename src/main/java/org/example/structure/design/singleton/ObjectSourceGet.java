package org.example.structure.design.singleton;

public class ObjectSourceGet {
    private ObjectSourceGet() {}

    public static ObjectSource getInstance() {
        return SingletonPattern.source;
    }

    private static class SingletonPattern {
        private static final ObjectSource source = new ObjectSource();
    }
}
