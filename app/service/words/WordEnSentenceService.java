package service.words;

import com.typesafe.config.Config;
import play.cache.AsyncCacheApi;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import repository.words.WordEnSentenceRepository;

import javax.inject.Inject;

public class WordEnSentenceService {

    protected final Config config;
    protected final AsyncCacheApi cache;
    protected final WSClient ws;
    protected final HttpExecutionContext httpExecutionContext;
    protected final MessagesApi messagesApi;
    protected final WordEnSentenceRepository repository;

    @Inject
    public WordEnSentenceService(Config config, AsyncCacheApi cache, WSClient ws, HttpExecutionContext httpExecutionContext,
                                 MessagesApi messagesApi, WordEnSentenceRepository repository) {
        this.config = config;
        this.cache = cache;
        this.ws = ws;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
        this.repository = repository;
    }


}
