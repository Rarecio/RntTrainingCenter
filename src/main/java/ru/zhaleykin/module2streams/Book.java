package ru.zhaleykin.module2streams;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Book {
    private final String title;
    private final int numberOfPages;
    private final List<Author> authors = new ArrayList<>();

    public Book(String title, int numberOfPages) {
        this.title = title;
        this.numberOfPages = numberOfPages;
    }

    public String title() {
        return title;
    }

    public int numberOfPages() {
        return numberOfPages;
    }

    public List<Author> authors() {
        return authors;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", numberOfPages=" + numberOfPages +
                ", authors=" + authors.stream().map(Author::name).collect(Collectors.toList()) +
                '}';
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Book)) return false;

        Book book = (Book) object;
        return numberOfPages == book.numberOfPages && Objects.equals(title, book.title);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(title);
        result = 31 * result + numberOfPages;
        return result;
    }
}
