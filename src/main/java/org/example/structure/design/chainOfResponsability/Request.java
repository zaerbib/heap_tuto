package org.example.structure.design.chainOfResponsability;

import java.util.Objects;

public class Request {
    private final RequestType requestType;
    private final String requestDescription;
    private boolean handled;

    public Request(RequestType requestType, String requestDescription) {
        this.requestType = Objects.requireNonNull(requestType);
        this.requestDescription = Objects.requireNonNull(requestDescription);
    }

    public String getRequestDescription() {
        return requestDescription;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public boolean isHandled() {
        return handled;
    }

    public void markHandled() {
        this.handled = true;
    }
}
