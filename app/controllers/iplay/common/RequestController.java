package controllers.iplay.common;

import com.typesafe.config.Config;
import controllers.BasicController;
import play.cache.AsyncCacheApi;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.mvc.Result;
import utils.http.ResultUtil;

import javax.inject.Inject;

public class RequestController extends BasicController {

    @Inject
    public RequestController(Config config, AsyncCacheApi cache, WSClient ws, HttpExecutionContext httpExecutionContext, MessagesApi messagesApi) {
        super(config, cache, ws, httpExecutionContext, messagesApi);
    }

    public Result hello() {
        return ResultUtil.success();
    }

    public Result parameter4path(Long id, String name) {
        return ResultUtil.success("id,name", id, name);
    }

    public Result wildParameter4path(String path) {
        return ResultUtil.success("path", path);
    }

    public Result parameterFixValue(String page) {
        return ResultUtil.success("page", page);
    }

    public Result parameterDefaultValue(Integer page) {
        return ResultUtil.success("page", page);
    }

    public Result parameterOptionalValue(Integer page, Integer pageSize) {
        return ResultUtil.success("page,pageSize", page, pageSize);
    }

}
