package service.common;

import akka.Done;
import models.iplay.view.CacheView;
import play.cache.SyncCacheApi;
import play.cache.redis.AsyncCacheApi;
import play.cache.redis.KeyValue;

import javax.inject.Inject;
import java.util.*;
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

    public void set(String key, String value) {
        cache.set(key, value);
    }

    public void remove(String key) {
        cache.remove(key);
    }

    public String get(String key) {
        return cache.getOptional(key).orElse("").toString();
    }

    public CompletionStage<Optional<Object>> getAsync(String key) {
        return asyncCache.getOptional(key);
    }

    public CompletionStage<Done> removeAsync(String key) {
        return asyncCache.remove(key);
    }

    public CompletionStage<Done> setAsync(String key, String value) {
        return asyncCache.set(key, value);
    }

    public CompletionStage<List<Optional<String>>> getKeysAsync(List<String> keys) {
        return asyncCache.getAll(String.class, keys);
    }

    public CompletionStage<Done> setKeysAsync(KeyValue[] keyValues) {
        return asyncCache.setAll(keyValues);
    }

    public CompletionStage<Done> removeKeysAsync(String[] keys) {
        return asyncCache.removeAllKeys(keys);
    }

    public void setAsyncList(List<CacheView> list) {
        list.forEach(v -> asyncCache.list(v.key, String.class).append(v.value));
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<List<List<String>>> getAsyncList(List<String> keys) {
        List<String> list = new ArrayList<>();
        List<CompletionStage<List<String>>> futureList = keys.stream().map(v -> asyncCache.list(v, String.class).toList()).collect(Collectors.toList());
        return CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()]))
                .thenApply(v -> futureList.stream().map(CompletionStage::toCompletableFuture).map(CompletableFuture::join).collect(Collectors.toList()))
                .whenComplete((v, e) -> {
                    v.forEach(list::addAll);
                });
    }

    @SuppressWarnings("unchecked")
    public CompletionStage<List<String>> getAsyncSingleList(String key) {
        return asyncCache.list(key, String.class).toList();
    }

    public void removeAsyncList(List<CacheView> list) {
        list.forEach(v -> asyncCache.list(v.key, String.class).remove(v.value));
    }

    public void setAsyncSet(Set<CacheView> set) {
        for (Iterator it = set.iterator(); it.hasNext();) {
            CacheView cacheView = (CacheView)it.next();
            asyncCache.set(cacheView.key, String.class).add(cacheView.value);
        }
    }

    public CompletableFuture<List<Set<String>>> getAsyncSet(List<String> keys) {
        List<String> list = new ArrayList<>();
        List<CompletionStage<Set<String>>> futureList = keys.stream().map(v->asyncCache.set(v, String.class).toSet()).collect(Collectors.toList());
        return CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()]))
                .thenApply(v -> futureList.stream().map(CompletionStage::toCompletableFuture).map(CompletableFuture::join).collect(Collectors.toList()))
                .whenComplete((v, e) -> {
                    v.forEach(list::addAll);
                });
    }

    public void removeAsyncSet(Set<CacheView> set) {
        for (Iterator it = set.iterator(); it.hasNext();) {
            CacheView cacheView = (CacheView)it.next();
            asyncCache.set(cacheView.key, String.class).remove(cacheView.value);
        }
    }
}
