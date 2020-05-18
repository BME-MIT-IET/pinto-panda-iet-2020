package com.complexible.pinto;

import com.complexible.common.openrdf.model.ModelIO;
import com.complexible.common.openrdf.vocabulary.FOAF;
import com.complexible.pinto.impl.IdentifiableImpl;
import com.google.common.collect.Maps;
import org.junit.Rule;
import org.junit.Test;
import org.openrdf.model.Model;
import org.openrdf.model.Resource;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.model.util.Models;

import java.io.File;
import java.net.URI;
import java.sql.Time;
import java.util.*;

import static org.junit.Assert.*;

public class RDFMapperTestsIET {

    @Rule
    public ConditionalIgnoreRule rule = new ConditionalIgnoreRule();

    public class NotCorrectTimeZone implements ConditionalIgnoreRule.IgnoreCondition {
        public boolean isSatisfied() {
            Calendar now = Calendar.getInstance();
            TimeZone timeZone = now.getTimeZone();
            return !(timeZone.getRawOffset() == (60*60*1000));
        }
    }


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

        Model aExpected = ModelIO.read(new File(getClass().getResource("/data/extended_primitives.nt").toURI()).toPath());

        assertEquals(aExpected, aGraph);
        assertTrue(Models.isomorphic(aGraph, aExpected));
    }

    @Test
    public void testReadPrimitivesExtended() throws Exception {
        Model aGraph = ModelIO.read(new File(getClass().getResource("/data/extended_primitives.nt").toURI()).toPath());

        RDFMapper aMapper = RDFMapper.create();

        final ClassWithMissedPrimitives aResult = aMapper.readValue(aGraph, ClassWithMissedPrimitives.class);

        ClassWithMissedPrimitives aExpected = new ClassWithMissedPrimitives();
        aExpected.setString("str value");
        aExpected.setInt(8);
        aExpected.setURI(java.net.URI.create("urn:any"));
        aExpected.setFloat(4.5f);
        aExpected.setDouble(20.22);
        aExpected.setChar('o');
        aExpected.setShort((short)21);
        aExpected.setByte((byte)32);

        assertEquals(aExpected, aResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWritePrimitivesInvalidUri() throws Exception {
        RDFMapper aMapper = RDFMapper.create();

        ClassWithMissedPrimitives aObj = new ClassWithMissedPrimitives();
        aObj.setString("str value");
        aObj.setInt(8);
        aObj.setURI(java.net.URI.create("invaliduri/%():"));
        aObj.setFloat(4.5f);
        aObj.setDouble(20.22);
        aObj.setChar('o');
        aObj.setShort((short)21);
        aObj.setByte((byte)32);
        aObj.id(SimpleValueFactory.getInstance().createIRI("tag:complexible:pinto:3d1c9ece37c3f9ee6068440cf9a383cc"));

        Model aGraph = aMapper.writeValue(aObj);
    }

    @Test
    @ConditionalIgnoreRule.ConditionalIgnore(condition = NotCorrectTimeZone.class)
    public void testWriteTime() throws Exception{


        final RDFMapperTests.ClassWithMap aObj = new RDFMapperTests.ClassWithMap();

        aObj.mMap = Maps.newLinkedHashMap();

        aObj.mMap.put(1L, "the size of something");
        aObj.mMap.put(new Time(5, 45, 26), 57.4);

        final Model aGraph = RDFMapper.builder()
                .build()
                .writeValue(aObj);

        final Model aExpected = ModelIO.read(RDFMapperTests.Files3.classPath("/data/map_with_time.nt").toPath());

        assertTrue(Models.isomorphic(aGraph, aExpected));
    }

    @Test
    @ConditionalIgnoreRule.ConditionalIgnore(condition = NotCorrectTimeZone.class)
    public void testReadTime() throws Exception{
        final RDFMapperTests.ClassWithMap aExpected = new RDFMapperTests.ClassWithMap();

        aExpected.mMap = Maps.newLinkedHashMap();

        aExpected.mMap.put(1L, "the size of something");
        aExpected.mMap.put(new Time(5, 45, 26), 57.4);

        final Model aGraph = ModelIO.read(RDFMapperTests.Files3.classPath("/data/map_with_time.nt").toPath());

        assertEquals(aExpected, RDFMapper.builder()
                .build()
                .readValue(aGraph, RDFMapperTests.ClassWithMap.class,
                        SimpleValueFactory.getInstance().createIRI("tag:complexible:pinto:537dd244e15d548ae9ed2e07aab97a9d")));
    }








    //////////////////////////////////////////////////////////////////////////////////////////////////

    static public class ClassWithMissedPrimitives implements Identifiable {
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

    public static final class ClassWithMap {
        private Map<Object, Object> mMap;

        public Map<Object, Object> getMap() {
            return mMap;
        }

        public void setMap(final Map<Object, Object> theMap) {
            mMap = theMap;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(mMap);
        }

        @Override
        public boolean equals(final Object theObj) {
            if (theObj == this) {
                return true;
            }
            else if (theObj instanceof RDFMapperTests.ClassWithMap) {
                return Objects.equals(mMap, ((RDFMapperTests.ClassWithMap) theObj).mMap);
            }
            else {
                return false;
            }
        }
    }


}
