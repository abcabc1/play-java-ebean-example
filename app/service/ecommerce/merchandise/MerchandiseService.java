package service.ecommerce.merchandise;

import models.ecommerce.merchandise.view.MerchandiseCodeView;

import java.util.ArrayList;
import java.util.List;

public class MerchandiseService {

    public MerchandiseCodeView buildMerchandiseRangeView(Long merchandiseId, Long merchandiseTagId, Long storeId) {
        MerchandiseCodeView merchandiseCodeView = new MerchandiseCodeView();
        merchandiseCodeView.merchandiseId = merchandiseId;
        merchandiseCodeView.merchandiseTagId = merchandiseTagId;
        merchandiseCodeView.merchandiseStoreId = storeId;
        return merchandiseCodeView;
    }

    public static List<String> getMerchandiseRangeViewCodeList(MerchandiseCodeView merchandiseCodeView) {
        List<String> list = new ArrayList<>();
        if (merchandiseCodeView.merchandiseId != null && merchandiseCodeView.merchandiseId > 0) {
            list.add("MM" + merchandiseCodeView.merchandiseId);
        }
        if (merchandiseCodeView.merchandiseTagId != null && merchandiseCodeView.merchandiseTagId != 0) {
            list.add("MT" + merchandiseCodeView.merchandiseTagId);
        }
        if (merchandiseCodeView.merchandiseStoreId != null && merchandiseCodeView.merchandiseStoreId != 0) {
            list.add("MS" + merchandiseCodeView.merchandiseStoreId);
        }
        return list;
    }
}
