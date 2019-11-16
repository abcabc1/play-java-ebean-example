package service.iplay;

import akka.Done;
import models.iplay.view.CacheView;
import play.cache.SyncCacheApi;
import play.cache.redis.AsyncCacheApi;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class CacheService {

    protected final AsyncCacheApi asyncCache;
    protected final SyncCacheApi cache;

    @Inject
    public CacheService(AsyncCacheApi asyncCache, SyncCacheApi cache) {
        this.asyncCache = asyncCache;
        this.cache = cache;
    }


    public void setCache(String key, String value) {
        cache.set(key, value);
    }

    public void removeCache(String key) {
        cache.remove(key);
    }

    public String getCache(String key) {
        return cache.getOptional(key).orElse("").toString();
    }

    public CompletionStage<Optional<Object>> getAsyncCache(String key) {
        return asyncCache.getOptional(key);
    }

    public CompletionStage<Done> removeAsyncCache(String key) {
        return asyncCache.remove(key);
    }

    public CompletionStage<Done> setAsyncCache(String key, String value) {
        return asyncCache.set(key, value);
    }

    public CompletionStage<List<Optional<String>>> getAsyncCaches(List<String> keys) {
        return asyncCache.getAll(String.class, keys);
    }

    public void setAsyncCacheList(List<CacheView> list) {
        list.forEach(v -> asyncCache.list(v.key, String.class).append(v.value));
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<List<List<String>>> getAsyncCacheList(List<String> keys) {
        List<String> list = new ArrayList<>();
        List<CompletionStage<List<String>>> futureList = keys.stream().map(v -> asyncCache.list(v, String.class).toList()).collect(Collectors.toList());
        return CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()]))
                .thenApply(v -> futureList.stream().map(CompletionStage::toCompletableFuture).map(CompletableFuture::join).collect(Collectors.toList()))
                .whenComplete((v, e) -> {
                    v.forEach(list::addAll);
                });
    }
}
