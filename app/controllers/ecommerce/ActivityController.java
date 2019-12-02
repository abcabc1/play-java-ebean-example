package controllers.ecommerce;

import models.ecommerce.customer.User;
import models.ecommerce.merchandise.Merchandise;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.common.CacheService;
import service.ecommerce.sale.SaleService;
import utils.http.RequestUtil;
import utils.http.ResultUtil;

import javax.inject.Inject;
import java.util.List;

public class ActivityController extends Controller {

    private final HttpExecutionContext httpExecutionContext;
    private final SaleService saleService;
    private final CacheService cacheService;

    @Inject
    public ActivityController(HttpExecutionContext httpExecutionContext, SaleService saleService, CacheService cacheService) {
        this.httpExecutionContext = httpExecutionContext;
        this.saleService = saleService;
        this.cacheService = cacheService;
    }

    public Result getActivityByRange(Http.Request request) {
        List<Merchandise> merchandiseList = RequestUtil.getRequestJsonList(request, "models", Merchandise.class);
        List<User> userList = RequestUtil.getRequestJsonList(request, "models", User.class);
        List<String> stringList = null;//saleService.getActivityKeysByRange(merchandiseList, userList);
        return ResultUtil.success("cache", cacheService.getAsyncListKeys(stringList));
    }

    public Result getActivity(Http.Request request) {
        List<String> models = RequestUtil.getRequestJsonList(request, "models", String.class);
        return ResultUtil.success("cache", cacheService.getAsyncListKeys(models));
//        return cacheService.getAsyncList(models).thenApplyAsync(v -> ResultUtil.success("cache", v.stream().flatMap(Collection::stream).collect(Collectors.toList())), httpExecutionContext.current());
    }

    public Result removeActivity(Http.Request request) {
//        List<CacheView> models = RequestUtil.getRequestJsonList(request, "models", CacheView.class);
//        cacheService.removeAsyncList(models);
        return ResultUtil.success();
    }

    public Result setActivity(Http.Request request) {
//        List<Activity> models = RequestUtil.getRequestJsonList(request, "models", Activity.class);
//        List<CacheView> cacheViewList = saleService.buildCacheViewList(models);
////        List<CacheView> cacheViewList = models.stream().map(saleService::getCacheView).flatMap(Collection::stream).collect(Collectors.toList());
//        cacheService.setAsyncList(cacheViewList);
        return ResultUtil.success();
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
