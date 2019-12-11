package service.ecommerce.sale;

import models.ecommerce.merchandise.Merchandise;
import models.ecommerce.merchandise.MerchandiseTag;
import models.ecommerce.merchandise.view.MerchandiseCodeView;
import models.ecommerce.promotion.Activity;
import models.ecommerce.promotion.ActivityRange;
import models.ecommerce.promotion.ActivityRangeCustomer;
import models.ecommerce.promotion.ActivityRangeMerchandise;
import models.ecommerce.promotion.view.ActivityRangeView;
import models.ecommerce.sale.StoreMerchandise;
import models.ecommerce.user.Customer;
import models.ecommerce.user.CustomerArea;
import models.ecommerce.user.CustomerCategory;
import models.ecommerce.user.CustomerTag;
import models.ecommerce.user.view.CustomerCodeView;
import org.jetbrains.annotations.NotNull;
import play.cache.redis.KeyValue;
import service.ecommerce.customer.CustomerService;
import service.ecommerce.merchandise.MerchandiseService;
import utils.maths.CalculatorUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SaleService {

    public List<ActivityRangeView> resolveActivityRangeList(List<String> list) {
        List<ActivityRangeView> activityRangeViewList = new ArrayList<>();
        for (String value : list) {
            boolean blackWhite = "+".equals(value.substring(0, 1));
            String[] subValues = value.substring(1).split("\\|");
            ActivityRangeView activityRangeView = new ActivityRangeView();
            activityRangeView.rangeCustomerBlackWhite = blackWhite;
            activityRangeView.rangeMerchandiseBlackWhite = true;
            activityRangeView.customerCodeView = buildCustomerCodeView(subValues[0]);
            activityRangeView.merchandiseCodeView = MerchandiseCodeView.buildFromCacheValue(subValues[1]);
            activityRangeViewList.add(activityRangeView);
        }
        return activityRangeViewList;
    }

    public ActivityRangeView resolveActivityRangeList(String value) {
        boolean blackWhite = "+".equals(value.substring(0, 1));
        String[] subValues = value.substring(1).split("\\|");
        ActivityRangeView activityRangeView = new ActivityRangeView();
        activityRangeView.rangeCustomerBlackWhite = blackWhite;
        activityRangeView.rangeMerchandiseBlackWhite = true;
        activityRangeView.customerCodeView = buildCustomerCodeView(subValues[0]);
        activityRangeView.merchandiseCodeView = MerchandiseCodeView.buildFromCacheValue(subValues[1]);
        return activityRangeView;
    }

    /*
    为一组用户及一组商品构造匹配规则
     */
    public List<String> buildActivityRangeKeyList(List<CustomerCodeView> customerCodeViewList, List<MerchandiseCodeView> merchandiseCodeViewList) {
        List<String> list = new ArrayList<>();
        for (CustomerCodeView customerCodeView : customerCodeViewList) {
            for (MerchandiseCodeView merchandiseCodeView : merchandiseCodeViewList) {
                list.addAll(buildActivityRangeKeyList(customerCodeView, merchandiseCodeView));
            }
        }
        return list;
    }

    /*
    构造指定用户及指定商品构造匹配规则
     */
    public List<String> buildActivityRangeKeyList(CustomerCodeView customerCodeView, MerchandiseCodeView merchandiseCodeView) {
        List<String> list = new ArrayList<>();
        List<String> customerList = CustomerService.getCustomerRangeViewCodeList(customerCodeView);
        List<String> merchandiseList = MerchandiseService.getMerchandiseRangeViewCodeList(merchandiseCodeView);
        for (String customer : customerList) {
            for (String merchandise : merchandiseList) {
                list.add(customer + "|" + merchandise);
                list.add(customer + "|" + "ALL");
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
        for (String s : keySet) {
            keyValueList.add(new KeyValue("ALL", "-" + s));
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
                if (!(CalculatorUtils.getBooleanResult(activityRange.rangeCustomerBlackWhite, activityRange.rangeMerchandiseBlackWhite))) {
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
            List<ActivityRangeCustomer> activityRangeCustomerList = new ArrayList<>();
            for (ActivityRangeCustomer activityRangeCustomer : activityRange.activityRangeCustomerList) {
                buildActivityRangeCustomerCode(activityRangeCustomer);
                activityRangeCustomerList.add(activityRangeCustomer);
            }
            for (ActivityRangeMerchandise activityRangeMerchandise : activityRange.activityRangeMerchandiseList) {
                buildActivityRangeMerchandiseCode(activityRangeMerchandise);
                activityRangeMerchandiseList.add(activityRangeMerchandise);
            }
            for (ActivityRangeMerchandise activityRangeMerchandise : activityRangeMerchandiseList) {
                for (ActivityRangeCustomer activityRangeCustomer : activityRangeCustomerList) {
                    String value = (CalculatorUtils.getBooleanResult(activityRange.rangeCustomerBlackWhite, activityRange.rangeMerchandiseBlackWhite) ? "+" : "-")
                            + activityRangeCustomer.customerType + activityRangeCustomer.customerId
                            + "|"
                            + activityRangeMerchandise.merchandiseType + activityRangeMerchandise.merchandiseId;
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
    public void buildActivityRangeCustomerCode(ActivityRangeCustomer activityRangeCustomer) {
        switch (activityRangeCustomer.customerType) {
            case "UU":
                activityRangeCustomer.customerId = activityRangeCustomer.customer.id;
                break;
            case "UT":
                activityRangeCustomer.customerId = activityRangeCustomer.customerTag.id;
                break;
            case "UA":
                activityRangeCustomer.customerId = activityRangeCustomer.customerArea.id;
                break;
            case "UC":
                activityRangeCustomer.customerId = activityRangeCustomer.customerCategory.id;
                break;
            case "ALL":
                activityRangeCustomer.customerId = 0L;
                break;
        }
    }

    /*
    归并活动商品集合
     */
    public void buildActivityRangeMerchandiseCode(ActivityRangeMerchandise activityRangeMerchandise) {
        switch (activityRangeMerchandise.merchandiseType) {
            case "MM":
                activityRangeMerchandise.merchandiseId = activityRangeMerchandise.merchandise.id;
                break;
            case "MT":
                activityRangeMerchandise.merchandiseId = activityRangeMerchandise.merchandiseTag.id;
                break;
            case "MS":
                activityRangeMerchandise.merchandiseId = activityRangeMerchandise.storeMerchandise.id;
                break;
            case "ALL":
                activityRangeMerchandise.merchandiseId = 0L;
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
    public ActivityRange buildActivityRange(String activityRangeId, String rangeCustomerBlackWhite, String rangeMerchandiseBlackWhite) {
        ActivityRange activityRange;
        activityRange = new ActivityRange();
        activityRange.id = Long.valueOf(activityRangeId);
        activityRange.rangeCustomerBlackWhite = rangeCustomerBlackWhite.equals("+");
        activityRange.activityRangeCustomerList = new ArrayList<>();
        activityRange.rangeMerchandiseBlackWhite = rangeMerchandiseBlackWhite.equals("+");
        activityRange.activityRangeMerchandiseList = new ArrayList<>();
        return activityRange;
    }

    /*
    构建活动用户集合实体
     */
    @NotNull
    public ActivityRangeCustomer buildActivityRangeCustomer(String activityRangeCustomerId, String activityRangeCustomerType, String activityRangeCustomerCustomerId, String activityRangeCustomerTagId, String activityRangeCustomerAreaId, String activityRangeCustomerCategoryId) {
        ActivityRangeCustomer activityRangeCustomer;
        activityRangeCustomer = new ActivityRangeCustomer();
        activityRangeCustomer.id = Long.valueOf(activityRangeCustomerId);
        activityRangeCustomer.customerType = activityRangeCustomerType;
        switch (activityRangeCustomerType) {
            case "UU":
                activityRangeCustomer.customer = new Customer();
                activityRangeCustomer.customer.id = Long.valueOf(activityRangeCustomerCustomerId);
                break;
            case "UT":
                activityRangeCustomer.customerTag = new CustomerTag();
                activityRangeCustomer.customerTag.id = Long.parseLong(activityRangeCustomerTagId);
                break;
            case "UA":
                activityRangeCustomer.customerArea = new CustomerArea();
                activityRangeCustomer.customerArea.id = Long.parseLong(activityRangeCustomerAreaId);
                break;
            case "UC":
                activityRangeCustomer.customerCategory = new CustomerCategory();
                activityRangeCustomer.customerCategory.id = Long.parseLong(activityRangeCustomerCategoryId);
                break;
            case "ALL":
                break;
        }
        return activityRangeCustomer;
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
                activityRangeMerchandise.storeMerchandise = new StoreMerchandise();
                activityRangeMerchandise.storeMerchandise.id = Long.parseLong(activityRangeMerchandiseStoreId);
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
    public boolean checkActivityRangeValid(String activityRangeId, String activityRangeCustomerBlackWhite, String activityRangeMerchandiseBlackWhite) {
        return activityRangeId != null && (!"".equals(activityRangeId.trim()))
                && activityRangeCustomerBlackWhite != null && (!"".equals(activityRangeCustomerBlackWhite.trim()))
                && activityRangeMerchandiseBlackWhite != null && (!"".equals(activityRangeMerchandiseBlackWhite.trim()));
    }

    /*
    校验活动用户集合
     */
    public boolean checkActivityRangeCustomerValid(String activityRangeCustomerId, String activityRangeCustomerType, String activityRangeCustomerCustomer, String activityRangeCustomerTag, String activityRangeCustomerArea, String activityRangeCustomerCategory) {
        if (activityRangeCustomerId == null || ("".equals(activityRangeCustomerId.trim()))
                || activityRangeCustomerType == null || ("".equals(activityRangeCustomerType.trim()))
                || activityRangeCustomerCustomer == null || activityRangeCustomerTag == null || activityRangeCustomerArea == null || activityRangeCustomerCategory == null) {
            return false;
        }
        return ("ALL".equalsIgnoreCase(activityRangeCustomerType) || !("".equals(activityRangeCustomerCustomer.trim())) || !("".equals(activityRangeCustomerTag.trim())) || !("".equals(activityRangeCustomerArea.trim())) || !("".equals(activityRangeCustomerCategory.trim())));
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

    public CustomerCodeView buildCustomerCodeView(String s) {
        Long id = Long.valueOf(s.substring(2));
        CustomerCodeView customerCodeView = new CustomerCodeView();
        switch (s.substring(0, 2)) {
            case "UU":
                customerCodeView.customerId = id;
            case "UT":
                customerCodeView.customerTagId = id;
            case "UA":
                customerCodeView.customerAreaId = id;
            case "UC":
                customerCodeView.customerCategoryId = id;
        }
        return customerCodeView;
    }

    public boolean match(CustomerCodeView customerCodeViewA, CustomerCodeView customerCodeViewB) {
        return customerCodeViewA.customerId == customerCodeViewB.customerId
                || customerCodeViewA.customerTagId == customerCodeViewB.customerTagId
                || customerCodeViewA.customerAreaId == customerCodeViewB.customerAreaId
                || customerCodeViewA.customerCategoryId == customerCodeViewB.customerCategoryId;
    }
}
