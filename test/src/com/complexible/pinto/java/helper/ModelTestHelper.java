package com.complexible.pinto.java.helper;

import org.openrdf.model.Model;

import java.util.Arrays;
import java.util.List;

public class ModelTestHelper {
    public static String getAttribute(Model model, String attributeName) {
        List<String> split = (Arrays.asList(model.predicates().toArray()[0].toString().split(":")));
        if (split.contains(attributeName)) {
            return split.get(split.indexOf(attributeName));
        }
        return "";

    }
    public static String getValue(Model model) {
        return model.objectValue().get().stringValue();
    }

}
