package graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.types.Book;
import graphql.types.Data;
import graphql.types.Author;

import java.util.List;

public class Query implements GraphQLQueryResolver {

    public Query() {
    }

    public Author getAuthorById(Integer id) {
        for (Author author : Data.authors) {
            if (author.id.intValue() == id) {
                return author;
            }
        }
        return null;
    }

    public List<Author> listAuthor() {
        return Data.authors;
    }

    public List<Book> listBook() {
        return Data.books;
    }
}