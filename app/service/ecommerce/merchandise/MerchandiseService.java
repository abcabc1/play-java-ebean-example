package service.ecommerce.merchandise;

import models.ecommerce.merchandise.view.MerchandiseRangeView;

import java.util.ArrayList;
import java.util.List;

public class MerchandiseService {

    public MerchandiseRangeView buildMerchandiseRangeView(Long merchandiseId, Long merchandiseTagId, Long storeId) {
        MerchandiseRangeView merchandiseRangeView = new MerchandiseRangeView();
        merchandiseRangeView.merchandiseId = merchandiseId;
        merchandiseRangeView.merchandiseTagId = merchandiseTagId;
        merchandiseRangeView.merchandiseStoreId = storeId;
        return merchandiseRangeView;
    }

    public static List<String> getMerchandiseRangeViewCodeList(MerchandiseRangeView merchandiseRangeView) {
        List<String> list = new ArrayList<>();
        if (merchandiseRangeView.merchandiseId != null && merchandiseRangeView.merchandiseId > 0) {
            list.add("MM" + merchandiseRangeView.merchandiseId);
        }
        if (merchandiseRangeView.merchandiseTagId != null && merchandiseRangeView.merchandiseTagId != 0) {
            list.add("MT" + merchandiseRangeView.merchandiseTagId);
        }
        if (merchandiseRangeView.merchandiseStoreId != null && merchandiseRangeView.merchandiseStoreId != 0) {
            list.add("MS" + merchandiseRangeView.merchandiseStoreId);
        }
        return list;
    }
}
