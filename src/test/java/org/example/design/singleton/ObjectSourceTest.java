package org.example.design.singleton;

import org.example.structure.design.singleton.ObjectSource;
import org.example.structure.design.singleton.ObjectSourceGet;

public class ObjectSourceTest extends SingletonTest<ObjectSource>{
    public ObjectSourceTest() {
        super(ObjectSourceGet::getInstance);
    }
}
