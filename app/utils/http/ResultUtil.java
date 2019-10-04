package utils.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yb on 2018/1/16.
 */
public class ResultUtil {

    public final static JsonNode EMPTY_NODE = Json.newObject();

    public static Result failure() {
        return failure(null, null);
    }

    public static Result failure(String code) {
        String key = "isSuccess,code,message";
        return Results.ok(ResultUtil.getMJson(key, false, false, code, null));
    }

    public static Result failure(String code, String message) {
        String key = "isSuccess,code,message";
        return Results.ok(ResultUtil.getMJson(key, false, false, code, message));
    }

    public static Result success(String key, Object... o) {
        ObjectNode objectNode = Json.newObject();
        objectNode.put("isSuccess", true);
        ObjectNode dataNode = (ObjectNode) ResultUtil.getMJson(key, o);
        objectNode.set("data", dataNode);
        return Results.ok(objectNode);
    }

    public static Result success(Boolean ignoreNull, String key, Object... o) {
        String[] keys = key.split(",");
        ObjectNode objectNode = Json.newObject();
        objectNode.put("isSuccess", true);
        ObjectNode dataNode = (ObjectNode) ResultUtil.getMJson(key, ignoreNull, o);
        objectNode.set("data", dataNode);
        return Results.ok(objectNode);
    }

    public static Result success() {
        String key = "isSuccess";
        return Results.ok(ResultUtil.getMJson(key, true, true));
    }

    public static JsonNode getJson(Object o) {
        return getJson(o, false);
    }

    public static JsonNode getJson(Object o, Boolean ignoreNull) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (ignoreNull) {
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        }
        Json.setObjectMapper(objectMapper);
        return (o == null) ? EMPTY_NODE : Json.toJson(o);
    }

    public static JsonNode getMJson(String key, Object... o) {
        return getMJson(key, true, o);
    }

    public static JsonNode getMJson(String key, Boolean ignoreNull, Object... o) {
        if (key == null || o == null) {
            return EMPTY_NODE;
        }
        String[] keys = key.split(",");
        if (keys.length != o.length) {
            return EMPTY_NODE;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < o.length; i++) {
            if (o[i] == null) {
                continue;
            }
            map.put(keys[i].trim(), o[i]);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        if (ignoreNull) {
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        }
        String jsonString = "{}";
        try {
            jsonString = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Json.parse(jsonString);
    }
}