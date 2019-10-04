package utils.bsh;

import bsh.EvalError;
import bsh.Interpreter;
import models.iplay.account.Operator;
import models.iplay.account.Role;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

public class Test {

    public void Test() {

    }

    public void test() throws EvalError {
        Interpreter i = new Interpreter();
        i.set("foo", 5);
        i.set("date", new Date());
        Date date = (Date) i.get("date");
        i.set("n", 3);
        i.eval("bar = foo*n");
        System.out.println(date.toString() + " " + i.get("bar"));
    }

    public void testFile() throws EvalError, IOException {
        Interpreter i = new Interpreter();
        i.set("foo", 5);
        i.set("n", 2);
        i.source("app/utils/bsh/test.bsh");
        System.out.println(i.get("bar"));
    }

    public void calculate(Operator operator) throws EvalError, IOException {
        Interpreter i = new Interpreter();
        i.set("operator", operator);
//        String f = "rtn=operator.id.toString()+operator.status.toString()";
//        i.eval(f);
        i.source("app/utils/bsh/calculate.bsh");
        System.out.println(i.get("rtn"));
    }

    public void testNull() {
        Role role = null;
//        role.roleName = "role";
        System.out.println(Optional.ofNullable(role).flatMap(v -> Optional.ofNullable(v.roleName)));
    }

    public static void main(String[] args) throws IOException, EvalError {
        Test test = new Test();
//        Operator operator = new Operator();
//        operator.id = 2L;
//        operator.status = true;
//        test.calculate(operator);
        test.testNull();
    }
}
