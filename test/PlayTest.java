import io.jsonwebtoken.Claims;
import org.junit.Assert;
import org.junit.Test;
import utils.jwt.JwtUtil;

import javax.validation.constraints.AssertTrue;
import java.util.HashMap;
import java.util.Map;

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
}
