package org.example.design.chainOf;

import org.example.structure.design.chainOfResponsability.OrcKing;
import org.example.structure.design.chainOfResponsability.Request;
import org.example.structure.design.chainOfResponsability.RequestType;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class ChainOfTest {

    private static final List<Request> REQUESTS = List.of(
            new Request(RequestType.DEFEND_CASTLE,
                    "Don't let the barbarians enter my castle"),
            new Request(RequestType.TORTURE_PRISONER,
                    "Don't just stand there, tickle him !"),
            new Request(RequestType.COLLECT_TAX,
                    "Don't steal, the King hates competition ...")
    );

    @Test
    public void testMarkeRequest() {
        final var king = new OrcKing();
        REQUESTS.forEach(request -> {
            king.makeRequest(request);
            assertTrue(
                    "Expected all requests from King to be handled, but [" + request + "] was not!",
                    request.isHandled()
            );
        });
    }
}
