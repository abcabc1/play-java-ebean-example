import models.ecommerce.customer.view.UserRangeView;
import models.ecommerce.merchandise.view.MerchandiseRangeView;
import models.ecommerce.promotion.Activity;
import models.ecommerce.promotion.ActivityRange;
import models.ecommerce.promotion.ActivityRangeMerchandise;
import models.ecommerce.promotion.ActivityRangeUser;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.cache.redis.KeyValue;
import play.test.WithApplication;
import service.common.CacheService;
import service.ecommerce.customer.CustomerService;
import service.ecommerce.merchandise.MerchandiseService;
import service.ecommerce.sale.SaleService;
import utils.maths.CalculatorUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

/*
test only organize the data and output, no calculation and computing which should be responsibility of service
 */
public class EcommerceTest extends WithApplication {

    SaleService saleService = new SaleService();
    CustomerService customerService = new CustomerService();
    MerchandiseService merchandiseService = new MerchandiseService();

    private CacheService cacheService;

    @Before
    public void setup() {
        cacheService = app.injector().instanceOf(CacheService.class);
    }

    @After
    public void teardown() {
        cacheService = null;
    }

    /*
    设置活动缓存
     */
    @Test
    public void setActivityListCache() {
        List<Activity> activityList = buildActivityData();
        List<KeyValue> keyValueList = saleService.buildKeyValueList(activityList);
        List<KeyValue> revertCacheViewList = saleService.revertKeyValueList(keyValueList);
        cacheService.setAsyncList(keyValueList);
        cacheService.setAsyncList(revertCacheViewList);
        await().atLeast(10, SECONDS);
    }

    /*
    获取用户商品匹配的活动
     */
    @Test
    public void getActivityListCache() {
        Set<String> set = cacheService.getAsyncSetKeys(Arrays.asList("ALL")).stream().flatMap(Collection::stream).collect(Collectors.toSet());
        Long[][] users = {
                //{UU,UT,UA,UC}
                {1L, 0L, 0L, 0L}
                , {2L, 1L, 0L, 0L}
                , {3L, 0L, 1L, 0L}
                , {4L, 0L, 0L, 1L}
        };
        Long[][] merchandises = {
                //{MM,MT,MS}
                {1L, 1L, 1L}
                , {2L, 1L, 1L}
                , {3L, 2L, 2L}
        };
        List<UserRangeView> userRangeViewList = Arrays.stream(users).map(v -> customerService.buildUserRangeView(v[0], v[1], v[2], v[3])).collect(Collectors.toList());
        List<MerchandiseRangeView> merchandiseRangeViewList = Arrays.stream(merchandises).map(v -> merchandiseService.buildMerchandiseRangeView(v[0], v[1], v[2])).collect(Collectors.toList());
        for (UserRangeView userRangeView : userRangeViewList) {
            System.out.println(userRangeView);
            for (MerchandiseRangeView merchandiseRangeView : merchandiseRangeViewList) {
                List<String> keyList = saleService.buildActivityRangeKeyList(userRangeView, merchandiseRangeView);
                System.out.print(merchandiseRangeView);
                System.out.println(CalculatorUtils.unionRelative(cacheService.getAsyncSetKeys(keyList).stream().flatMap(Collection::stream).collect(Collectors.toSet()), set));
            }
            System.out.println();
        }
//        await().atLeast(10, SECONDS);
//        Assert.assertTrue(cacheViewList.size() != 0);
    }

    @Test
    public void removeActivityList() {
        String[] activities = {"1", "2", "4", "6", "8"};
        List<String> keyList = Arrays.asList(activities);
        List<String> list = cacheService.getAsyncListKeys(keyList).stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<KeyValue> keyValueList = saleService.getKeyValueList(keyList, list);
        cacheService.removeAsyncKeys(activities);
        cacheService.removeAsyncListKeys(keyValueList);
    }

