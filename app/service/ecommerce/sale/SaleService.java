package service.ecommerce.sale;

import models.ecommerce.customer.User;
import models.ecommerce.customer.UserArea;
import models.ecommerce.customer.UserCategory;
import models.ecommerce.customer.UserTag;
import models.ecommerce.merchandise.Merchandise;
import models.ecommerce.merchandise.MerchandiseTag;
import models.ecommerce.promotion.Activity;
import models.ecommerce.promotion.ActivityRange;
import models.ecommerce.promotion.ActivityRangeMerchandise;
import models.ecommerce.promotion.ActivityRangeUser;
import models.iplay.view.CacheView;
import org.jetbrains.annotations.NotNull;
import utils.maths.CalculatorUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SaleService {

    public List<String> buildActivityRangeList(List<Long> userList, List<Long> merchandiseList) {
        List<String> list = new ArrayList<>();
        for (Long userId : userList) {
            list.addAll(buildActivityRangeList(userId, merchandiseList));
        }
        return list;
    }

    public Set<String> buildActivitySet(Long userId, List<Long> merchandiseList) {
        Set<String> keys = new HashSet<>();
        for (Long merchandise : merchandiseList) {
            keys.add("+" + userId + "|" + merchandise);
            keys.add("-" + userId + "|" + merchandise);
            keys.add("+" + userId + "|" + "ALL");
            keys.add("-" + "ALL" + "|" + merchandise);
        }
        return keys;
    }

    public List<String> buildActivityRangeList(Long userId, List<Long> merchandiseList) {
        List<String> keyList = new ArrayList<>();
        for (Long merchandiseId : merchandiseList) {
            keyList.addAll(buildActivityRangeList(userId, merchandiseId));
        }
        return keyList;
    }

    public List<String> buildActivityRangeList(Long userId, Long merchandiseId) {
        List<String> keyList = new ArrayList<>();
        keyList.add("UU" + userId + "|MM" + merchandiseId);
        keyList.add("+UU" + userId + "|ALL");
        keyList.add("ALL" + "|MM" + merchandiseId);
        return keyList;
    }

    public List<String> getActivityByRangeKeys(List<CacheView> cacheViewList, Set<String> set) {
        List<String> list = new ArrayList<>();
        for (CacheView cacheView : cacheViewList) {
            if (set.contains(cacheView.value)) {
                list.add(cacheView.key);
            }
        }
        return list;
    }

    public List<CacheView> buildCacheViewList(List<Activity> activityList) {
        List<CacheView> cacheViewList = new ArrayList<>();
        CacheView cacheViewAll = new CacheView();
        cacheViewAll.key = "ALL";
        cacheViewAll.value = "";
        cacheViewList.add(cacheViewAll);
        for (Activity activity : activityList) {
            List<CacheView> tempList = buildCacheView(activity);
            cacheViewList.addAll(tempList);
            for (ActivityRange activityRange : activity.activityRangeList) {
                if (!(CalculatorUtils.getBooleanResult(activityRange.rangeUserBlackWhite, activityRange.rangeMerchandiseBlackWhite))) {
                    cacheViewAll.value += activity.id + ",";
                }
            }
        }
        return cacheViewList;
//        return activityList.stream().map(this::buildCacheView).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public List<CacheView> revertCacheViewList(List<CacheView> cacheViewList) {
        List<CacheView> list = new ArrayList<>();
        for (CacheView cacheView : cacheViewList) {
            if (cacheView.key.equals("ALL")) {
                continue;
            }
            String key = cacheView.key;
            String value = cacheView.value;
            CacheView cacheViewReverted = new CacheView();
            cacheViewReverted.key = value.substring(1, value.length());
            cacheViewReverted.value = value.substring(0, 1) + key;
            list.add(cacheViewReverted);
        }
        return list;
    }

    public List<CacheView> buildCacheView(Activity activity) {

        List<CacheView> cacheViewList = new ArrayList<>();
        String key = buildCacheKey(activity);

        for (ActivityRange activityRange : activity.activityRangeList) {
            List<ActivityRangeMerchandise> activityRangeMerchandiseList = new ArrayList<>();
            List<ActivityRangeUser> activityRangeUserList = new ArrayList<>();
            for (ActivityRangeUser activityRangeUser : activityRange.activityRangeUserList) {
                buildRangeUserCode(activityRangeUser);
                activityRangeUserList.add(activityRangeUser);
            }
            for (ActivityRangeMerchandise activityRangeMerchandise : activityRange.activityRangeMerchandiseList) {
                buildRangeMerchandiseCode(activityRangeMerchandise);
                activityRangeMerchandiseList.add(activityRangeMerchandise);
            }
            for (ActivityRangeMerchandise activityRangeMerchandise : activityRangeMerchandiseList) {
                for (ActivityRangeUser activityRangeUser : activityRangeUserList) {
                    CacheView cacheView = new CacheView();
                    cacheView.key = key;
                    cacheView.value = (CalculatorUtils.getBooleanResult(activityRange.rangeUserBlackWhite, activityRange.rangeMerchandiseBlackWhite) ? "+" : "-")
                            + activityRangeUser.userType + activityRangeUser.code
                            + "|"
                            + activityRangeMerchandise.merchandiseType + activityRangeMerchandise.code;
                    cacheViewList.add(cacheView);
                }
            }
        }
        return cacheViewList;
    }

    public void buildRangeUserCode(ActivityRangeUser activityRangeUser) {
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

    public void buildRangeMerchandiseCode(ActivityRangeMerchandise activityRangeMerchandise) {
        switch (activityRangeMerchandise.merchandiseType) {
            case "MM":
                activityRangeMerchandise.code = activityRangeMerchandise.merchandise.id.toString();
                break;
            case "MT":
                activityRangeMerchandise.code = activityRangeMerchandise.merchandiseTag.id.toString();
                break;
            case "ALL":
                activityRangeMerchandise.code = "";
                break;
        }
    }

    public String buildCacheKey(Activity activity) {
        return activity.id.toString();
    }

    @NotNull
    public Activity buildActivity(String activityId) {
        Activity activity;
        activity = new Activity();
        activity.id = Long.parseLong(activityId);
        activity.activityRangeList = new ArrayList<>();
        return activity;
    }

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

    @NotNull
    public ActivityRangeMerchandise buildActivityRangeMerchandise(String activityRangeMerchandiseId, String activityRangeMerchandiseType, String activityRangeMerchandiseMerchandiseId, String activityRangeMerchandiseTagId) {
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
            case "ALL":
                break;
        }
        return activityRangeMerchandise;
    }

    public boolean checkActivityValid(String activityId) {
        return !activityId.trim().equals("");
    }

    public boolean checkActivityRangeValid(String activityRangeId, String activityRangeUserBlackWhite, String activityRangeMerchandiseBlackWhite) {
        return !activityRangeId.trim().equals("") && !activityRangeUserBlackWhite.trim().equals("") && !activityRangeMerchandiseBlackWhite.trim().equals("");
    }

    public boolean checkActivityRangeUserValid(String activityRangeUserId, String activityRangeUserType, String activityRangeUserUser, String activityRangeUserTag, String activityRangeUserArea, String activityRangeUserCategory) {
        return !activityRangeUserId.trim().equals("") && !activityRangeUserType.trim().equals("") && (activityRangeUserType.equalsIgnoreCase("ALL") || !activityRangeUserUser.trim().equals("") || !activityRangeUserTag.trim().equals("") || !activityRangeUserArea.trim().equals("") || !activityRangeUserCategory.trim().equals(""));
    }

    public boolean checkActivityRangeMerchandiseValid(String activityRangeMerchandiseId, String activityRangeMerchandiseType, String activityRangeMerchandiseMerchandise, String activityRangeMerchandiseTag) {
        return !activityRangeMerchandiseId.trim().equals("") && !activityRangeMerchandiseType.trim().equals("") && (activityRangeMerchandiseType.equalsIgnoreCase("ALL") || !activityRangeMerchandiseMerchandise.trim().equals("") || !activityRangeMerchandiseTag.trim().equals(""));
    }
}
