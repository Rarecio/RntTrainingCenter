package ru.zhaleykin.module2streams;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StreamsTest {

    private Author[] authors;
    private Book[] books;

    @Before
    public void init() {
        authors = new Author[4];
        books = new Book[4];
        authors[0] = new Author("Author 1", (short) 50);
        authors[1] = new Author("Author 2", (short) 27);
        authors[2] = new Author("Author 3", (short) 5);
        authors[3] = new Author("Author 4", (short) 70);

        books[0] = new Book("book 1", 100);
        books[1] = new Book("book 2", 50);
        books[2] = new Book("book 3", 400);
        books[3] = new Book("book 4", 50);

        authors[0].books().addAll(Arrays.asList(books[0], books[1]));
        authors[1].books().add(books[2]);
        authors[2].books().add(books[3]);
        authors[3].books().addAll(Arrays.asList(books[0], books[2]));

        books[0].authors().addAll(Arrays.asList(authors[0], authors[3]));
        books[1].authors().add(authors[0]);
        books[2].authors().addAll(Arrays.asList(authors[1], authors[3]));
        books[3].authors().add(authors[2]);
    }

    @Test
    public void doesAnyBookContainMoreThan200Pages() {
        boolean a = Arrays.stream(books)
                .anyMatch(book -> book.numberOfPages() > 200);
        assertTrue(a);
    }

    @Test
    public void bookWithMaxNumberOfPages() {
        Optional<Book> book = Arrays.stream(books)
                .max(Comparator.comparing(Book::numberOfPages));
        assertTrue(book.isPresent());
        assertEquals(book.get().numberOfPages(), 400);
    }

    @Test
    public void bookWithMinNumberOfPages() {
        Optional<Book> book = Arrays.stream(books)
                .min(Comparator.comparing(Book::numberOfPages));
        assertTrue(book.isPresent());
        assertEquals(book.get().numberOfPages(), 50);
    }

    @Test
    public void booksWithOnlyOneAuthor() {
        List<Book> result = Arrays.stream(books)
                .filter(book -> book.authors().size() == 1)
                .collect(Collectors.toList());
        for (Book book : result) {
            assertEquals(1, book.authors().size());
        }
    }

    @Test
    public void booksSortedByNumberOfPagesAndTitle() {
        List<Book> result = Arrays.stream(books)
                .sorted(Comparator
                        .comparingInt(Book::numberOfPages)
                        .thenComparing(Book::title))
                .collect(Collectors.toList());
        for (int i = 1; i < result.size(); i++) {
            if (result.get(i - 1).numberOfPages() < result.get(i).numberOfPages()){
                assert true;
            } else if (result.get(i - 1).numberOfPages() == result.get(i).numberOfPages()) {
                assertTrue(result.get(i - 1).title().compareTo(result.get(i).title()) <= 0);
            }
        }
    }

    @Test
    public void allTitles(){
        List<String> titles = Arrays.stream(books)
                .map(Book::title)
                .collect(Collectors.toList());

        assertEquals(titles, Arrays.asList("book 1", "book 2", "book 3", "book 4"));
    }

    @Test
    public void allTitlesPrint(){
        Arrays.stream(books)
                .peek(System.out::println)
                .map(Book::title)
                .forEach(System.out::println);
    }

    @Test
    public void allAuthors(){
        List<Author> authorList = Arrays.stream(books)
                .flatMap(book -> book.authors().stream())
                .distinct()
                .collect(Collectors.toList());
        for (Author author : authors) {
            assertTrue(authorList.contains(author));
        }
    }

    @Test
    public void titleOfTheBookWithTheMostPagesForEachAuthor(){
        Map<Author, Optional<Book>> result = Arrays.stream(authors)
                .collect(Collectors.toMap(
                        author -> author,
                        author -> author.books()
                                .stream()
                                .max(Comparator.comparing(Book::numberOfPages))));

        assertTrue(result.get(authors[0]).isPresent());
        assertEquals(result.get(authors[0]).get(), books[0]);

        assertTrue(result.get(authors[1]).isPresent());
        assertEquals(result.get(authors[1]).get(), books[2]);

        assertTrue(result.get(authors[2]).isPresent());
        assertEquals(result.get(authors[2]).get(), books[3]);

        assertTrue(result.get(authors[3]).isPresent());
        assertEquals(result.get(authors[3]).get(), books[2]);

    }

    @Test
    public void collectorTest(){
        List<SomeEntity> list = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 10; i++)
            list.add(getRandomSomeEntity(() -> {
                Map<Property, Boolean> props = new HashMap<>();
                int len = r.nextInt(10);
                for (int k = 0; k < len; k++) {
                    props.put(Property.values()[r.nextInt(Property.values().length)], r.nextBoolean());
                }
                return props;
            }));

        Map<Property, Map<Boolean, Integer>> result = list.stream()
                .collect(new CustomCollector());

        for (Property property : Property.values()) {
            int t=0, f=0;
            for (SomeEntity someEntity : list) {
                if (!someEntity.properties().containsKey(property)) continue;
                if (someEntity.properties().get(property)) t++;
                else f++;
            }
            Map<Boolean, Integer> res = result.get(property);
            assertEquals(res.getOrDefault(true, 0).intValue(), t);
            assertEquals(res.getOrDefault(false, 0).intValue(), f);

        }
    }

    @Test
    public void collectorTestWithEmptyMap(){
        List<SomeEntity> list = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 10; i++)
            list.add(getRandomSomeEntity(Collections::emptyMap));

        Map<Property, Map<Boolean, Integer>> result = list.stream()
                .collect(new CustomCollector());
        for (Property property : Property.values()) {
            int t=0, f=0;
            for (SomeEntity someEntity : list) {
                if (!someEntity.properties().containsKey(property)) continue;
                if (someEntity.properties().get(property)) t++;
                else f++;
            }
            Map<Boolean, Integer> res = result.getOrDefault(property, Collections.emptyMap());
            assertEquals(res.getOrDefault(true, 0).intValue(), t);
            assertEquals(res.getOrDefault(false, 0).intValue(), f);
        }
    }

    private static SomeEntity getRandomSomeEntity(Supplier<Map<Property, Boolean>> supplier) {
        Random r = new Random();
        return new SomeEntity(r.nextInt(), "qwe", r.nextBoolean(), supplier.get());
    }


}
