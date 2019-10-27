package graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.types.Data;
import graphql.types.Employer;
import graphql.types.User;

import java.util.ArrayList;
import java.util.List;

public class Query implements GraphQLQueryResolver {

    public Query() {
    }

    public User getUserById(Integer id) {
        for (User user: Data.users) {
            if (user.id.intValue() == id) {
                return user;
            }
        }
        return null;
    }

    public List<User> listUser() {
        return Data.users;
    }

    public List<Employer> employers(String id) {
        List<Employer> employers = new ArrayList<>();
        List<Employer> rtnEmployers = new ArrayList<>();
        Employer temp = new Employer();
        temp.id = "1";
        temp.name = "SpronQ";
        temp.mnEmployersNumber = "1234";
        employers.add(temp);
        temp = new Employer();
        temp.id = "2";
        temp.name = "MN";
        temp.mnEmployersNumber = "5678";
        employers.add(temp);
        for (Employer employer: employers){
            if (employer.id.equals(id)) {
                rtnEmployers.add(employer);
            }
        }
        return rtnEmployers;
    }
}