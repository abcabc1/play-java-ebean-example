package service.ecommerce.sale;

import models.ecommerce.customer.User;
import models.ecommerce.merchandise.Merchandise;
import models.ecommerce.promotion.Activity;
import models.ecommerce.promotion.RangeMerchandise;
import models.ecommerce.promotion.RangeUser;
import models.iplay.view.CacheView;
import play.cache.SyncCacheApi;
import play.cache.redis.AsyncCacheApi;
import utils.maths.CalculatorUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        return asyncCache.getAll(String.class, ids);
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

    public List<String> getActivityKeysByRange(List<Merchandise> merchandiseList, List<User> userList) {
        List<String> keys = new ArrayList<>();
        for (Merchandise merchandise : merchandiseList) {
            for (User user : userList) {
                keys.add("+" + merchandise.id + "|" + user.id);
                keys.add("-" + merchandise.id + "|" + user.id);
            }
        }
        return keys;
    }

    public List<CacheView> buildCacheView(Activity activity) {
        List<CacheView> cacheViewList = new ArrayList<>();
        String key = buildCacheKey(activity);
        buildActivityMerchandise(activity);
        buildActivityUser(activity);
        for (RangeMerchandise rangeMerchandise : activity.rangeMerchandiseList) {
            for (RangeUser rangeUser : activity.rangeUserList) {
                CacheView cacheView = new CacheView();
                cacheView.key = key;
                cacheView.value = (CalculatorUtils.getBooleanResult(rangeMerchandise.blackWhite, rangeUser.blackWhite) ? "+" : "-") + rangeMerchandise.merchandiseType + rangeMerchandise.code + "|" + rangeUser.userType + rangeUser.code;
                cacheViewList.add(cacheView);
            }
        }
        return cacheViewList;
    }

    private void buildActivityUser(Activity activity) {
        for (RangeUser rangeUser : activity.rangeUserList) {
            switch (rangeUser.userType) {
                case "UU":
                    rangeUser.code = rangeUser.user.id.toString();
                    break;
                case "UG":
                    rangeUser.code = rangeUser.tag.code;
                    break;
                case "UA":
                    rangeUser.code = rangeUser.area;
                    break;
                case "UT":
                    rangeUser.code = rangeUser.user.type.code;
                    break;
                default:
                    rangeUser.code = "";
                    break;
            }
        }
    }

    private void buildActivityMerchandise(Activity activity) {
        for (RangeMerchandise rangeMerchandise : activity.rangeMerchandiseList) {
            switch (rangeMerchandise.merchandiseType) {
                case "MM":
                    rangeMerchandise.code = rangeMerchandise.merchandise.id.toString();
                    break;
                case "MG":
                    rangeMerchandise.code = rangeMerchandise.tag.code;
                    break;
                default:
                    rangeMerchandise.code = "";
                    break;
            }
        }
    }

    private String buildCacheKey(Activity activity) {
        return activity.id.toString();
    }
}
