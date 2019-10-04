package controllers.iplay.account;

import com.typesafe.config.Config;
import controllers.BasicController;
import models.iplay.account.Operator;
import play.cache.AsyncCacheApi;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.mvc.Http;
import play.mvc.Result;
import repository.iplay.account.OperatorRepository;
import utils.Constant;
import utils.http.RequestUtil;
import utils.http.ResultUtil;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class OperatorController extends BasicController {

    private final OperatorRepository repository;

    @Inject
    public OperatorController(Config config, AsyncCacheApi cache, WSClient ws, HttpExecutionContext httpExecutionContext, MessagesApi messagesApi, OperatorRepository repository) {
        super(config, cache, ws, httpExecutionContext, messagesApi);
        this.repository = repository;
    }

    public CompletionStage<Result> save(Http.Request request) {
        Operator modelRequest = (Operator) RequestUtil.getRequestJson(request, "model", Operator.class);
        if (modelRequest.id == null) {
            modelRequest.operatorPass = Constant.PWD;
        }
        return repository.save(modelRequest).thenApplyAsync(model -> ResultUtil.success("model", model.orElse(null)), httpExecutionContext.current());
    }

    public CompletionStage<Result> get(Http.Request request) {
        Operator modelRequest = (Operator) RequestUtil.getRequestJson(request, "model", Operator.class);
        return repository.get(modelRequest).thenApplyAsync(model -> {
            cache.set("menus", ((Operator) model.get()).roles.stream().map(v -> v.roleCode).reduce((v1, v2) -> (v1 + "," + v2)).orElse(""));
            return ResultUtil.success("model", model.orElse(null));
        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> pagedList(Http.Request request) {
        Operator modelRequest = (Operator) RequestUtil.getRequestJson(request, "model", Operator.class);
        Integer page = RequestUtil.getRequestInt(request, "model/page").orElse(0);
        Integer pageSize = RequestUtil.getRequestInt(request, "model/pageSize").orElse(10);
        String sortBy = RequestUtil.getRequestString(request, "model/sortBy").orElse("id asc");
        return repository.pagedList(modelRequest, page, pageSize, sortBy).thenApplyAsync(list -> ResultUtil.success("models, totalPage, totalCount", list.getList(), list.getTotalPageCount(), list.getTotalCount()), httpExecutionContext.current());
    }

    public CompletionStage<Result> list(Http.Request request) {
        Operator modelRequest = (Operator) RequestUtil.getRequestJson(request, "model", Operator.class);
        String sortBy = RequestUtil.getRequestString(request, "model/sortBy").orElse("id asc");
        return repository.list(modelRequest, sortBy).thenApplyAsync(list -> ResultUtil.success("models", list), httpExecutionContext.current());
    }

    public CompletionStage<Result> remove(Http.Request request) {
        Operator modelRequest = (Operator) RequestUtil.getRequestJson(request, "model", Operator.class);
        return repository.remove(modelRequest).thenApplyAsync(id -> ResultUtil.success("id", id.orElse(null)), httpExecutionContext.current());
    }

    public CompletionStage<Result> batchSave(Http.Request request) {
        List<Operator> modelRequest = RequestUtil.getRequestJsonArray(request, "models", Operator.class);
        return repository.saveBath(modelRequest).thenApplyAsync(n -> ResultUtil.success("size", n), httpExecutionContext.current());
    }
}
