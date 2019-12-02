package service.ecommerce.sale;

import models.ecommerce.customer.User;
import models.ecommerce.customer.UserArea;
import models.ecommerce.customer.UserCategory;
import models.ecommerce.customer.UserTag;
import models.ecommerce.customer.view.UserRangeView;
import models.ecommerce.merchandise.Merchandise;
import models.ecommerce.merchandise.MerchandiseTag;
import models.ecommerce.merchandise.view.MerchandiseRangeView;
import models.ecommerce.promotion.Activity;
import models.ecommerce.promotion.ActivityRange;
import models.ecommerce.promotion.ActivityRangeMerchandise;
import models.ecommerce.promotion.ActivityRangeUser;
import models.ecommerce.sale.MerchandiseStore;
import org.jetbrains.annotations.NotNull;
import play.cache.redis.KeyValue;
import service.ecommerce.customer.CustomerService;
import service.ecommerce.merchandise.MerchandiseService;
import utils.maths.CalculatorUtils;

import java.util.*;

public class SaleService {

    /*
    为一组用户及一组商品构造匹配规则
     */
    public List<String> buildActivityRangeKeyList(List<UserRangeView> userRangeViewList, List<MerchandiseRangeView> merchandiseRangeViewList) {
        List<String> list = new ArrayList<>();
        for (UserRangeView userRangeView : userRangeViewList) {
            for (MerchandiseRangeView merchandiseRangeView : merchandiseRangeViewList) {
                list.addAll(buildActivityRangeKeyList(userRangeView, merchandiseRangeView));
            }
        }
        return list;
    }

    /*
    构造指定用户及指定商品构造匹配规则
     */
    public List<String> buildActivityRangeKeyList(UserRangeView userRangeView, MerchandiseRangeView merchandiseRangeView) {
        List<String> list = new ArrayList<>();
        List<String> userList = CustomerService.getUserRangeViewCodeList(userRangeView);
        List<String> merchandiseList = MerchandiseService.getMerchandiseRangeViewCodeList(merchandiseRangeView);
        for (String user : userList) {
            for (String merchandise : merchandiseList) {
                list.add(user + "|" + merchandise);
                list.add(user + "|" + "ALL");
                list.add("ALL" + "|" + merchandise);
            }
        }
        return list;
    }

    /*
    获取活动相关的key-value集合
     */
    public List<KeyValue> getKeyValueList(List<String> activityList, List<String> activityRangeList) {
        List<KeyValue> keyValueList = new ArrayList<>();
        Set<String> keySet = new HashSet<>();
        for (String activity : activityList) {
            for (String activityRange : activityRangeList) {
                String activityRangeBlackWhite = activityRange.substring(0, 1);
                keyValueList.add(new KeyValue(activityRange.substring(1), activityRangeBlackWhite + activity));
                if (activityRangeBlackWhite.equals("-")) {
                    keySet.add(activity);
                }
            }
        }
        for (Iterator it = keySet.iterator(); it.hasNext(); ) {
            keyValueList.add(new KeyValue("ALL", "-" + it.next()));
        }
        return keyValueList;
    }

    /*
    构造活动集合并返回相应的key-value集合
     */
    public List<KeyValue> buildKeyValueList(List<Activity> activityList) {
        List<KeyValue> keyValueList = new ArrayList<>();
        for (Activity activity : activityList) {
            List<KeyValue> tempList = buildKeyValue(activity);
            keyValueList.addAll(tempList);
            for (ActivityRange activityRange : activity.activityRangeList) {
                if (!(CalculatorUtils.getBooleanResult(activityRange.rangeUserBlackWhite, activityRange.rangeMerchandiseBlackWhite))) {
                    KeyValue keyValue = new KeyValue("ALL", "-" + activity.id);
                    keyValueList.add(keyValue);
                }
            }
        }
        return keyValueList;
//        return activityList.stream().map(this::KeyValue).flatMap(Collection::stream).collect(Collectors.toList());
    }

    /*
    构建key-value集合的反向集合
     */
    public List<KeyValue> revertKeyValueList(List<KeyValue> keyValueList) {
        List<KeyValue> list = new ArrayList<>();
        for (KeyValue keyValue : keyValueList) {
            if (keyValue.key.equals("ALL")) {
                continue;
            }
            String key = keyValue.key;
            String value = keyValue.value.toString();
            KeyValue keyValueReverted = new KeyValue(value.substring(1), value.substring(0, 1) + key);
            list.add(keyValueReverted);
        }
        return list;
    }

