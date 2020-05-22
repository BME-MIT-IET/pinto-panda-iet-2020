package com.complexible.pinto.java.helper;

import com.complexible.pinto.annotations.RdfId;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import io.cucumber.datatable.internal.difflib.StringUtills;
import io.cucumber.java.sl.In;
import org.apache.commons.io.Charsets;
import org.openrdf.model.Model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
            List<String> split = Arrays.asList(o.toString().split("[:, -, #]"));
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

    public static String getHash(Model model) {
        Object[] objects = model.subjects().toArray();
        String[] split = objects[0].toString().split(":");
        return split[split.length - 1];
    }

    public static String getExpectedHash(Object testClass) throws NoSuchMethodException {
        Field[] fields = testClass.getClass().getDeclaredFields();
        List<Method> getters = new ArrayList<Method>();

        for (Field field : fields) {
            Method setter = testClass.getClass().getDeclaredMethod("set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), field.getType());
            Method getter = testClass.getClass().getDeclaredMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
            if (setter.getAnnotation(RdfId.class) != null || getter.getAnnotation(RdfId.class) != null) {
                getters.add(testClass.getClass().getMethod(getter.getName()));
            }
        }

        Hasher hasher = Hashing.md5().newHasher();
        for (Method getter : getters) {
            try {
                Object value = getter.invoke(testClass);

                hasher.putString(value.toString(), Charsets.UTF_8);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return hasher.hash().toString();
    }

    public static String getAnnotatedAttribute(Model model, String attributeName) {
        Object[] attributes = model.predicates().toArray();
        for (Object o: attributes) {
            String split = o.toString();
            if (split.contains(attributeName)) {
                return split;
            }
        }

        return "";

    }

    public static String getAnnotatedClass(Model model, String attributeName) {
        Object[] ns = model.objects().toArray();
        Integer index = getIndexOfAttribute(model, attributeName);
        if (index >= 0) {
            return ns[index].toString();
        }
        return "";
    }
}
