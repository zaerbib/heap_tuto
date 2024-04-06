package org.example.structure.design.chainOfResponsability;

import java.util.logging.Logger;

public class OrcCommander implements RequestHandler {
    private Logger log = Logger.getLogger(RequestHandler.class.getName());
    @Override
    public boolean canHandleRequest(Request request) {
        return request.getRequestType() == RequestType.DEFEND_CASTLE;
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public void handle(Request request) {
        request.markHandled();
        log.info(name()+" handling request "+request.getRequestDescription());
    }

    @Override
    public String name() {
        return "Orc commander";
    }
}
