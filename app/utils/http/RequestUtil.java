package utils.http;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Http;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by yb on 2018/1/9.
 */
public class RequestUtil {

    public static <A> List<A> getRequestJsonList(Http.Request request, String object, Class<A> objectClass) {
        JsonNode jsonNode = resolveNode(request, object);
        List<A> list = new ArrayList<>();
        if (jsonNode.isArray()) {
            for (JsonNode node : jsonNode) {
                list.add(Json.fromJson(node, objectClass));
            }
        }
        return list;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static <A> Object getRequestJson(Http.Request request, String object, Class<A> objectClass) {
        JsonNode jsonNode = resolveNode(request, object);
        return Json.fromJson(jsonNode, objectClass);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Optional<Integer> getRequestInt(Http.Request request, String object) {
        JsonNode jsonNode = resolveNode(request, object);
        return (jsonNode == null) ? Optional.empty() : Optional.ofNullable(Integer.valueOf(jsonNode.asInt()));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Optional<String> getRequestString(Http.Request request, String object) {
        JsonNode jsonNode = resolveNode(request, object);
        return (jsonNode == null) ? Optional.empty() : Optional.ofNullable(jsonNode.asText());
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Optional<Boolean> getRequestBoolean(Http.Request request, String object) {
        JsonNode jsonNode = resolveNode(request, object);
        return (jsonNode == null) ? Optional.empty() : Optional.ofNullable(Boolean.valueOf(jsonNode.asBoolean()));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Optional<Long> getRequestLong(Http.Request request, String object) {
        JsonNode jsonNode = resolveNode(request, object);
        return (jsonNode == null) ? Optional.empty() : Optional.ofNullable(Long.valueOf(jsonNode.asLong()));
    }

    private static JsonNode resolveNode(Http.Request request, String object) {
        String[] paths = object.split("/");
        JsonNode jsonNode = null;
        for (int i = 0; i < paths.length; i++) {
            if (i == 0) {
                jsonNode = request.body().asJson().get(paths[0]);
            } else {
                jsonNode = jsonNode.get(paths[i]);
            }
        }
        return jsonNode;
    }
}
