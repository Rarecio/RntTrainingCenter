package ru.zhaleykin.module5patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Branch implements Component {

    private final int value;
    private final List<Component> elements;

    public Branch(int value) {
        this(value, new ArrayList<>());
    }

    public Branch(int value, Component... elements) {
        this(value, Arrays.asList(elements));
    }

    public Branch(int value, List<Component> elements) {
        this.value = value;
        this.elements = new ArrayList<>(elements);
    }

    public static Branch withRandomValue(){
        return new Branch((int)(Math.random()*100));
    }

    @Override
    public void add(Component component) {
        elements.add(component);
    }

    @Override
    public void remove(Component component) {
        elements.remove(component);
    }

    @Override
    public List<Component> getComponents() {
        return elements;
    }

    @Override
    public void print() {
        System.out.println("branch value: " + value);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}