package models.words;

import io.ebean.Finder;
import io.ebean.Model;

import javax.persistence.Entity;

@Entity
public class Author extends Model {

    public Integer id;
    public String name;
    public Integer age;

    public static final Finder<Long, Author> find = new Finder<>(Author.class);
}
