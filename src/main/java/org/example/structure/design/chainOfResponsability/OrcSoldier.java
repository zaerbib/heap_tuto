package org.example.structure.design.chainOfResponsability;

import java.util.logging.Logger;

public class OrcSoldier implements RequestHandler {
    private Logger log = Logger.getLogger(RequestHandler.class.getName());

    @Override
    public boolean canHandleRequest(Request request) {
        return request.getRequestType() == RequestType.COLLECT_TAX;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public void handle(Request request) {
        request.markHandled();
        log.info(name()+" handling request "+request.getRequestDescription());
    }

    @Override
    public String name() {
        return "Orc Soldier";
    }
}
