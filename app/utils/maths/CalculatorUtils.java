package utils.maths;

import java.util.stream.Stream;

public class CalculatorUtils {

    public static Boolean getBooleanResult(Boolean... booleans) {
        return Stream.of(booleans).reduce(true, (result, v) -> (result == v));
    }
}
