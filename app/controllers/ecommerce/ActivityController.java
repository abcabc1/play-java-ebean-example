package controllers.ecommerce;

import models.ecommerce.promotion.Activity;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.ecommerce.sale.SaleService;
import utils.bean.BeanUtil;
import utils.http.RequestUtil;
import utils.http.ResultUtil;

import javax.inject.Inject;
import java.util.List;

import static utils.Constant.Cache4RedisKey;

public class ActivityController extends Controller {

    private final SaleService saleService;

    @Inject
    public ActivityController(SaleService saleService) {
        this.saleService = saleService;
    }

    public Result getActivity(Http.Request request) {
        List<String> codes = RequestUtil.getRequestJsonArray(request, "models", String.class);
        return ResultUtil.success(Cache4RedisKey, saleService.getActivity(codes));
    }

    public Result removeActivity(Http.Request request) {
        List<String> codes = RequestUtil.getRequestJsonArray(request, "models", String.class);
        saleService.removeActivity(codes);
        return ResultUtil.success(Cache4RedisKey, saleService.getActivity(codes));
    }

    public Result setActivity(Http.Request request) {
        List<Activity> activityList = RequestUtil.getRequestJsonArray(request, "models", Activity.class);
        saleService.setActivity(activityList);
        List<String> codes = BeanUtil.cast(activityList.stream().map(value -> value.code));
        return ResultUtil.success(Cache4RedisKey, saleService.getActivity(codes));
    }

    public Result refreshActivity(Http.Request request) {
        List<Activity> activityList = RequestUtil.getRequestJsonArray(request, "models", Activity.class);
        saleService.refreshActivity(activityList);
        List<String> codes = BeanUtil.cast(activityList.stream().map(value -> value.code));
        return ResultUtil.success(Cache4RedisKey, saleService.getActivity(codes));
    }
}
