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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class Stepdefs {
    private Model model;
    private Person person;
    private Object testClass;

    @Then("I should see the {string} in a {string} tag")
    public void iShouldSeeTheAttributeInATag(String expectedBreed, String expectedAttribute) {
        String actualTag = ModelTestHelper.getAttribute(model, expectedAttribute);
        String actualName = ModelTestHelper.getValue(model);
        assertEquals(expectedBreed, actualName);
        assertEquals(expectedAttribute, actualTag);
    }

    @Given("I have an NTriple of a Person object in {string}")
    public void iHaveAnNTripleOfAPersonObject(String filePath) throws URISyntaxException, IOException {
        model = ModelIO.read(new File(getClass().getResource(filePath).toURI()).toPath());
    }

    @When("I deserialize the object")
    public void iDeserializeTheObject() {
        person = RDFMapper.create().readValue(model, Person.class);
    }

    @Then("I should get an object of type {string}")
    public void iShouldGetAnObjectOfType(String expectedClassType) {
        assertEquals(expectedClassType, person.getClass().getSimpleName());
    }

    @And("I should see the {string} is {string}")
    public void iShouldSeeTheIs(String expectedAttribute, String expectedValue) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        Field field = person.getClass().getDeclaredField("name");
        field.setAccessible(true);
        String str = field.getName();

        Method method = person.getClass().getMethod("get" + str.substring(0, 1).toUpperCase() + str.substring(1));
        System.out.println(method.getName());
        assertEquals(expectedAttribute, field.getName());
        assertEquals(expectedValue, person.getName());

        try {
            assertEquals(expectedValue, method.invoke(person));
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

    @And("the {string} of the {string} is {string}")
    public void theAttributeOfTheObjectIs(String attribute, String objectName, String value) {
        try {
            Method method = testClass.getClass().getDeclaredMethod("set" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1), String.class);
            method.invoke(testClass, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @When("I serialize the {string}")
    public void iSerializeTheObject(String objectName) {
        this.model = RDFMapper.create().writeValue(testClass);
    }

}