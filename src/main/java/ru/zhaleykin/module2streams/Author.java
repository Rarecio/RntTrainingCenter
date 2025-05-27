package ru.zhaleykin.module2streams;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Author {
    private final String name;
    private final short age;
    private final List<Book> books = new ArrayList<>();

    public Author(String name, short age) {
        this.name = name;
        this.age = age;
    }

    public String name() {
        return name;
    }

    public short age() {
        return age;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public List<Book> books() {
        return books;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", books=" + books.stream().map(Book::title).collect(Collectors.toList()) +
                '}';
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Author)) return false;

        Author author = (Author) object;
        return age == author.age && Objects.equals(name, author.name);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + age;
        return result;
    }
}
