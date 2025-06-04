package ru.zhaleykin.module5patterns;

public interface Visitor {
    void visit(Leaf leaf);
    void visit(Branch branch);
}
