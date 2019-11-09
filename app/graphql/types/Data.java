package graphql.types;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Data {

    public static List<Author> authors = new ArrayList<>();
    public static List<Book> books = new ArrayList<>();

    public static void initBooks() {
        if (!books.isEmpty()) {
            return;
        }
        initAnthors();
        for (int i = 0; i < 3; i++) {
            Book book = new Book();
            book.id = i;
            book.name = "book name " + i;
            book.publishTime = new Date();
            if (i == 0) {
                book.author = authors.get(0);
                book.unit = Book.LengthUnit.Meter;
            } else {
                book.author = authors.get(1);
                book.unit = Book.LengthUnit.Centimeter;
            }
            books.add(book);
        }
    }

    private static void initAnthors() {
        for (int i = 0; i < 2; i++) {
            Author author = new Author();
            author.id = i;
            author.name = "author name " + i;
            author.age = i;
            author.password = "password " + i;
            if (i == 0) {
                author.gender = Author.Gender.Male;
            } else {
                author.gender = Author.Gender.Female;
            }
            authors.add(author);
        }
    }
}
