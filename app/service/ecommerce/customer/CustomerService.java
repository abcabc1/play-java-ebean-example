package service.ecommerce.customer;

import models.ecommerce.customer.view.UserRangeView;

import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    public UserRangeView buildUserRangeView(Long userId, Long userTagId, Long userAreaId, Long userCategoryId) {
        UserRangeView userRangeView = new UserRangeView();
        userRangeView.userId = userId;
        userRangeView.userTagId = userTagId;
        userRangeView.userAreaId = userAreaId;
        userRangeView.userCategoryId = userCategoryId;
        return userRangeView;
    }

    public static List<String> getUserRangeViewCodeList(UserRangeView userRangeView) {
        List<String> list = new ArrayList<>();
        if (userRangeView.userId != null && userRangeView.userId > 0) {
            list.add("UU" + userRangeView.userId);
        }
        if (userRangeView.userTagId != null && userRangeView.userTagId > 0) {
            list.add("UT" + userRangeView.userTagId);
        }
        if (userRangeView.userAreaId != null && userRangeView.userAreaId > 0) {
            list.add("UA" + userRangeView.userAreaId);
        }
        if (userRangeView.userCategoryId != null && userRangeView.userCategoryId > 0) {
            list.add("UC" + userRangeView.userCategoryId);
        }
        return list;
    }
}
