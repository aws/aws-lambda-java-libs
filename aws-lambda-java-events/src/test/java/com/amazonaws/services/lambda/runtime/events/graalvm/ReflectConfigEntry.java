package com.amazonaws.services.lambda.runtime.events.graalvm;

public class ReflectConfigEntry {

    private String name;
    private boolean allDeclaredFields;
    private boolean allDeclaredMethods;
    private boolean allDeclaredConstructors;

    public ReflectConfigEntry() {
    }

    public ReflectConfigEntry(String name, boolean allDeclaredFields, boolean allDeclaredMethods, boolean allDeclaredConstructors) {
        this.name = name;
        this.allDeclaredFields = allDeclaredFields;
        this.allDeclaredMethods = allDeclaredMethods;
        this.allDeclaredConstructors = allDeclaredConstructors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAllDeclaredFields() {
        return allDeclaredFields;
    }

    public void setAllDeclaredFields(boolean allDeclaredFields) {
        this.allDeclaredFields = allDeclaredFields;
    }

    public boolean isAllDeclaredMethods() {
        return allDeclaredMethods;
    }

    public void setAllDeclaredMethods(boolean allDeclaredMethods) {
        this.allDeclaredMethods = allDeclaredMethods;
    }

    public boolean isAllDeclaredConstructors() {
        return allDeclaredConstructors;
    }

    public void setAllDeclaredConstructors(boolean allDeclaredConstructors) {
        this.allDeclaredConstructors = allDeclaredConstructors;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ReflectConfigEntry{");
        sb.append("name='").append(name).append('\'');
        sb.append(", allDeclaredFields=").append(allDeclaredFields);
        sb.append(", allDeclaredMethods=").append(allDeclaredMethods);
        sb.append(", allDeclaredConstructors=").append(allDeclaredConstructors);
        sb.append('}');
        return sb.toString();
    }

    public static ReflectConfigEntry allTrue(String name) {
        return new ReflectConfigEntry(name, true, true, true);
    }
}