package service.words;

import com.typesafe.config.Config;
import play.cache.AsyncCacheApi;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import repository.words.WordSentenceRepository;

import javax.inject.Inject;

public class WordSentenceService {

    protected final Config config;
    protected final AsyncCacheApi cache;
    protected final WSClient ws;
    protected final HttpExecutionContext httpExecutionContext;
    protected final MessagesApi messagesApi;
    protected final WordSentenceRepository repository;

    @Inject
    public WordSentenceService(Config config, AsyncCacheApi cache, WSClient ws, HttpExecutionContext httpExecutionContext,
                               MessagesApi messagesApi, WordSentenceRepository repository) {
        this.config = config;
        this.cache = cache;
        this.ws = ws;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
        this.repository = repository;
    }


}
