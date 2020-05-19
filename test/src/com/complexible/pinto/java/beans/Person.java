package com.complexible.pinto.java.beans;

import java.util.Objects;

public final class Person {
    private String name;

    public Person() {
    }

    public Person(final String theName) {
        name = theName;
    }

    public String getName() {
        return name;
    }

    public void setName(final String theName) {
        name = theName;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(final Object theObj) {
        if (theObj == this) {
            return true;
        }
        else if (theObj instanceof Person) {
            return Objects.equals(name, ((Person) theObj).name);
        }
        else {
            return false;
        }
    }
}