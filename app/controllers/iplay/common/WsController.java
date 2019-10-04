package controllers.iplay.common;

import com.typesafe.config.Config;
import controllers.BasicController;
import play.cache.AsyncCacheApi;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class WsController extends BasicController {
    @Inject
    public WsController(Config config, AsyncCacheApi cache, WSClient ws, HttpExecutionContext httpExecutionContext, MessagesApi messagesApi) {
        super(config, cache, ws, httpExecutionContext, messagesApi);
    }

    public CompletionStage<Result> example() {
        String url = "http://example.com";
        return ws.url(url)
                .get()
                .thenApply(result -> ok(result.toString()));
    }
}
