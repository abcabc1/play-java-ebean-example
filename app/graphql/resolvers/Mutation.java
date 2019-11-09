package graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import graphql.types.Book;
import graphql.types.Data;
import graphql.types.Author;

import java.util.List;

public class Mutation implements GraphQLMutationResolver {

    public Mutation() {
    }

    public Boolean saveAuthors(List<Author> authors) {
        Data.authors.addAll(authors);
        return true;
    }

    public Boolean saveAuthor(Author author) {
        Data.authors.add(author);
        return true;
    }

    public Boolean deleteAuthor(Integer id) {
        for (Author author : Data.authors) {
            if (author.id.intValue() == id) {
                Data.authors.remove(author);
                return true;
            }
        }
        return false;
    }

    public Boolean updateAuthor(Author author) {
        for (Author temp : Data.authors) {
            if (temp.id.intValue() == author.id) {
                temp.age = author.age;
                temp.name = author.name;
                temp.password = author.password;
                return true;
            }
        }
        return false;
    }

    public Boolean saveBook(Book book, Integer authorId) {
        Query query = new Query();
        book.author = query.getAuthorById(authorId);
        Data.books.add(book);
        return true;
    }

    public Boolean deleteBook(Integer id) {
        for (Book book : Data.books) {
            if (book.id.intValue() == id) {
                Data.books.remove(book);
                return true;
            }
        }
        return false;
    }

    public Boolean updateBook(Book book) {
        for (Book temp : Data.books) {
            if (temp.id.intValue() == book.id) {
                temp.publishTime = book.publishTime;
                temp.name = book.name;
                temp.author = book.author;
                return true;
            }
        }
        return false;
    }
}
