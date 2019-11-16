package service.ecommerce.sale;

import models.ecommerce.promotion.Activity;
import play.cache.SyncCacheApi;
import play.cache.redis.AsyncCacheApi;
import play.cache.redis.AsyncRedisList;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static utils.Constant.Cache4RedisKey;
import static utils.Constant.Cache4RedisValue;

public class SaleService {

    protected final AsyncCacheApi asyncCache;
    protected final SyncCacheApi cache;

    @Inject
    public SaleService(AsyncCacheApi asyncCache, SyncCacheApi cache) {
        this.asyncCache = asyncCache;
        this.cache = cache;
    }

    public CompletionStage<AsyncRedisList<String>> setActivity(List<Activity> activityList) {
        CompletionStage<AsyncRedisList<String>> list = null;
        for (Activity activity : activityList) {
            Set<String> set = activity.build();
            for (String value : set) {
                list  = asyncCache.list(activity.id.toString(), String.class).append(value);
            }
        }
        return list;
    }

    public void setActivityAll() {

    }

    public void removeActivity(List<Long> ids) {
        for (Long id : ids) {
            cache.remove(Cache4RedisKey);
        }
    }

    public void removeActivityAll() {

    }

    public CompletionStage<List<Optional<String>>> getActivity(List<String> ids) {
//        cache.getOptional("1");
//        return asyncCache.getOptional("1").thenApplyAsync(Optional::get);
        return asyncCache.getAll(String.class, ids);
//        List<String> list = new ArrayList<>();
//        return CompletableFuture.supplyAsync(
//                () -> {
//                    for (Long id : ids) {
//                        asyncCache.list(id.toString(), String.class).toList().thenApply(v -> list.addAll(v));
//                    }
//                    return list;
//                }
//        );
    }

    public List<String> getActivityAll() {
        List<String> list = new ArrayList<>();
        return list;
    }

    public void logActivity(List<String> codes) {
        for (String code : codes) {
            System.out.println(cache.getOptional(code).orElse(""));
        }
    }

    public void logActivityAll() {
    }

    public void getOrUpdateActivity(List<Activity> activityList) {
        for (Activity activityView : activityList) {
            System.out.println(cache.getOrElseUpdate(Cache4RedisKey, () -> Cache4RedisValue));
        }
    }

    public void getOrUpdateActivityAll() {

    }

    public void refreshActivity(List<Activity> activityList) {
        List<Long> list = activityList.stream().map(value -> value.id).collect(Collectors.toList());
        removeActivity(list);
//        setActivity(activityViewList);
//        getActivity(list);
    }

    public void refreshActivityAll() {
        removeActivityAll();
        setActivityAll();
        logActivityAll();
    }
}