    /*
    为指定活动构建key-value列表，用户黑白名单+商品黑白名单
     */
    public List<KeyValue> buildKeyValue(Activity activity) {

        List<KeyValue> keyValueList = new ArrayList<>();
        String key = buildCacheKey(activity);

        for (ActivityRange activityRange : activity.activityRangeList) {
            List<ActivityRangeMerchandise> activityRangeMerchandiseList = new ArrayList<>();
            List<ActivityRangeUser> activityRangeUserList = new ArrayList<>();
            for (ActivityRangeUser activityRangeUser : activityRange.activityRangeUserList) {
                buildActivityRangeUserCode(activityRangeUser);
                activityRangeUserList.add(activityRangeUser);
            }
            for (ActivityRangeMerchandise activityRangeMerchandise : activityRange.activityRangeMerchandiseList) {
                buildActivityRangeMerchandiseCode(activityRangeMerchandise);
                activityRangeMerchandiseList.add(activityRangeMerchandise);
            }
            for (ActivityRangeMerchandise activityRangeMerchandise : activityRangeMerchandiseList) {
                for (ActivityRangeUser activityRangeUser : activityRangeUserList) {
                    String value = (CalculatorUtils.getBooleanResult(activityRange.rangeUserBlackWhite, activityRange.rangeMerchandiseBlackWhite) ? "+" : "-")
                            + activityRangeUser.userType + activityRangeUser.code
                            + "|"
                            + activityRangeMerchandise.merchandiseType + activityRangeMerchandise.code;
                    KeyValue keyValue = new KeyValue(key, value);
                    keyValueList.add(keyValue);
                }
            }
        }
        return keyValueList;
    }

    /*
    归并活动用户集合
     */
    public void buildActivityRangeUserCode(ActivityRangeUser activityRangeUser) {
        switch (activityRangeUser.userType) {
            case "UU":
                activityRangeUser.code = activityRangeUser.user.id.toString();
                break;
            case "UT":
                activityRangeUser.code = activityRangeUser.userTag.id.toString();
                break;
            case "UA":
                activityRangeUser.code = activityRangeUser.userArea.id.toString();
                break;
            case "UC":
                activityRangeUser.code = activityRangeUser.userCategory.id.toString();
                break;
            case "ALL":
                activityRangeUser.code = "";
                break;
        }
    }

    /*
    归并活动商品集合
     */
    public void buildActivityRangeMerchandiseCode(ActivityRangeMerchandise activityRangeMerchandise) {
        switch (activityRangeMerchandise.merchandiseType) {
            case "MM":
                activityRangeMerchandise.code = activityRangeMerchandise.merchandise.id.toString();
                break;
            case "MT":
                activityRangeMerchandise.code = activityRangeMerchandise.merchandiseTag.id.toString();
                break;
            case "MS":
                activityRangeMerchandise.code = activityRangeMerchandise.merchandiseStore.id.toString();
                break;
            case "ALL":
                activityRangeMerchandise.code = "";
                break;
        }
    }

    /*
    构建活动key
     */
    public String buildCacheKey(Activity activity) {
        return activity.id.toString();
    }

    /*
    构建活动实体
     */
    @NotNull
    public Activity buildActivity(String activityId) {
        Activity activity;
        activity = new Activity();
        activity.id = Long.parseLong(activityId);
        activity.activityRangeList = new ArrayList<>();
        return activity;
    }

    /*
    构建活动规则实体
     */
    @NotNull
    public ActivityRange buildActivityRange(String activityRangeId, String rangeUserBlackWhite, String rangeMerchandiseBlackWhite) {
        ActivityRange activityRange;
        activityRange = new ActivityRange();
        activityRange.id = Long.valueOf(activityRangeId);
        activityRange.rangeUserBlackWhite = rangeUserBlackWhite.equals("+");
        activityRange.activityRangeUserList = new ArrayList<>();
        activityRange.rangeMerchandiseBlackWhite = rangeMerchandiseBlackWhite.equals("+");
        activityRange.activityRangeMerchandiseList = new ArrayList<>();
        return activityRange;
    }

