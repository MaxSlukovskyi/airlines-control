package com.slukovskyi.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {
    private final static String propertiesFileName = "project.properties";

    public static String getProperty(String propertyName) throws IOException {
        Properties properties = new Properties();
        properties.load(PropertiesReader.class.getClassLoader().getResourceAsStream(propertiesFileName));
        return properties.getProperty(propertyName);
    }
}
