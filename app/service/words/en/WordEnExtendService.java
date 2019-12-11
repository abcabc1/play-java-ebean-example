package service.words.en;

import com.typesafe.config.Config;
import models.words.WordEnExtend;
import play.cache.AsyncCacheApi;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import repository.words.WordEnExtendRepository;

import javax.inject.Inject;
import java.util.Optional;

public class WordEnExtendService {

    protected final Config config;
    protected final AsyncCacheApi cache;
    protected final WSClient ws;
    protected final HttpExecutionContext httpExecutionContext;
    protected final MessagesApi messagesApi;
    protected final WordEnExtendRepository repository;

    @Inject
    public WordEnExtendService(Config config, AsyncCacheApi cache, WSClient ws, HttpExecutionContext httpExecutionContext,
                               MessagesApi messagesApi, WordEnExtendRepository repository) {
        this.config = config;
        this.cache = cache;
        this.ws = ws;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
        this.repository = repository;
    }

    public String get(WordEnExtend model) {
        String rtn = "test service";
        Optional<WordEnExtend> word = repository.get(model);
        return rtn + " " + word.map(v -> v.pk.word).orElse("");
    }
}