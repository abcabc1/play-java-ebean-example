import models.ecommerce.promotion.Activity;
import models.ecommerce.promotion.ActivityRange;
import models.ecommerce.promotion.ActivityRangeMerchandise;
import models.ecommerce.promotion.ActivityRangeUser;
import models.iplay.view.CacheView;
import org.junit.Test;
import play.test.WithApplication;
import service.common.CacheService;
import service.ecommerce.sale.SaleService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

/*
test only organize the data and output, no calculation and computing which should be responsibility of service
 */
public class EcommerceTest extends WithApplication {

    SaleService saleService = new SaleService();

    @Test
    public void setActivityListCache() {
        CacheService cacheService = app.injector().instanceOf(CacheService.class);
        List<Activity> activityList = buildActivity4Test();
        List<CacheView> cacheViewList = saleService.buildCacheViewList(activityList);
        List<CacheView> revertCacheViewList = saleService.revertCacheViewList(cacheViewList);
        cacheService.setAsyncList(cacheViewList);
        cacheService.setAsyncList(revertCacheViewList);
        await().atLeast(10, SECONDS);
    }

    @Test
    public void buildActivityCacheViewList() {
        List<Activity> activityList = buildActivity4Test();
        saleService.buildCacheViewList(activityList);
    }

    @Test
    public void getActivityListCache() {
        String userId = "2";
        List<Long> merchandiseList = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        CacheService cacheService = app.injector().instanceOf(CacheService.class);
        for (Long merchandiseId : merchandiseList) {
            List<String> keyList = saleService.buildActivityRangeList(Long.parseLong(userId), merchandiseId);
            cacheService.getAsyncList(keyList).thenApplyAsync(v -> v.stream().flatMap(Collection::stream).collect(Collectors.toList())).thenAccept(v -> {
                System.out.println("merchandise: " + merchandiseId);
                System.out.println(v);
            });
        }
        await().atLeast(10, SECONDS);
//        Assert.assertTrue(cacheViewList.size() != 0);
    }

    public void removeActivityKeys() {
        List<Long> userList = Arrays.asList(1L);
        List<Long> merchandiseList = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        List<String> list = saleService.buildActivityRangeList(userList, merchandiseList);
        CacheService cacheService = app.injector().instanceOf(CacheService.class);
        cacheService.removeAsyncList(null);
    }

    public void removeActivityAll() {

    }

    private List<Activity> buildActivity4Test() {
        String[][] datass = {
                //0    1    2    3    4     5    6   7    8      9   10  11  12  13 14
//                A,AR,RU_BW,RU,RU_TYPE,UU,UT,UA,UC,RM,RM_BW,RM_TYPE,MM,MT,REMARK}
                {"1", "1", "+", "1", "UU", "1", "", "", "", "1", "+", "MM", "1", "", "用户1商品1活动1"}
                , {"1", "1", "+", "1", "UU", "1", "", "", "", "2", "+", "MM", "2", "", "用户1商品2活动1"}
                , {"2", "2", "+", "2", "UU", "1", "", "", "", "3", "+", "MM", "1", "", "用户1商品1活动2"}
                , {"2", "2", "+", "2", "UU", "1", "", "", "", "4", "+", "MM", "2", "", "用户1商品2活动2"}
                , {"3", "3", "+", "3", "UU", "1", "", "", "", "5", "-", "MM", "3", "", "用户1商品3活动3-"}
                , {"3", "3", "+", "3", "UU", "1", "", "", "", "6", "-", "MM", "1", "", "用户1商品1活动3-"}
                , {"3", "3", "+", "3", "UU", "1", "", "", "", "7", "-", "MM", "2", "", "用户1商品2活动3-"}
                , {"4", "4", "+", "4", "UU", "1", "", "", "", "8", "+", "MM", "4", "", "用户1商品4活动4"}
                , {"4", "4", "+", "4", "UU", "1", "", "", "", "9", "+", "MM", "3", "", "用户1商品3活动4"}
                , {"4", "4", "+", "4", "UU", "1", "", "", "", "10", "+", "MM", "1", "", "用户1商品1活动4"}
                , {"5", "5", "+", "5", "ALL", "", "", "", "", "11", "+", "MM", "5", "", "用户ALL商品5活动5"}
                , {"5", "5", "+", "5", "ALL", "", "", "", "", "12", "+", "MM", "6", "", "用户ALL商品6活动5"}
                , {"5", "5", "+", "5", "ALL", "", "", "", "", "13", "+", "MM", "1", "", "用户ALL商品1活动5"}
                , {"6", "6", "+", "6", "ALL", "", "", "", "", "14", "-", "MM", "7", "", "用户ALL商品7活动6-"}
                , {"7", "7", "+", "7", "UU", "2", "", "", "", "15", "-", "ALL", "", "", "用户2商品ALL活动7-"}
                , {"8", "8", "+", "8", "UU", "2", "", "", "", "16", "+", "ALL", "", "", "用户2商品ALL活动8"}
        };
        List<Activity> activityList = new ArrayList<>();
        Activity currentActivity = null;
        ActivityRange currentActivityRange = null;
        ActivityRangeUser currentActivityRangeUser = null;
        ActivityRangeMerchandise currentActivityRangeMerchandise = null;

        for (String[] datas : datass) {
            String activityId = datas[0], activityRangeId = datas[1], activityRangeUserBlackWhite = datas[2], activityRangeMerchandiseBlackWhite = datas[10], activityRangeUserId = datas[3], activityRangeUserType = datas[4], activityRangeUserUser = datas[5], activityRangeUserTag = datas[6], activityRangeUserArea = datas[7], activityRangeUserCategory = datas[8], activityRangeMerchandiseId = datas[9], activityRangeMerchandiseType = datas[11], activityRangeMerchandiseMerchandise = datas[12], activityRangeMerchandiseTag = datas[13];
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
            if (saleService.checkActivityRangeMerchandiseValid(activityRangeMerchandiseId, activityRangeMerchandiseType, activityRangeMerchandiseMerchandise, activityRangeMerchandiseTag) && (currentActivityRangeMerchandise == null || currentActivityRangeMerchandise.id != Long.parseLong(activityRangeMerchandiseId))) {
                currentActivityRangeMerchandise = saleService.buildActivityRangeMerchandise(activityRangeMerchandiseId, activityRangeMerchandiseType, activityRangeMerchandiseMerchandise, activityRangeMerchandiseTag);
                currentActivityRangeMerchandise.activityRange = currentActivityRange;
                currentActivityRange.activityRangeMerchandiseList.add(currentActivityRangeMerchandise);
            }
        }
        return activityList;
//        Arrays.stream(datas).flatMap(v-> Stream.of(v[0])).distinct().forEach(System.out::println);
//        Arrays.stream(datas).map(v-> buildActivity(v[0])).distinct().forEach(System.out::println);
//        Arrays.stream(datass).filter(v -> !("".equals(v))).map(v -> buildActivityRangeUser(v[7], v[8], v[9], v[10], v[11], v[12])).distinct().forEach(v -> userList.add(v));
    }
}
