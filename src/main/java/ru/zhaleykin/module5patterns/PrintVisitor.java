package ru.zhaleykin.module5patterns;

public class PrintVisitor implements Visitor {
    @Override
    public void visit(Leaf leaf){
        System.out.println("i found leaf");
    }

    @Override
    public void visit(Branch branch){
        System.out.println("i found branch");
    }
}
