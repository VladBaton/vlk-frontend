package com.bivgroup.utils;

import com.vaadin.flow.server.VaadinSession;

public class SessionUtils {

    public static void saveAttribute(String attributeName, String attributeValue) {
        VaadinSession.getCurrent().setAttribute(attributeName, attributeValue);
    }

    public static String getAttribute(String attributeName) {
        return (String) VaadinSession.getCurrent().getAttribute(attributeName);
    }

    public static void clearAttribute(String attributeName) {
        VaadinSession.getCurrent().setAttribute(attributeName, null);
    }
}
