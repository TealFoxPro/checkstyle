package org.checkstyle.suppressionxpathfilter.hiddenfield;

public class InputXpathHiddenFieldMethodParam {
    static int someField;
    static Object other = null;
    Object field = null;

    static void method(Object field, Object other) { //warn
    }
}