    private List<Activity> buildActivityData() {
        String[][] datass = {
//                 0  1   2      3   4        5   6   7   8   9   10     11       12  13  14  15
//                {A, AR, RU_BW, RU, RU_TYPE, UU, UT, UA, UC, RM, RM_BW, RM_TYPE, MM, MT, MS, REMARK}
//                {"1", "1", "+", "1", "UU", "1", "", "", "", "1", "+", "MM", "1", "", "", "活动1   用户1商品1"}
//                , {"1", "1", "+", "1", "UU", "1", "", "", "", "2", "+", "MM", "2", "", "", "活动1   用户1商品2"}
//                , {"2", "2", "+", "2", "UU", "1", "", "", "", "3", "+", "MM", "1", "", "", "活动2   用户1商品1"}
//                , {"2", "2", "+", "2", "UU", "1", "", "", "", "4", "+", "MM", "2", "", "", "活动2   用户1商品2"}
//                , {"3", "3", "+", "3", "UU", "1", "", "", "", "5", "-", "MM", "3", "", "", "活动3   用户1商品3-"}
//                , {"3", "3", "+", "3", "UU", "1", "", "", "", "6", "-", "MM", "1", "", "", "活动3   用户1商品1-"}
//                , {"3", "3", "+", "3", "UU", "1", "", "", "", "7", "-", "MM", "2", "", "", "活动3   用户1商品2-"}
//                , {"4", "4", "+", "4", "UU", "1", "", "", "", "8", "+", "MM", "4", "", "", "活动4   用户1商品4"}
//                , {"4", "4", "+", "4", "UU", "1", "", "", "", "9", "+", "MM", "3", "", "", "活动4   用户1商品3"}
//                , {"4", "4", "+", "4", "UU", "1", "", "", "", "10", "+", "MM", "1", "", "", "活动4   用户1商品1"}
//                , {"4", "4", "+", "4", "UU", "1", "", "", "", "11", "+", "MM", "2", "", "", "活动4   用户1商品2"}
//                , {"5", "5", "+", "5", "ALL", "", "", "", "", "12", "+", "MM", "5", "", "", "活动5   用户ALL商品5"}
//                , {"5", "5", "+", "5", "ALL", "", "", "", "", "13", "+", "MM", "6", "", "", "活动5   用户ALL商品6"}
//                , {"5", "5", "+", "5", "ALL", "", "", "", "", "14", "+", "MM", "1", "", "", "活动5   用户ALL商品1"}
//                , {"6", "6", "+", "6", "ALL", "", "", "", "", "15", "-", "MM", "7", "", "", "活动6   用户ALL商品7-"}
//                , {"7", "7", "+", "7", "UU", "2", "", "", "", "16", "-", "ALL", "", "", "", "活动7   用户2商品ALL-"}
//                , {"8", "8", "+", "8", "UU", "2", "", "", "", "16", "+", "ALL", "", "", "", "活动8   用户2商品ALL"}
//                , {"9", "9", "+", "9", "UT", "", "1", "", "", "17", "+", "MT", "", "1", "", "活动9   用户标签1商品类别1"}
//                , {"9", "9", "+", "10", "UT", "", "2", "", "", "17", "+", "MT", "", "1", "", "活动9   用户标签2商品类别1"}
//                , {"10", "10", "+", "11", "UA", "", "", "1", "", "18", "+", "MT", "", "1", "", "活动10 用户区域1商品类别1"}
//                , {"11", "11", "+", "12", "UC", "", "", "", "1", "19", "+", "MT", "", "1", "", "活动11 用户类别1商品类别1"}
                {"12", "12", "+", "13", "UU", "1", "", "", "", "20", "+", "MS", "", "", "1", "活动12 用户1商品商户1"}
                , {"12", "12", "+", "13", "UU", "1", "", "", "", "21", "+", "MS", "", "", "2", "活动12 用户1商品商户2"}
                , {"13", "13", "+", "14", "UU", "1", "", "", "", "22", "-", "MS", "", "", "1", "活动13 用户1商品商户1-"}
        };
        return buildActitityList(datass);
//        Arrays.stream(datas).flatMap(v-> Stream.of(v[0])).distinct().forEach(System.out::println);
//        Arrays.stream(datas).map(v-> buildActivity(v[0])).distinct().forEach(System.out::println);
//        Arrays.stream(datass).filter(v -> !("".equals(v))).map(v -> buildActivityRangeUser(v[7], v[8], v[9], v[10], v[11], v[12])).distinct().forEach(v -> userList.add(v));
    }

