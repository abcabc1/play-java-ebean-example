package service.ecommerce.sale;

import models.ecommerce.promotion.Activity;
import play.cache.SyncCacheApi;
import play.cache.redis.AsyncCacheApi;
import utils.bean.BeanUtil;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public void setActivity(List<Activity> activityList) {
        for(Activity activity : activityList) {
            Set<String> set = activity.build();
            cache.set(activity.code, set);
//            for (RangeMerchandise rangeMerchandise: activityView.rangeMerchandiseList) {
//                String value = ActivityView.buildActivityMerchandise(rangeMerchandise);
//                cache.set(activityView.activity.code, value);
//            }
        }
    }

    public void setActivityAll() {

    }

    public void removeActivity(List<String> codes) {
        for(String code: codes) {
            cache.remove(Cache4RedisKey);
        }
    }

    public void removeActivityAll() {

    }

    public List<String> getActivity(List<String> codes) {
        List<String> list = new ArrayList<>();
        for(String code: codes) {
            list.add((String) cache.getOptional(code).orElse(""));
        }
        return list;
    }

    public List<String> getActivityAll() {
        List<String> list = new ArrayList<>();
        return list;
    }

    public void logActivity(List<String> codes) {
        for(String code: codes) {
            System.out.println(cache.getOptional(code).orElse(""));
        }
    }

    public void logActivityAll() {
    }

    public void getOrUpdateActivity(List<Activity> activityList) {
        for(Activity activityView: activityList) {
            System.out.println(cache.getOrElseUpdate(Cache4RedisKey, () -> Cache4RedisValue));
        }
    }

    public void getOrUpdateActivityAll() {

    }

    public void refreshActivity(List<Activity> activityList) {
        List<String> list = BeanUtil.cast(activityList.stream().map(value -> value.code));
        removeActivity(list);
//        setActivity(activityViewList);
        getActivity(list);
    }

    public void refreshActivityAll() {
        removeActivityAll();
        setActivityAll();
        logActivityAll();
    }
}
