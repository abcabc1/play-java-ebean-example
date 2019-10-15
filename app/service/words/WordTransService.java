package service.words;

import com.typesafe.config.Config;
import models.words.WordTrans;
import play.cache.AsyncCacheApi;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import repository.words.WordTransRepository;

import javax.inject.Inject;
import java.util.Optional;

public class WordTransService {

    protected final Config config;
    protected final AsyncCacheApi cache;
    protected final WSClient ws;
    protected final HttpExecutionContext httpExecutionContext;
    protected final MessagesApi messagesApi;
    protected final WordTransRepository repository;

    @Inject
    public WordTransService(Config config, AsyncCacheApi cache, WSClient ws, HttpExecutionContext httpExecutionContext,
                            MessagesApi messagesApi, WordTransRepository repository) {
        this.config = config;
        this.cache = cache;
        this.ws = ws;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
        this.repository = repository;
    }

    public String get(WordTrans model) {
        String rtn = "test service";
        Optional<WordTrans> word = repository.get(model);
        return rtn + " " + word.map(v -> v.pk.wordEn).orElse("");
    }
}