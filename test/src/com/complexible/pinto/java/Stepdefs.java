package com.complexible.pinto.java;

import com.complexible.common.openrdf.model.ModelIO;
import com.complexible.pinto.RDFMapper;
import com.complexible.pinto.annotations.RdfId;
import com.complexible.pinto.annotations.RdfProperty;
import com.complexible.pinto.java.beans.Dog;
import com.complexible.pinto.java.beans.Person;
import com.complexible.pinto.java.helper.ModelTestHelper;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.sun.tools.javac.code.TypeMetadata;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.apache.commons.io.Charsets;
import org.openrdf.model.Model;
import org.openrdf.rio.RDFFormat;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.Assert.*;

public class Stepdefs {
    private Model model;
    private String className;
    private Object testClass;

    @Then("I should see the {string} in a {string} tag")
    public void iShouldSeeTheAttributeInATag(String expectedBreed, String expectedAttribute) throws IOException {
        String actualTag = ModelTestHelper.getAttribute(model, expectedAttribute);
        String actualName = ModelTestHelper.getValue(model, expectedAttribute);
        ModelIO.write(model, System.out, RDFFormat.NTRIPLES);
        assertEquals(expectedBreed, actualName);
        assertEquals(expectedAttribute, actualTag);
    }

    @Given("I have an NTriple of a {string} object in {string}")
    public void iHaveAnNTripleOfAClassObject(String className, String filePath) throws URISyntaxException, IOException {
        this.model = ModelIO.read(new File(getClass().getResource(filePath).toURI()).toPath());
        this.className = className;
    }

    @When("I deserialize the object")
    public void iDeserializeTheObject() throws ClassNotFoundException {
        this.testClass = RDFMapper.create().readValue(model, Class.forName("com.complexible.pinto.java.beans." + className));
    }

    @Then("I should get an object of type {string}")
    public void iShouldGetAnObjectOfType(String expectedClassType) {
        assertEquals(expectedClassType, testClass.getClass().getSimpleName());
    }

    @And("I should see the {string} is {string}")
    public void iShouldSeeTheAttributeIs(String expectedAttribute, String expectedValue) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        Field field = testClass.getClass().getDeclaredField(expectedAttribute);
        field.setAccessible(true);
        String str = field.getName();

        Method method = testClass.getClass().getMethod("get" + str.substring(0, 1).toUpperCase() + str.substring(1));

        assertEquals(expectedAttribute, field.getName());

        try {
            assertEquals(expectedValue, method.invoke(testClass).toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Given("I use the {string} class")
    public void iUseTheGivenClass(String creatableClass) {
        try {
            this.testClass = Class.forName("com.complexible.pinto.java.beans." + creatableClass).getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @And("the {string} of the {string} is {string} of type String")
    public void theAttributeOfTheObjectIsOfTypeString(String attribute, String objectName, String value) {
        try {
            Method method = testClass.getClass().getDeclaredMethod("set" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1), String.class);
            method.invoke(testClass, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @And("the {string} of the {string} is {string} of type Integer")
    public void theAttributeOfTheObjectIsOfTypeInteger(String attribute, String objectName, String value) {
        try {
            Method method = testClass.getClass().getDeclaredMethod("set" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1), Integer.class);
            method.invoke(testClass, Integer.parseInt(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @And("the {string} of the {string} is {string} of type Boolean")
    public void theAttributeOfTheObjectIsOfTypeBoolean(String attribute, String objectName, String value) {
        try {
            Method method = testClass.getClass().getDeclaredMethod("set" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1), Boolean.class);
            method.invoke(testClass, Boolean.parseBoolean(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @And("the {string} of the {string} is {string} of type Float")
    public void theAttributeOfTheObjectIsOfTypeFloat(String attribute, String objectName, String value) {
        try {
            Method method = testClass.getClass().getDeclaredMethod("set" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1), Float.class);
            method.invoke(testClass, Float.parseFloat(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @When("I serialize the {string}")
    public void iSerializeTheObject(String objectName) {
        this.model = RDFMapper.create().writeValue(testClass);
        System.out.println(model.toString());
    }

    @And("the {string} of the {string} is annotated with @RdfId")
    public void theAttributeOfTheClassIsAnnotatedWithRdfId(String attribute, String className) {
        //Nem csinál még semmit, de ez nem baj jelenleg
    }

    @Then("I should see the URI of the {string} object is generated only by the annotated properties")
    public void iShouldSeeTheURIOfTheObjectIsGeneratedOnlyByTheAnnotatedProperties(String className) throws NoSuchMethodException {
        String writtenHash = ModelTestHelper.getHash(model);
        String expected = ModelTestHelper.getExpectedHash(testClass);

        assertEquals(expected, writtenHash);
    }

    @And("the {string} of the {string} of the {string} is annotated with @RdfProperty")
    public void theMethodOfTheOfTheObjectIsAnnotatedWithRdfProperty(String methodType, String attribute, String className) {
        //Nem csinál még semmit, de ez nem baj jelenleg
    }


    @And("the URI of the {string} property is {string}")
    public void theURIOfThePropertyIs(String attribute, String attributeURI) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Field field = testClass.getClass().getDeclaredField(attribute);
        Method setter = testClass.getClass().getDeclaredMethod("set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), field.getType());
        Method getter = testClass.getClass().getDeclaredMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
        RdfProperty setterAnnotation = setter.getAnnotation(RdfProperty.class);
        RdfProperty getterAnnotation = getter.getAnnotation(RdfProperty.class);

        if (setterAnnotation == null && getterAnnotation == null) {
            return;
        } else {
            Method method = Class.class.getDeclaredMethod("annotationData", null);
            method.setAccessible(true);
            Object annotationData = method.invoke(RdfProperty.class);
            Field annotations = annotationData.getClass().getDeclaredField("annotations");
            annotations.setAccessible(true);
            Map<Class<? extends Annotation>, Annotation> map =
                    (Map<Class<? extends Annotation>, Annotation>) annotations.get(annotationData);
            Annotation newRdfProp = new RdfProperty() {

                @Override
                public Class<? extends Annotation> annotationType() {
                    return RdfProperty.class;
                }

                @Override
                public String value() {
                    return attributeURI;
                }

                @Override
                public boolean isList() {
                    return false;
                }

                @Override
                public String language() {
                    return "";
                }

                @Override
                public String datatype() {
                    return "";
                }
            };

            if (setterAnnotation != null) {
                map.put(setterAnnotation.getClass(), newRdfProp);
            } else {
                map.put(getterAnnotation.getClass(), newRdfProp);
            }
        }
    }

    @Then("I should see the URI of the {string} property is {string}")
    public void iShouldSeeTheURIOfThePropertyIs(String attribute, String value) {
        String actualTag = ModelTestHelper.getAnnotatedAttribute(model, value);

        assertEquals(value, actualTag);
    }
}