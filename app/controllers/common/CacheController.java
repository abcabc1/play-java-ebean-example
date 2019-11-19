package controllers.common;

import models.iplay.view.CacheView;
import play.cache.redis.KeyValue;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.common.CacheService;
import utils.http.RequestUtil;
import utils.http.ResultUtil;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class CacheController extends Controller {

    private final HttpExecutionContext httpExecutionContext;
    private final CacheService cacheService;

    @Inject
    public CacheController(HttpExecutionContext httpExecutionContext, CacheService cacheService) {
        this.httpExecutionContext = httpExecutionContext;
        this.cacheService = cacheService;
    }

    public Result get(Http.Request request) {
        String key = RequestUtil.getRequestString(request, "key").orElse("");
        String value = cacheService.get(key);
        return ResultUtil.success("key", value);
    }

    public Result set(Http.Request request) {
        String key = RequestUtil.getRequestString(request, "key").orElse("");
        String value = RequestUtil.getRequestString(request, "value").orElse("");
        cacheService.set(key, value);
        return ResultUtil.success("key", value);
    }

    public Result remove(Http.Request request) {
        String key = RequestUtil.getRequestString(request, "key").orElse("");
        cacheService.remove(key);
        return ResultUtil.success("key", key);
    }

    public CompletionStage<Result> getAsync(Http.Request request) {
        String key = RequestUtil.getRequestString(request, "key").orElse("");
        return cacheService.getAsync(key).thenApply(v -> ResultUtil.success("key", v.orElse("is null")));
    }

    public CompletionStage<Result> setAsync(Http.Request request) {
        String key = RequestUtil.getRequestString(request, "key").orElse("");
        String value = RequestUtil.getRequestString(request, "value").orElse("");
        return cacheService.setAsync(key, value).thenApply(v -> ResultUtil.success(key + ":" + value, v.toString()));
    }

    public CompletionStage<Result> removeAsync(Http.Request request) {
        String key = RequestUtil.getRequestString(request, "key").orElse("");
        return cacheService.removeAsync(key).thenApply(v -> ResultUtil.success(key + " removed", v.toString()));
    }

    public CompletionStage<Result> getKeysAsync(Http.Request request) {
        List<String> models = RequestUtil.getRequestJsonList(request, "models", String.class);
        return cacheService.getKeysAsync(models).thenApply(v -> ResultUtil.success("caches", v.stream().map(s -> s.orElse("")).collect(Collectors.toList())));
    }

    public CompletionStage<Result> setKeysAsync(Http.Request request) {
        List<CacheView> models = RequestUtil.getRequestJsonList(request, "models", CacheView.class);
        KeyValue[] keyValues = new KeyValue[models.size()];
        for (int i = 0; i < models.size(); i++) {
            keyValues[i] = new KeyValue(models.get(i).key, models.get(i).value);
        }
        return cacheService.setKeysAsync(keyValues).thenApply(v -> ResultUtil.success("caches", models));
    }

    public CompletionStage<Result> removeKeysAsync(Http.Request request) {
        List<String> models = RequestUtil.getRequestJsonList(request, "models", String.class);
        return cacheService.removeKeysAsync(models.toArray(new String[models.size()])).thenApply(v -> ResultUtil.success("caches", models));
    }

    public Result setAsyncList(Http.Request request) {
        List<CacheView> models = RequestUtil.getRequestJsonList(request, "models", CacheView.class);
        cacheService.setAsyncList(models);
        return ResultUtil.success();
    }

    public CompletionStage<Result> getAsyncList(Http.Request request) {
        List<String> models = RequestUtil.getRequestJsonList(request, "models", String.class);
        return cacheService.getAsyncList(models).thenApplyAsync(v -> ResultUtil.success("cache", v));
    }

    public Result removeAsyncList(Http.Request request) {
        List<CacheView> models = RequestUtil.getRequestJsonList(request, "models", CacheView.class);
        cacheService.removeAsyncList(models);
        return ResultUtil.success();
    }
}
