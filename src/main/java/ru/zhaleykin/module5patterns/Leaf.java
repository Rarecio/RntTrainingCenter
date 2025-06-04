package ru.zhaleykin.module5patterns;

import java.util.Collections;
import java.util.List;

public class Leaf implements Component {

    private final int value;

    public Leaf(int value) {
        this.value = value;
    }

    public static Leaf withRandomValue(){
        return new Leaf((int)(Math.random()*100));
    }

    @Override
    public void print() {
        System.out.println("leaf value: " + value);
    }

    @Override
    public void add(Component component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(Component component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Component> getComponents() {
        return Collections.emptyList();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
