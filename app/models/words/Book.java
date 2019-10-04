package models.words;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Entity;

@Entity
public class Book extends Model {

    public Integer id;
    public String name;
    public Author author;
    public String publisher;

    public static final Finder<Long, Book> find = new Finder<>(Book.class);
}
