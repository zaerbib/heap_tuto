package org.example.structure.design.chainOfResponsability;

import java.util.logging.Logger;

public class OrcOfficer implements RequestHandler {
    private Logger log = Logger.getLogger(RequestHandler.class.getName());

    @Override
    public boolean canHandleRequest(Request request) {
        return request.getRequestType() == RequestType.TORTURE_PRISONER;
    }

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public void handle(Request request) {
        request.markHandled();
        log.info(name()+" handling request "+request.getRequestDescription());
    }

    @Override
    public String name() {
        return "Orc officer";
    }
}
