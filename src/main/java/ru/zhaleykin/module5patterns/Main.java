package ru.zhaleykin.module5patterns;

public class Main {

    public static void main(String[] args) {
//        Component component = createRandomComponent();
        Component component = createWithSequencedValues();
        Visitor visitor = new PrintVisitor();
        Graph graph = new Graph(component);

        graph.setIteratorType(Graph.IteratorType.DFS);
        graph.forEach(Component::print);
        System.out.println();
        System.out.println();
        graph.setIteratorType(Graph.IteratorType.BFS);
        graph.forEach(Component::print);

        graph.forEach(comp -> comp.accept(visitor));
    }

    static int n = 0;

    static Component createWithSequencedValues(){
        Branch branch = new Branch(n++);
        for (int i = 0; i < 2; i++)
            branch.add(Math.random() < 0.6 ? new Leaf(n++) : createWithSequencedValues());
        return branch;
    }

    static Component createRandomComponent() {
        Branch branch = Branch.withRandomValue();
        for (int i = 0; i < 2; i++)
            branch.add(Math.random() < 0.6 ? Leaf.withRandomValue() : createRandomComponent());
        return branch;
    }
}
