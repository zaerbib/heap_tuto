package org.example.design.singleton;

import org.junit.Test;

import java.util.function.Supplier;

import static org.junit.Assert.assertSame;


public abstract class SingletonTest<S> {

    private final Supplier<S> singletonInstanceMethod;


    public SingletonTest(Supplier<S> singletonInstanceMethod) {
        this.singletonInstanceMethod = singletonInstanceMethod;
    }

    @Test
    public void testMultipleCallsReturnTheSameObjectInSameThread() {
        // Create several instances in the same calling thread
        var instance1 = this.singletonInstanceMethod.get();
        var instance2 = this.singletonInstanceMethod.get();
        var instance3 = this.singletonInstanceMethod.get();
        // now check they are equal
        assertSame(instance1, instance2);
        assertSame(instance1, instance3);
        assertSame(instance2, instance3);
    }
}
