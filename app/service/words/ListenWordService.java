package service.words;

import com.typesafe.config.Config;
import models.words.ListenWord;
import play.cache.AsyncCacheApi;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import repository.words.ListenWordRepository;

import javax.inject.Inject;
import java.util.List;

public class ListenWordService {

    protected final Config config;
    protected final AsyncCacheApi cache;
    protected final WSClient ws;
    protected final HttpExecutionContext httpExecutionContext;
    protected final MessagesApi messagesApi;
    protected final ListenWordRepository repository;

    @Inject
    public ListenWordService(Config config, AsyncCacheApi cache, WSClient ws, HttpExecutionContext httpExecutionContext,
                             MessagesApi messagesApi, ListenWordRepository repository) {
        this.config = config;
        this.cache = cache;
        this.ws = ws;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
        this.repository = repository;
    }

    public void saveAll(List<ListenWord> listenWordList) {
        repository.saveAll(listenWordList);
    }
}