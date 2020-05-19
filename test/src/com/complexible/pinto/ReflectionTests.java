package com.complexible.pinto;

import com.complexible.common.reflect.Classes;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReflectionTests {

    @Test
    public void checkInterfacesTest() {

        assertTrue(Classes._implements(ClassThatImplements.class, TestInterfaceOne.class));
        assertFalse(Classes._implements(ClassThatImplements.class, TestInterfaceThree.class));

    }

    @Test
    public void getInterfacesTest() {

        int result = 0;
        for (Class item :  Classes.interfaces(ClassThatImplements.class)) {
            result++;
        }
        assertEquals(2, result);
    }


    public interface TestInterfaceOne {}
    public interface TestInterfaceTwo {}
    public interface TestInterfaceThree {}
    static public class ClassThatImplements implements TestInterfaceOne, TestInterfaceTwo {}
}
