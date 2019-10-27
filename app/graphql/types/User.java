package graphql.types;

import io.ebean.Model;

public class User extends Model {

    public Long id;
    public String name;
    public String password;
    public Integer age;
}
