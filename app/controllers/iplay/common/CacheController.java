package controllers.iplay.common;

import com.typesafe.config.Config;
import controllers.BasicController;
import play.cache.AsyncCacheApi;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static utils.Constant.*;

public class CacheController extends BasicController {

    @Inject
    public CacheController(Config config, AsyncCacheApi cache, WSClient ws, HttpExecutionContext httpExecutionContext, MessagesApi messagesApi) {
        super(config, cache, ws, httpExecutionContext, messagesApi);
    }

    public CompletionStage<Result> cache() {
        return cache.set(CacheKey, CacheValue, CacheAliveTime).thenApply(result -> ok("cache " + CacheKey + " set: " + result.toString()));
    }

    public CompletionStage<Result> getCache() {
        return cache.getOptional(CacheKey).thenApply(result -> ok("cache " + CacheKey + " get: " + result.orElse("").toString()));
    }

    public CompletionStage<Result> getOrSetCache() {
        return cache.getOrElseUpdate(CacheKey, () -> CompletableFuture.completedFuture(CacheValue).toCompletableFuture()).thenApply(v -> ok("cache " + CacheKey + " get: " + v));
    }

//    private CompletionStage<String> lookUpFrontPageNews() {
//        return CompletableFuture.completedFuture("a");
//    }

    public CompletionStage<Result> removeCache() {
        return cache.remove(CacheKey).thenApply(result -> ok("cache " + CacheKey + " removed: " + result.toString()));
    }

    public Result removeAllCache() {
        cache.removeAll();
        return ok("cache cleaned");
    }

    public CompletionStage<Result> cache4Redis() {
        return cache.set(Cache4RedisKey, Cache4RedisValue, CacheAliveTime).thenApply(result -> ok("cache " + Cache4RedisKey + " set: " + result.toString()));
    }

    public CompletionStage<Result> getCache4Redis() {
        return cache.getOptional(Cache4RedisKey).thenApply(result -> ok("cache " + Cache4RedisKey + " get: " + result.orElse("").toString()));
    }

    public CompletionStage<Result> getOrSetCache4Redis() {
        return cache.getOrElseUpdate(Cache4RedisKey, () -> CompletableFuture.completedFuture(Cache4RedisValue).toCompletableFuture()).thenApply(v -> ok("cache " + Cache4RedisKey + " get: " + v));
    }

    public CompletionStage<Result> removeCache4Redis() {
        return cache.remove(Cache4RedisKey).thenApply(result -> ok("cache " + Cache4RedisKey + " removed: " + result.toString()));
    }

    public Result removeAllCache4Redis() {
        cache.removeAll();
        return ok("cache cleaned");
    }
}