    /*
    构建活动集合
     */
    @NotNull
    public List<Activity> buildActitityList(String[][] datas) {
        List<Activity> activityList = new ArrayList<>();
        Activity currentActivity = null;
        ActivityRange currentActivityRange = null;
        ActivityRangeUser currentActivityRangeUser = null;
        ActivityRangeMerchandise currentActivityRangeMerchandise = null;

        for (String[] data : datas) {
            String activityId = data[0], activityRangeId = data[1], activityRangeUserBlackWhite = data[2], activityRangeMerchandiseBlackWhite = data[10], activityRangeUserId = data[3], activityRangeUserType = data[4], activityRangeUserUser = data[5], activityRangeUserTag = data[6], activityRangeUserArea = data[7], activityRangeUserCategory = data[8], activityRangeMerchandiseId = data[9], activityRangeMerchandiseType = data[11], activityRangeMerchandiseMerchandise = data[12], activityRangeMerchandiseTag = data[13], activityRangeMerchandiseStoreId = data[14];
            if (saleService.checkActivityValid(activityId) && (currentActivity == null || currentActivity.id != Long.parseLong(activityId))) {
                currentActivity = saleService.buildActivity(activityId);
                activityList.add(currentActivity);
            }
            if (saleService.checkActivityRangeValid(activityRangeId, activityRangeUserBlackWhite, activityRangeMerchandiseBlackWhite) && (currentActivityRange == null || currentActivityRange.id != Long.parseLong(activityRangeId))) {
                currentActivityRange = saleService.buildActivityRange(activityRangeId, activityRangeUserBlackWhite, activityRangeMerchandiseBlackWhite);
                currentActivityRange.activity = currentActivity;
                currentActivity.activityRangeList.add(currentActivityRange);
            }
            if (saleService.checkActivityRangeUserValid(activityRangeUserId, activityRangeUserType, activityRangeUserUser, activityRangeUserTag, activityRangeUserArea, activityRangeUserCategory) && (currentActivityRangeUser == null || currentActivityRangeUser.id != Long.parseLong(activityRangeUserId))) {
                currentActivityRangeUser = saleService.buildActivityRangeUser(activityRangeUserId, activityRangeUserType, activityRangeUserUser, activityRangeUserTag, activityRangeUserArea, activityRangeUserCategory);
                currentActivityRangeUser.activityRange = currentActivityRange;
                currentActivityRange.activityRangeUserList.add(currentActivityRangeUser);
            }
            if (saleService.checkActivityRangeMerchandiseValid(activityRangeMerchandiseId, activityRangeMerchandiseType, activityRangeMerchandiseMerchandise, activityRangeMerchandiseTag, activityRangeMerchandiseStoreId) && (currentActivityRangeMerchandise == null || currentActivityRangeMerchandise.id != Long.parseLong(activityRangeMerchandiseId))) {
                currentActivityRangeMerchandise = saleService.buildActivityRangeMerchandise(activityRangeMerchandiseId, activityRangeMerchandiseType, activityRangeMerchandiseMerchandise, activityRangeMerchandiseTag, activityRangeMerchandiseStoreId);
                currentActivityRangeMerchandise.activityRange = currentActivityRange;
                currentActivityRange.activityRangeMerchandiseList.add(currentActivityRangeMerchandise);
            }
        }
        return activityList;
    }
}
