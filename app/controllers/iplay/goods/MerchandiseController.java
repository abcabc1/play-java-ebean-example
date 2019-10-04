package controllers.iplay.goods;

import com.typesafe.config.Config;
import controllers.BasicController;
import models.iplay.goods.Merchandise;
import play.cache.AsyncCacheApi;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.mvc.Http;
import play.mvc.Result;
import repository.iplay.goods.MerchandiseRepository;
import utils.http.RequestUtil;
import utils.http.ResultUtil;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class MerchandiseController extends BasicController {

    private final MerchandiseRepository repository;

    @Inject
    public MerchandiseController(Config config, AsyncCacheApi cache, WSClient ws, HttpExecutionContext httpExecutionContext, MessagesApi messagesApi, MerchandiseRepository repository) {
        super(config, cache, ws, httpExecutionContext, messagesApi);
        this.repository = repository;
    }

    public CompletionStage<Result> save(Http.Request request) {
        Merchandise modelRequest = (Merchandise) RequestUtil.getRequestJson(request, "model", Merchandise.class);
        return repository.save(modelRequest).thenApplyAsync(model -> ResultUtil.success("model", model.orElse(null)), httpExecutionContext.current());
    }

    public CompletionStage<Result> get(Http.Request request) {
        Merchandise modelRequest = (Merchandise) RequestUtil.getRequestJson(request, "model", Merchandise.class);
        return repository.get(modelRequest).thenApplyAsync(model -> ResultUtil.success("model", model.orElse(null)), httpExecutionContext.current());
    }

    public CompletionStage<Result> pagedList(Http.Request request) {
        Merchandise modelRequest = (Merchandise) RequestUtil.getRequestJson(request, "model", Merchandise.class);
        Integer page = RequestUtil.getRequestInt(request, "model/page").orElse(0);
        Integer pageSize = RequestUtil.getRequestInt(request, "model/pageSize").orElse(10);
        String sortBy = RequestUtil.getRequestString(request, "model/sortBy").orElse("id asc");
        return repository.pagedList(modelRequest, page, pageSize, sortBy).thenApplyAsync(list -> ResultUtil.success("models, totalPage, totalCount", list.getList(), list.getTotalPageCount(), list.getTotalCount()), httpExecutionContext.current());
    }

    public CompletionStage<Result> list(Http.Request request) {
        Merchandise modelRequest = (Merchandise) RequestUtil.getRequestJson(request, "model", Merchandise.class);
        String sortBy = RequestUtil.getRequestString(request, "model/sortBy").orElse("id asc");
        return repository.list(modelRequest, sortBy).thenApplyAsync(list -> ResultUtil.success("models", list), httpExecutionContext.current());
    }

    public CompletionStage<Result> remove(Http.Request request) {
        Merchandise modelRequest = (Merchandise) RequestUtil.getRequestJson(request, "model", Merchandise.class);
        return repository.remove(modelRequest).thenApplyAsync(id -> ResultUtil.success("id", id.orElse(null)), httpExecutionContext.current());
    }

    public CompletionStage<Result> batchSave(Http.Request request) {
        List<Merchandise> modelRequest = RequestUtil.getRequestJsonArray(request, "models", Merchandise.class);
        return repository.saveBath(modelRequest).thenApplyAsync(n -> ResultUtil.success("size", n), httpExecutionContext.current());
    }
}
