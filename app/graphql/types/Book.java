package graphql.types;

import io.ebean.Model;

import java.util.Date;

public class Book extends Model {

    public Integer id;
    public String name;
    public Author author;
    public Date publishTime;
    public Float length;
    public LengthUnit unit;

    public enum LengthUnit {

        Meter("米"), Centimeter("厘米"), Millimetre("毫米");

        private String value;

        private LengthUnit(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}


