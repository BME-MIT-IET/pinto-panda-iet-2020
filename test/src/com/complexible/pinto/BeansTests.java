package com.complexible.pinto;

import com.complexible.common.beans.Beans;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.Assert.*;

public class BeansTests {

    @Test
    public void testIsPrimitives() {
        assertTrue(Beans.isPrimitive(Character.class));
        assertTrue(Beans.isPrimitive(String.class));
        assertTrue(Beans.isPrimitive(Boolean.class));
        assertTrue(Beans.isPrimitive(Integer.class));
        assertTrue(Beans.isPrimitive(Long.class));
        assertTrue(Beans.isPrimitive(Short.class));
        assertTrue(Beans.isPrimitive(Float.class));
        assertTrue(Beans.isPrimitive(Double.class));
        assertTrue(Beans.isPrimitive(java.net.URI.class));

        //byte was missing causing write error
        assertTrue(Beans.isPrimitive(Byte.class));

        //java sql date not primitive
        assertTrue(Beans.isPrimitive(java.util.Date.class));
        assertFalse(Beans.isPrimitive(java.sql.Date.class));
    }

    @Test
    public void TestGetDeclaredFields() {
        Iterable<Field> result = Beans.getDeclaredFields(ClassWithTestFields.class);
        ArrayList<String> actual = new ArrayList<String>();
        for (Field item : result) {
            if (!item.toString().contains("transient"))
                actual.add(item.getName());
        }

        ArrayList<String> expected = new ArrayList<String>();
        expected.add("stringField");
        expected.add("integerField");

        assertEquals(expected, actual);

    }


    ////////////////////////////////////////////////////////////

    static public class ClassWithTestFields {
        private String stringField;
        private int integerField;
    }
}
