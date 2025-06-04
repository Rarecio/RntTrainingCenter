package ru.zhaleykin.module5patterns;

import java.util.*;

public class Graph implements Iterable<Component>{

    private final Component root;
    private IteratorType iteratorType = IteratorType.DFS;

    public Graph(Component root) {
        this.root = root;
    }

    public void setIteratorType(IteratorType iteratorType) {
        this.iteratorType = iteratorType;
    }

    @Override
    public Iterator<Component> iterator() {
        return iteratorType == IteratorType.DFS ? new DFSIterator() : new BFSIterator();
    }

    public enum IteratorType {
        DFS, BFS
    }

    private class DFSIterator implements Iterator<Component> {

        private final Deque<Component> stack = new ArrayDeque<>();

        public DFSIterator() {
            stack.push(root);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public Component next() {
            Component component = stack.pop();
            component.getComponents().forEach(stack::push);
            return component;
        }
    }

    private class BFSIterator implements Iterator<Component> {

        private final Queue<Component> queue = new ArrayDeque<>();

        public BFSIterator() {
            queue.offer(root);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public Component next() {
            Component component = queue.poll();
            if (component == null)
                throw new NoSuchElementException();
            queue.addAll(component.getComponents());
            return component;
        }
    }
}
