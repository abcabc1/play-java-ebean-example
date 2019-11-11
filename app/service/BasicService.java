package service;

import com.typesafe.config.Config;
import play.cache.SyncCacheApi;
import play.cache.redis.AsyncCacheApi;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;

import javax.inject.Inject;

public class BasicService {
    protected final Config config;
    protected final AsyncCacheApi asyncCache;
    protected final SyncCacheApi cache;
    protected final WSClient ws;
    protected final HttpExecutionContext httpExecutionContext;
    protected final MessagesApi messagesApi;

    @Inject
    public BasicService(Config config, AsyncCacheApi asyncCache, SyncCacheApi cache, WSClient ws, HttpExecutionContext httpExecutionContext,
                         MessagesApi messagesApi) {
        this.config = config;
        this.asyncCache = asyncCache;
        this.cache = cache;
        this.ws = ws;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
    }
}
