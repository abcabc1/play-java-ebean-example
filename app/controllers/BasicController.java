package controllers;

import com.typesafe.config.Config;
import play.cache.AsyncCacheApi;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.mvc.Controller;

import javax.inject.Inject;

/**
 * Created by yb on 2018/1/17.
 */
public class BasicController extends Controller {

    protected final Config config;
    protected final AsyncCacheApi cache;
    protected final WSClient ws;
    protected final HttpExecutionContext httpExecutionContext;
    protected final MessagesApi messagesApi;

    @Inject
    public BasicController(Config config, AsyncCacheApi cache, WSClient ws, HttpExecutionContext httpExecutionContext,
                           MessagesApi messagesApi) {
        this.config = config;
        this.cache = cache;
        this.ws = ws;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
    }
}
