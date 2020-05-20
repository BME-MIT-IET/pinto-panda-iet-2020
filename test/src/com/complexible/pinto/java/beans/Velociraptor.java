package com.complexible.pinto.java.beans;

import java.util.Objects;

public class Velociraptor {
    private Integer velocity;
    private String name;

    public Velociraptor() {}

    public Velociraptor(Integer velocity, String name) {
        this.velocity = velocity;
        this.name = name;
    }


    public Integer getVelocity() {
        return velocity;
    }

    public void setVelocity(Integer velocity) {
        this.velocity = velocity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
            return Objects.equals(name, ((Velociraptor) theObj).name);
        }
        else {
            return false;
        }
    }
}
