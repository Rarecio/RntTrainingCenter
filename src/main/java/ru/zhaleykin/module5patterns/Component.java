package ru.zhaleykin.module5patterns;

import java.util.List;

public interface Component {
    void print();
    void add(Component component);
    void remove(Component component);
    List<Component> getComponents();
    void accept(Visitor visitor);
}
