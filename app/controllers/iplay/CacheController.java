package controllers.iplay;

import models.iplay.view.CacheView;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.iplay.CacheService;
import utils.http.RequestUtil;
import utils.http.ResultUtil;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class CacheController extends Controller {

    private final HttpExecutionContext httpExecutionContext;
    private final CacheService cacheService;

    @Inject
    public CacheController(HttpExecutionContext httpExecutionContext, CacheService cacheService) {
        this.httpExecutionContext = httpExecutionContext;
        this.cacheService = cacheService;
    }

    public Result setAsyncCacheList(Http.Request request) {
        List<CacheView> models  = RequestUtil.getRequestJsonArray(request, "models", CacheView.class);
        cacheService.setAsyncCacheList(models);
        return ResultUtil.success();
    }

    public CompletionStage<Result> getAsyncCacheList(Http.Request request) {
        List<String> models  = RequestUtil.getRequestJsonArray(request, "models", String.class);
        return cacheService.getAsyncCacheList(models).thenApplyAsync(v->ResultUtil.success("cache", v));
    }

    public CompletionStage<Result> getAsyncCaches(Http.Request request) {
        List<String> models  = RequestUtil.getRequestJsonArray(request, "models", String.class);
        return cacheService.getAsyncCaches(models).thenApplyAsync(v->ResultUtil.success("cache", v));
    }

    public Result getCache(Http.Request request) {
        String key = RequestUtil.getRequestString(request, "key").orElse("");
        String value = cacheService.getCache(key);
        return ResultUtil.success("key,value",key, value);
    }

    public Result removeCache(Http.Request request) {
        String key = RequestUtil.getRequestString(request, "key").orElse("");
        cacheService.removeCache(key);
        return ResultUtil.success("key",key);
    }

    public Result setCache(Http.Request request) {
        String key = RequestUtil.getRequestString(request, "key").orElse("");
        String value = RequestUtil.getRequestString(request, "value").orElse("");
        cacheService.setCache(key, value);
        return ResultUtil.success("key,value",key,value);
    }

    public CompletionStage<Result> getAsyncCache(Http.Request request) {
        String key = RequestUtil.getRequestString(request, "key").orElse("");
        return cacheService.getAsyncCache(key).thenApply(v->ResultUtil.success(key, v.orElse("")));
    }

    public Result removeAsyncCache(Http.Request request) {
        String key = RequestUtil.getRequestString(request, "key").orElse("");
        cacheService.removeAsyncCache(key);
        return ResultUtil.success("key",key);
    }

    public CompletionStage<Result> setAsyncCache(Http.Request request) {
        String key = RequestUtil.getRequestString(request, "key").orElse("");
        String value = RequestUtil.getRequestString(request, "value").orElse("");
        return cacheService.setAsyncCache(key, value).thenApply(v -> ResultUtil.success("key, value", key, v));
    }
}
