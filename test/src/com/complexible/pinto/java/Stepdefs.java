package com.complexible.pinto.java;

import com.complexible.common.openrdf.model.ModelIO;
import com.complexible.pinto.RDFMapper;
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
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class Stepdefs {
    private String name;
    private Model model;
    private Person person;

    @Given("I use the Person class")
    public void use_person_class() {
        // Ez most nem baj, hogy nem csinál semmit, majd factory-t kéne rá alkalmazni
    }

    @Given("the name of a person is {string}")
    public void name_of_person_is(String name) {
        this.name = name;
    }

    @When("I serialize the person")
    public void i_serialize_the_person() {
        this.model = RDFMapper.create().writeValue((new Person(name)));
    }

    @Then("I should see the {string} name in a {string} tag")
    public void i_should_see(String expectedName, String expectedAttribute) {
        String actualTag = ModelTestHelper.getTag(model);
        String actualName = ModelTestHelper.getName(model);
        assertEquals(expectedName, actualName);
        assertEquals(expectedAttribute, actualTag);
    }

    @Given("I have an NTriple of a Person object in {string}")
    public void iHaveAnNTripleOfAPersonObject(String filePath) throws URISyntaxException, IOException {
        model = ModelIO.read(new File(getClass().getResource(filePath).toURI()).toPath());
    }

    @When("I deserialize the object")
    public void iDeserializeTheObject() {
        person = RDFMapper.create().readValue(model, Person.class);
        //System.out.println(person.getName());
    }

    @Then("I should get an object of type {string}")
    public void iShouldGetAnObjectOfType(String expectedClassType) {
        assertEquals(expectedClassType, person.getClass().getSimpleName());
    }

    @And("I should see the {string} is {string}")
    public void iShouldSeeTheIs(String expectedAttribute, String expectedValue) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        Field field = person.getClass().getDeclaredField("name");
        field.setAccessible(true);
        //Object value = field.get(person);
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
}