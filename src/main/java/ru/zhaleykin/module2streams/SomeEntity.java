package ru.zhaleykin.module2streams;

import java.util.Map;

public class SomeEntity {
    private final Map<Property, Boolean> properties;

    public SomeEntity(Map<Property, Boolean> properties) {
        this.properties = properties;
    }

    public Map<Property, Boolean> properties() {
        return properties;
    }
}
