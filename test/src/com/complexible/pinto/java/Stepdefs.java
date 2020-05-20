package com.complexible.pinto.java;

import com.complexible.common.openrdf.model.ModelIO;
import com.complexible.pinto.RDFMapper;
import com.complexible.pinto.java.beans.Dog;
import com.complexible.pinto.java.beans.Person;
import com.complexible.pinto.java.helper.ModelTestHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openrdf.model.Model;
import org.openrdf.rio.RDFFormat;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

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
    public void iShouldSeeTheIs(String expectedAttribute, String expectedValue) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        Field field = testClass.getClass().getDeclaredField(expectedAttribute);
        field.setAccessible(true);
        String str = field.getName();

        Method method = testClass.getClass().getMethod("get" + str.substring(0, 1).toUpperCase() + str.substring(1));
        System.out.println(method.getName());
        assertEquals(expectedAttribute, field.getName());
        //assertEquals(expectedValue, method.invoke(testClass));

        try {
            assertEquals(expectedValue, method.invoke(testClass));
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

    @When("I serialize the {string}")
    public void iSerializeTheObject(String objectName) {
        this.model = RDFMapper.create().writeValue(testClass);
    }

}