package controllers.ecommerce;

import models.ecommerce.promotion.Activity;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.ecommerce.sale.SaleService;
import utils.http.RequestUtil;
import utils.http.ResultUtil;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class ActivityController extends Controller {

    private final HttpExecutionContext httpExecutionContext;
    private final SaleService saleService;

    @Inject
    public ActivityController(HttpExecutionContext httpExecutionContext, SaleService saleService) {
        this.httpExecutionContext = httpExecutionContext;
        this.saleService = saleService;
    }

    public CompletionStage<Result> getActivity(Http.Request request) {
        List<String> ids = RequestUtil.getRequestJsonArray(request, "models", String.class);
        return saleService.getActivity(ids).thenApplyAsync(v->ResultUtil.success("activity", v),
        httpExecutionContext.current());
    }

    public Result removeActivity(Http.Request request) {
//        List<Long> ids = BeanUtil.cast(RequestUtil.getRequestJsonArray(request, "models", String.class));
//        saleService.removeActivity(ids);
//        return ResultUtil.success(Cache4RedisKey, saleService.getActivity(ids));
        return ok();
    }

    public CompletionStage<Result> setActivity(Http.Request request) {
        List<Activity> activityList = RequestUtil.getRequestJsonArray(request, "models", Activity.class);
        return saleService.setActivity(activityList)
                .thenApplyAsync(
                        v -> ResultUtil.success("activity", v.toList()),
                        httpExecutionContext.current());
    }

    public Result refreshActivity(Http.Request request) {
//        List<Activity> activityList = RequestUtil.getRequestJsonArray(request, "models", Activity.class);
//        saleService.refreshActivity(activityList);
//        List<Long> ids = activityList.stream().map(value -> value.id).collect(Collectors.toList());
//        ;
//        return ResultUtil.success(Cache4RedisKey, saleService.getActivity(ids));
        return ok();
    }
}
