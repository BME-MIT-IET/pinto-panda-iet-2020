package com.complexible.pinto.java.beans;

import com.complexible.pinto.annotations.RdfId;
import com.complexible.pinto.annotations.RdfProperty;
import com.complexible.pinto.annotations.RdfsClass;

import java.util.Objects;

@RdfsClass("urn:Tankcica")
public class AnnotatedCat {
    private String name;
    private Integer weight;

    public AnnotatedCat() {}

    public AnnotatedCat(String name, Integer weight) {
        this.name = name;
        this.weight = weight;
    }

    @RdfId
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @RdfProperty("urn:extraLove")
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(final Object theObj) {
        if (theObj == this) {
            return true;
        }
        else if (theObj instanceof AnnotatedCat) {
            return Objects.equals(name, ((AnnotatedCat) theObj).name);
        }
        else {
            return false;
        }
    }
}
