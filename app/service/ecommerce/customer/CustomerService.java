package service.ecommerce.customer;

import models.ecommerce.user.view.CustomerCodeView;

import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    public CustomerCodeView buildCustomerRangeView(Long customerId, Long customerTagId, Long customerAreaId, Long customerCategoryId) {
        CustomerCodeView customerCodeView = new CustomerCodeView();
        customerCodeView.customerId = customerId;
        customerCodeView.customerTagId = customerTagId;
        customerCodeView.customerAreaId = customerAreaId;
        customerCodeView.customerCategoryId = customerCategoryId;
        return customerCodeView;
    }

    public static List<String> getCustomerRangeViewCodeList(CustomerCodeView customerCodeView) {
        List<String> list = new ArrayList<>();
        if (customerCodeView.customerId > 0) {
            list.add("UU" + customerCodeView.customerId);
        }
        if (customerCodeView.customerTagId > 0) {
            list.add("UT" + customerCodeView.customerTagId);
        }
        if (customerCodeView.customerAreaId > 0) {
            list.add("UA" + customerCodeView.customerAreaId);
        }
        if (customerCodeView.customerCategoryId > 0) {
            list.add("UC" + customerCodeView.customerCategoryId);
        }
        return list;
    }
}