    /*
    构建活动用户集合实体
     */
    @NotNull
    public ActivityRangeUser buildActivityRangeUser(String activityRangeUserId, String activityRangeUserType, String activityRangeUserUserId, String activityRangeUserTagId, String activityRangeUserAreaId, String activityRangeUserCategoryId) {
        ActivityRangeUser activityRangeUser;
        activityRangeUser = new ActivityRangeUser();
        activityRangeUser.id = Long.valueOf(activityRangeUserId);
        activityRangeUser.userType = activityRangeUserType;
        switch (activityRangeUserType) {
            case "UU":
                activityRangeUser.user = new User();
                activityRangeUser.user.id = Long.valueOf(activityRangeUserUserId);
                break;
            case "UT":
                activityRangeUser.userTag = new UserTag();
                activityRangeUser.userTag.id = Long.parseLong(activityRangeUserTagId);
                break;
            case "UA":
                activityRangeUser.userArea = new UserArea();
                activityRangeUser.userArea.id = Long.parseLong(activityRangeUserAreaId);
                break;
            case "UC":
                activityRangeUser.userCategory = new UserCategory();
                activityRangeUser.userCategory.id = Long.parseLong(activityRangeUserCategoryId);
                break;
            case "ALL":
                break;
        }
        return activityRangeUser;
    }

    /*
    构建活动商品集合实体
     */
    @NotNull
    public ActivityRangeMerchandise buildActivityRangeMerchandise(String activityRangeMerchandiseId, String activityRangeMerchandiseType, String activityRangeMerchandiseMerchandiseId, String activityRangeMerchandiseTagId, String activityRangeMerchandiseStoreId) {
        ActivityRangeMerchandise activityRangeMerchandise;
        activityRangeMerchandise = new ActivityRangeMerchandise();
        activityRangeMerchandise.id = Long.valueOf(activityRangeMerchandiseId);
        activityRangeMerchandise.merchandiseType = activityRangeMerchandiseType;
        switch (activityRangeMerchandiseType) {
            case "MM":
                activityRangeMerchandise.merchandise = new Merchandise();
                activityRangeMerchandise.merchandise.id = Long.parseLong(activityRangeMerchandiseMerchandiseId);
                break;
            case "MT":
                activityRangeMerchandise.merchandiseTag = new MerchandiseTag();
                activityRangeMerchandise.merchandiseTag.id = Long.parseLong(activityRangeMerchandiseTagId);
                break;
            case "MS":
                activityRangeMerchandise.merchandiseStore = new MerchandiseStore();
                activityRangeMerchandise.merchandiseStore.id = Long.parseLong(activityRangeMerchandiseStoreId);
                break;
            case "ALL":
                break;
        }
        return activityRangeMerchandise;
    }

    /*
    校验活动
     */
    public boolean checkActivityValid(String activityId) {
        return activityId != null && !("".equals(activityId.trim()));
    }

    /*
    校验活动集合
     */
    public boolean checkActivityRangeValid(String activityRangeId, String activityRangeUserBlackWhite, String activityRangeMerchandiseBlackWhite) {
        if (activityRangeId == null || ("".equals(activityRangeId.trim()))
                || activityRangeUserBlackWhite == null || ("".equals(activityRangeUserBlackWhite.trim()))
                || activityRangeMerchandiseBlackWhite == null || ("".equals(activityRangeMerchandiseBlackWhite.trim()))) {
            return false;
        }
        return true;
    }

    /*
    校验活动用户集合
     */
    public boolean checkActivityRangeUserValid(String activityRangeUserId, String activityRangeUserType, String activityRangeUserUser, String activityRangeUserTag, String activityRangeUserArea, String activityRangeUserCategory) {
        if (activityRangeUserId == null || ("".equals(activityRangeUserId.trim()))
                || activityRangeUserType == null || ("".equals(activityRangeUserType.trim()))
                || activityRangeUserUser == null || activityRangeUserTag == null || activityRangeUserArea == null || activityRangeUserCategory == null) {
            return false;
        }
        return ("ALL".equalsIgnoreCase(activityRangeUserType) || !("".equals(activityRangeUserUser.trim())) || !("".equals(activityRangeUserTag.trim())) || !("".equals(activityRangeUserArea.trim())) || !("".equals(activityRangeUserCategory.trim())));
    }

    /*
    校验活动商品集合
     */
    public boolean checkActivityRangeMerchandiseValid(String activityRangeMerchandiseId, String activityRangeMerchandiseType, String activityRangeMerchandiseMerchandise, String activityRangeMerchandiseTag, String activityRangeMerchandiseStore) {
        if (activityRangeMerchandiseId == null || ("".equals(activityRangeMerchandiseId.trim()))
                || activityRangeMerchandiseType == null || ("".equals(activityRangeMerchandiseType.trim()))
                || activityRangeMerchandiseMerchandise == null || activityRangeMerchandiseTag == null || activityRangeMerchandiseStore == null) {
            return false;
        }
        return "ALL".equalsIgnoreCase(activityRangeMerchandiseType) || !("".equals(activityRangeMerchandiseMerchandise.trim())) || !("".equals(activityRangeMerchandiseTag.trim())) || !("".equals(activityRangeMerchandiseStore.trim()));
    }
}
