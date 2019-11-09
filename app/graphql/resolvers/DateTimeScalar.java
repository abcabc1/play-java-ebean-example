package graphql.resolvers;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTimeScalar extends GraphQLScalarType {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DateTimeScalar() {

        super("DateTime", "Built-in DateTime as timestamp", new Coercing() {

            //接收一个Java对象，将它转换为scalar中的output表示形式
            @Override
            public String serialize(Object input) {
                if (input instanceof java.util.Date) {
                    return sdf.format(input);
                }
                return null;
            }

            //接收一个input变量，然后在运行时转换为Java中的对象
            @Override
            public java.util.Date parseValue(Object input) {
                if (input instanceof String) {
                    try {
                        return sdf.parse((String) input);
                    } catch (ParseException e) {
                        System.out.println("cast input time with yyyy-MM-dd HH:mm:ss fail " + input.toString());
                    }
                }
                return null;
            }

            //接收一个AST常量（graphql.language.Value类型）作为输入，在运行时转换为Java中的对象
            @Override
            public java.util.Date parseLiteral(Object input) {
                if (input instanceof StringValue) {
                    return parseValue(((StringValue) input).getValue());
                }
                return null;
            }
        });
    }
}