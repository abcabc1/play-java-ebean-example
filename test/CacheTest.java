import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.cache.redis.KeyValue;
import play.test.WithApplication;
import service.common.CacheService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CacheTest extends WithApplication {

    private CacheService cacheService;

    @Before
    public void setup() {
        cacheService = app.injector().instanceOf(CacheService.class);
    }


    @After
    public void teardown() {
        cacheService = null;
    }

    @Test
    public void set() {
        KeyValue keyValue = new KeyValue("key", "value");
        cacheService.set(keyValue.key, keyValue.value.toString());
    }

    @Test
    public void get() {
        String key = "key";
        String value = cacheService.get(key);
        Assert.assertTrue("value".equals(value));
    }

    @Test
    public void remove() {
        String key = "key";
        cacheService.remove(key);
    }

    @Test
    public void setAsync() {
        KeyValue keyValue = new KeyValue("key", "value");
        cacheService.setAsync(keyValue.key, keyValue.value.toString());
    }

    @Test
    public void getAsync() {
        String key = "key";
        cacheService.getAsync(key).thenAccept(System.out::println);
    }

    @Test
    public void removeAsync() {
        String key = "key";
        cacheService.removeAsyncKey(key);
    }



    @Test
    public void setAsyncKeys() {
        String[][] data = {
                {"k1", "v1"}
                , {"k2", "v2"}
                , {"k3", "v3"}
        };
        KeyValue[] keyValues = Arrays.stream(data).map(v -> cacheService.buildKeyValue(v[0], v[1])).toArray(KeyValue[]::new);
        cacheService.setAsyncKeys(keyValues);
    }

    @Test
    public void getAsyncKeys() {
        List<String> list = Arrays.asList("k1", "k2", "k3");
        cacheService.getAsyncKeys(list).thenAccept(System.out::println);
    }

    @Test
    public void removeAsyncKeys() {
        String[] keys = {"k1", "k2", "k3"};
        cacheService.removeAsyncKeys(keys);
    }

    @Test
    public void setAsyncListKeys() {
        String[][] data = {
                {"k1", "v11"}
                , {"k1", "v12"}
                , {"k2", "v21"}
                , {"k3", "v31"}
                , {"k3", "v32"}
        };
//        KeyValue[] keyValues = Arrays.stream(data).map(v -> buildKeyValue(v[0], v[1])).toArray(KeyValue[]::new);
        List<KeyValue> keyValueList = Arrays.stream(data).map(v -> cacheService.buildKeyValue(v[0], v[1])).collect(Collectors.toList());
        cacheService.setAsyncList(keyValueList);
    }

    @Test
    public void getAsyncListKeys() {
        List<String> keyList = Arrays.asList("1");
        List<List<String>> list = cacheService.getAsyncListKeys(keyList);
        for (int i = 0; i < list.size(); i++) {
            System.out.print(keyList.get(i) + ":");
            System.out.println(list.get(i));
        }
    }

    @Test
    public void removeAsyncListKeys() {
        String[][] data = {
                {"UU1|MM1", "-3"}
                , {"UU1|MM2", "-3"}
                , {"UU1|MM3", "-3"}
        };
        List<KeyValue> keyValueList = Arrays.stream(data).map(v -> cacheService.buildKeyValue(v[0], v[1])).collect(Collectors.toList());
        cacheService.removeAsyncListKeys(keyValueList);
    }
}
