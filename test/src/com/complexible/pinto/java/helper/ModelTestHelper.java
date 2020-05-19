package com.complexible.pinto.java.helper;

import org.openrdf.model.Model;

import java.util.Arrays;
import java.util.List;

public class ModelTestHelper {
    public static String getTag(Model model) {
        List<String> split = (Arrays.asList(model.predicates().toArray()[0].toString().split(":")));
        if (split.contains("name")) {
            return split.get(split.indexOf("name"));
        }
        return "";

    }
    public static String getName(Model model) {
        return model.objectValue().get().stringValue();
    }
}
