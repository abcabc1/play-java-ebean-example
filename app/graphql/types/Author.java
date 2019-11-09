package graphql.types;

import io.ebean.Model;

public class Author extends Model {

    public Integer id;
    public String name;
    public String password;
    public Integer age;
    public Gender gender;

    public enum Gender {

        Male("男"), Female("女"), Unkown("未知");

        private String value;

        private Gender(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}


