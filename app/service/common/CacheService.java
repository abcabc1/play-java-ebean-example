package service.common;

import akka.Done;
import play.cache.SyncCacheApi;
import play.cache.redis.AsyncCacheApi;
import play.cache.redis.AsyncRedisList;
import play.cache.redis.KeyValue;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    public CompletionStage<Done> setAsync(String key, String value) {
        return asyncCache.set(key, value);
    }

    public CompletionStage<Done> setAsyncKeys(KeyValue[] keyValues) {
        return asyncCache.setAll(keyValues);
    }

    public List<AsyncRedisList<String>> setAsyncList(List<KeyValue> list) {
        return list.stream().map(v -> asyncCache.list(v.key, String.class).append(v.value.toString())).map(CompletionStage::toCompletableFuture).map(CompletableFuture::join).collect(Collectors.toList());
//        list.forEach(v -> asyncCache.list(v.key, String.class).append(v.value.toString()));
    }

    public String get(String key) {
        return cache.getOptional(key).orElse("").toString();
    }

    public CompletionStage<Optional<Object>> getAsync(String key) {
        return asyncCache.getOptional(key);
    }

    public CompletionStage<List<Optional<String>>> getAsyncKeys(List<String> keys) {
        return asyncCache.getAll(String.class, keys);
    }

    public List<List<String>> getAsyncListKeys(List<String> keyList) {
        return keyList.stream().map(v -> asyncCache.list(v, String.class).toList()).collect(Collectors.toList()).stream().map(CompletionStage::toCompletableFuture).map(CompletableFuture::join).collect(Collectors.toList());
    }

//    @SuppressWarnings("unchecked")
//    public CompletableFuture<List<List<String>>> getAsyncListKeys(List<String> keys) {
//        List<String> list = new ArrayList<>();
//        List<CompletionStage<List<String>>> futureList = keys.stream().map(v -> asyncCache.list(v, String.class).toList()).collect(Collectors.toList());
//        return CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()]))
//                .thenApply(v -> futureList.stream().map(CompletionStage::toCompletableFuture).map(CompletableFuture::join).collect(Collectors.toList()))
//                .whenComplete((v, e) -> {
//                    v.forEach(list::addAll);
//                });
//    }

    public Set<List<String>> getAsyncSetKeys(List<String> keyList) {
        return keyList.stream().map(v -> asyncCache.list(v, String.class).toList()).collect(Collectors.toSet()).stream().map(CompletionStage::toCompletableFuture).map(CompletableFuture::join).collect(Collectors.toSet());
    }

    public void remove(String key) {
        cache.remove(key);
    }

    public CompletionStage<Done> removeAsyncKey(String key) {
        return asyncCache.remove(key);
    }

    public CompletionStage<Done> removeAsyncKeys(String[] keys) {
        return asyncCache.removeAllKeys(keys);
    }

    public void removeAsyncListKeys(List<KeyValue> list) {
        list.stream().map(v->asyncCache.list(v.key,String.class).remove(v.value.toString())).map(CompletionStage::toCompletableFuture).map(CompletableFuture::join).collect(Collectors.toList());
    }

    public void removeAsyncSet(Set<KeyValue> set) {
        for (Iterator it = set.iterator(); it.hasNext(); ) {
            KeyValue keyValue = (KeyValue) it.next();
            asyncCache.set(keyValue.key, String.class).remove(keyValue.value.toString());
        }
    }

    public KeyValue buildKeyValue(String key, String value) {
        return new KeyValue(key, value);
    }
}
