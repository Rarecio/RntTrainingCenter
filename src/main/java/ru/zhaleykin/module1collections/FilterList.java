package ru.zhaleykin.module1collections;

import java.util.*;

public class FilterList<E> extends AbstractList<E> implements List<E> {

    private final List<E> objects;
    private final Set<E> predicate;

    public FilterList(Collection<E> objects, Collection<E> predicate) {
        this.objects = new ArrayList<>(objects);
        this.predicate = new HashSet<>(predicate);
    }

    @Override
    public int size() {
        return objects.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return objects.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        return objects.toArray();
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        if (predicate.contains(e)) {
            return false;
        }
        return objects.add(e);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (predicate.contains(o)) {
            return false;
        }
        return objects.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            changed |= add(e);
        }
        return changed;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean changed = false;
        List<E> tmp = new ArrayList<>(c);

        for (int i = c.size() - 1; i >= 0; i--) {
            add(index, tmp.get(i));
            if (predicate.contains(tmp.get(i))) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object e : c) {
            changed |= remove(e);
        }
        return changed;
    }

    @Override
    public void clear() {
        objects.clear();
    }

    @Override
    public E get(int index) {
        if (index > objects.size() || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        return objects.get(index);
    }

    @Override
    public E set(int index, E element) {
        if (index > objects.size() || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (element == null) {
            throw new NullPointerException();
        }
        return objects.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        if (index > objects.size() || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (element == null) {
            throw new NullPointerException();
        }
        if (predicate.contains(element)) {
            return;
        }
        objects.add(index, element);
    }

    @Override
    public E remove(int index) {
        if (index > objects.size() || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        E e = objects.get(index);
        if (!predicate.contains(e)) {
            objects.remove(e);
        }
        return e;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        return objects.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        return objects.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListItr(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return objects.subList(fromIndex, toIndex);
    }

    private class Itr implements Iterator<E> {
        int cursor = 0;
        int lastRet = -1;

        Itr() {
            while (cursor < size() && predicate.contains(objects.get(cursor))) {
                cursor++;
            }
        }

        @Override
        public boolean hasNext() {
            return cursor < size();
        }

        @Override
        public E next() {
            if (cursor >= objects.size()){
                throw new NoSuchElementException();
            }
            lastRet = cursor;
            do {
                cursor++;
            } while (cursor < size() && predicate.contains(objects.get(cursor)));
            return objects.get(lastRet);
        }

        @Override
        public void remove() {
            int tmp = lastRet;
            while (tmp >= 0 && predicate.contains(objects.get(tmp))) {
                tmp--;
            }
            if (tmp < 0) {
                throw new IllegalStateException();
            }
            objects.remove(tmp);
            cursor--;
            lastRet = -1;
        }
    }

    private class ListItr extends Itr implements ListIterator<E> {

        ListItr(int index) {
            super();
            cursor = index;
            while (cursor < size() && predicate.contains(objects.get(cursor))) {
                cursor++;
            }
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public boolean hasPrevious() {
            int tmp = cursor;
            do {
                tmp--;
            } while (tmp >= 0 && predicate.contains(objects.get(tmp)));
            return tmp >= 0;
        }

        @Override
        public E previous() {
            do {
                cursor--;
            } while (cursor >= 0 && predicate.contains(objects.get(cursor)));
            if (cursor < 0){
                throw new NoSuchElementException();
            }
            lastRet = cursor;
            return objects.get(lastRet);
        }

        @Override
        public int previousIndex() {
            int tmp = cursor;
            do {
                tmp--;
            } while (tmp >= 0 && predicate.contains(objects.get(tmp)));
            return tmp;
        }

        @Override
        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            FilterList.this.set(lastRet, e);
        }

        @Override
        public void add(E e) {
            FilterList.this.add(cursor, e);
            lastRet = cursor;
            cursor++;
        }
    }
}
