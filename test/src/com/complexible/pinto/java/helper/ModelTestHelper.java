package com.complexible.pinto.java.helper;

import io.cucumber.datatable.internal.difflib.StringUtills;
import io.cucumber.java.sl.In;
import org.openrdf.model.Model;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModelTestHelper {
    private static Pattern pattern = Pattern.compile("\"([^\"]*)\"");

    public static String getAttribute(Model model, String attributeName) {
        Object[] attributes = model.predicates().toArray();
        for (Object o: attributes) {
            List<String> split = Arrays.asList(o.toString().split(":"));
            if (split.contains(attributeName)) {
                return split.get(split.indexOf(attributeName));
            }
        }

        return "";

    }
    public static String getValue(Model model, String attributeName) {
        Object[] values = model.objects().toArray();
        Integer index = getIndexOfAttribute(model, attributeName);
        if (index >= 0) {
            return findValue(values[index]);
        }
        return "";
    }

    private static Integer getIndexOfAttribute(Model model, String attributeName) {
        List<Object> attributes = Arrays.asList(model.predicates().toArray());
        for (Object o: attributes) {
            List<String> split = Arrays.asList(o.toString().split(":"));
            if (split.contains(attributeName)) {
                return attributes.indexOf(o);
            }
        }
        return -1;
    }

    private static String findValue(Object o) {
        Matcher matcher = pattern.matcher(o.toString());
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

}
