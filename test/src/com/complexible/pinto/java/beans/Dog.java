package com.complexible.pinto.java.beans;

import java.util.Objects;

public final class Dog {
    private String breed;

    public Dog() {}

    public Dog(final String breed) {
        this.breed = breed;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(breed);
    }

    @Override
    public boolean equals(final Object theObj) {
        if (theObj == this) {
            return true;
        }
        else if (theObj instanceof Dog) {
            return Objects.equals(breed, ((Dog) theObj).breed);
        }
        else {
            return false;
        }
    }
}
