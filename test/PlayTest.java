import io.jsonwebtoken.Claims;
import org.junit.Assert;
import org.junit.Test;
import utils.jwt.JwtUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static utils.jwt.JwtUtil.check;

public class PlayTest {

    @Test
    public void testJwt() {
        String operatorIdInput = "admin";
        String operatorIdOutput = "admin";
        Map<String, Object> map = new HashMap<>();
        map.put("operatorId", operatorIdInput);
        String jwt = JwtUtil.create(map);
        Claims claims = check(jwt);
        Assert.assertNotNull("operatorId is null", claims.get("operatorId"));
        boolean result = claims.get("operatorId").toString().equalsIgnoreCase(operatorIdOutput);
        Assert.assertTrue(result);
    }

    @Test
    public void testOptional() {
        System.out.println(Optional.ofNullable(null).orElse("is null"));
    }

    @Test
    public void testOptionalList() {
        List<Optional<String>> list = new ArrayList<>();
        list.add(Optional.ofNullable("test"));
        list.add(Optional.ofNullable(null));
        list.add(Optional.empty());
        List mapList = list.stream().map(v -> v.orElse("is null")).collect(Collectors.toList());
        mapList.size();
        for (int i = 0; i < mapList.size(); i++) {
            System.out.println(mapList.get(i));
        }
    }

    @Test
    public void testMapFlatMap() {
        String[] arr = {"1", "2", "3", "2"};
        Stream.of(arr).map(String::toUpperCase).collect(Collectors.toList()).forEach(System.out::println);
        List<List<String>> list = Arrays.asList(
                Arrays.asList("a", "b"),
                Arrays.asList("b", "c"));
        System.out.println(list);
        System.out.println(list.stream().flatMap(Collection::stream).collect(Collectors.toList()));

    }

    @Test
    public void testBlackWhite() {
        int bw1 = 1, bw2 = -1, bw3 = 1, bw4 = -1;
//        Assert.assertTrue(String.valueOf(bw1 * bw2 * bw4).equals("1"));
        boolean b1 = true, b2 = false, b3 = true, b4 = false;
//        System.out.println(b1 && b2 && b3 && b4);
        boolean result = Stream.of(true,false, true,false).reduce(true,(b, v)-> (b == v));
        System.out.println(result);
        Assert.assertTrue(result);
//        Assert.assertFalse(Stream.of(true, false, true, false).reduce(true,(result, v)-> (result == v)));
    }


}
