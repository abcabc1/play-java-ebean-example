package graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import graphql.types.Data;
import graphql.types.User;

public class Mutation implements GraphQLMutationResolver {
    public Mutation() {
    }

    public Boolean saveUser(User user){
        Data.users.add(user);
        return true;
    }

    public Boolean deleteUser(Integer id) {
        for (User user : Data.users) {
            if (user.id.intValue() == id) {
                Data.users.remove(user);
                break;
            }
        }
        return true;
    }

    public Boolean updateUser(User user) {
        for (User temp : Data.users) {
            if (temp.id.intValue() == user.id) {
                temp.age = user.age;
                temp.name = user.name;
                temp.password = user.password;
                break;
            }
        }
        return true;
    }
}
