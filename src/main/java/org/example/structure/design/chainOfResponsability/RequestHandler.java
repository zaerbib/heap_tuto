package org.example.structure.design.chainOfResponsability;

public interface RequestHandler {
    boolean canHandleRequest(Request request);
    int getPriority();
    void handle(Request request);
    String name();
}
