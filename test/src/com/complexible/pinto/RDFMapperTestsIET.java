package com.complexible.pinto;

import com.complexible.common.openrdf.model.ModelIO;
import com.complexible.pinto.impl.IdentifiableImpl;
import org.junit.Test;
import org.openrdf.model.Model;
import org.openrdf.model.Resource;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.model.util.Models;

import java.io.File;
import java.net.URI;
import java.util.Objects;

import static org.junit.Assert.*;

public class RDFMapperTestsIET {

    @Test
    public void testRDFMapper_newInstance_noConstructor() {
        RDFMapperTests.Person p = new RDFMapperTests.Person();
        p.setName("John Doe");
        Model x = RDFMapper.create().writeValue(p);

        final RDFMapperTests.Person resultP = RDFMapper.create().readValue(x, null, null);

        assertNull(resultP);
    }

    @Test
    public void testWritePrimitivesExtended() throws Exception {
        RDFMapper aMapper = RDFMapper.create();

        ClassWithMissedPrimitives aObj = new ClassWithMissedPrimitives();
        aObj.setString("str value");
        aObj.setInt(8);
        aObj.setURI(java.net.URI.create("urn:any"));
        aObj.setFloat(4.5f);
        aObj.setDouble(20.22);
        aObj.setChar('o');
        aObj.setShort((short)21);
        aObj.setByte((byte)32);
        aObj.id(SimpleValueFactory.getInstance().createIRI("tag:complexible:pinto:3d1c9ece37c3f9ee6068440cf9a383cc"));

        Model aGraph = aMapper.writeValue(aObj);
        System.out.println(aGraph);

        Model aExpected = ModelIO.read(new File(getClass().getResource("/data/extended_primitives.nt").toURI()).toPath());
        System.out.println(aExpected);

        assertEquals(aExpected, aGraph);
        assertTrue(Models.isomorphic(aGraph, aExpected));
    }


    public class ClassWithMissedPrimitives implements Identifiable {
        private String mString;
        private int mInt;
        private float mFloat;
        private double mDouble;
        private char mChar;

        private short mShort;
        private byte mByte;
        private java.net.URI mURI;

        private Identifiable mIdentifiable = new IdentifiableImpl();

        @Override
        public Resource id() {
            return mIdentifiable.id();
        }

        @Override
        public void id(final Resource theResource) {
            mIdentifiable.id(theResource);
        }

        @Override
        public boolean equals(final Object theObj) {
            if (this == theObj) {
                return true;
            }
            if (theObj == null || getClass() != theObj.getClass()) {
                return false;
            }

            ClassWithMissedPrimitives that = (ClassWithMissedPrimitives) theObj;

            return mChar == that.mChar
                    && Double.compare(mDouble, that.mDouble) == 0
                    && Float.compare(mFloat, that.mFloat) == 0
                    && mInt == that.mInt
                    && Objects.equals(mString, that.mString)
                    && Objects.equals(mURI, that.mURI)
                    && mByte == that.mByte
                    && mShort == that.mShort;
        }

        @Override
        public int hashCode() {
            return Objects.hash(mChar, mDouble, mFloat, mInt, mString, mURI);
        }

        public char getChar() {
            return mChar;
        }

        public void setChar(final char theChar) {
            mChar = theChar;
        }

        public double getDouble() {
            return mDouble;
        }

        public void setDouble(final double theDouble) {
            mDouble = theDouble;
        }

        public float getFloat() {
            return mFloat;
        }

        public void setFloat(final float theFloat) {
            mFloat = theFloat;
        }

        public int getInt() {
            return mInt;
        }

        public void setInt(final int theInt) {
            mInt = theInt;
        }

        public String getString() {
            return mString;
        }

        public void setString(final String theString) {
            mString = theString;
        }

        public URI getURI() {
            return mURI;
        }

        public void setURI(final URI theURI) {
            mURI = theURI;
        }

        public byte getByte() {return mByte;}
        public void setByte(final byte theByte) {mByte = theByte;}

        public short getShort() {return mShort;}
        public void setShort(final short theShort) {mShort = theShort;}
    }
}
