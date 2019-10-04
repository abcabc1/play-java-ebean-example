package controllers.words;

import com.typesafe.config.Config;
import controllers.BasicController;
import models.words.WordEn;
import play.cache.AsyncCacheApi;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.mvc.Http;
import play.mvc.Result;
import repository.words.WordEnRepository;
import utils.http.RequestUtil;
import utils.http.ResultUtil;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class WordEnController extends BasicController {

    private final WordEnRepository repository;

    @Inject
    public WordEnController(Config config, AsyncCacheApi cache, WSClient ws, HttpExecutionContext httpExecutionContext, MessagesApi messagesApi, WordEnRepository repository) {
        super(config, cache, ws, httpExecutionContext, messagesApi);
        this.repository = repository;
    }

    public CompletionStage<Result> save(Http.Request request) {
        WordEn modelRequest = (WordEn) RequestUtil.getRequestJson(request, "model", WordEn.class);
        return repository.save(modelRequest).thenApplyAsync(model -> ResultUtil.success("model", model.orElse(null)), httpExecutionContext.current());
    }

    public CompletionStage<Result> get(Http.Request request) {
        WordEn modelRequest = (WordEn) RequestUtil.getRequestJson(request, "model", WordEn.class);
        return repository.get(modelRequest).thenApplyAsync(model -> ResultUtil.success("model", model.orElse(null)), httpExecutionContext.current());
    }

    public CompletionStage<Result> pagedList(Http.Request request) {
        WordEn modelRequest = (WordEn) RequestUtil.getRequestJson(request, "model", WordEn.class);
        Integer page = RequestUtil.getRequestInt(request, "model/page").orElse(0);
        Integer pageSize = RequestUtil.getRequestInt(request, "model/pageSize").orElse(10);
        String sortBy = RequestUtil.getRequestString(request, "model/sortBy").orElse("id asc");
        return repository.pagedList(modelRequest, page, pageSize, sortBy).thenApplyAsync(list -> ResultUtil.success("models, totalPage, totalCount", list.getList(), list.getTotalPageCount(), list.getTotalCount()), httpExecutionContext.current());
    }

    public CompletionStage<Result> list(Http.Request request) {
        WordEn modelRequest = (WordEn) RequestUtil.getRequestJson(request, "model", WordEn.class);
        String sortBy = RequestUtil.getRequestString(request, "model/sortBy").orElse("id asc");
        return repository.list(modelRequest, sortBy).thenApplyAsync(list -> ResultUtil.success("models", list), httpExecutionContext.current());
    }

    public CompletionStage<Result> remove(Http.Request request) {
        WordEn modelRequest = (WordEn) RequestUtil.getRequestJson(request, "model", WordEn.class);
        return repository.remove(modelRequest).thenApplyAsync(id -> ResultUtil.success("id", id.orElse(null)), httpExecutionContext.current());
    }

    public CompletionStage<Result> batchSave(Http.Request request) {
        List<WordEn> modelRequest = RequestUtil.getRequestJsonArray(request, "models", WordEn.class);
        return repository.saveBath(modelRequest).thenApplyAsync(n -> ResultUtil.success("size", n), httpExecutionContext.current());
    }

    public Result loadFromFile(Http.Request request) {
        WordEn modelRequest = (WordEn) RequestUtil.getRequestJson(request, "model", WordEn.class);
        String sortBy = RequestUtil.getRequestString(request, "model/sortBy").orElse("id asc");
        repository.loadFromFile(modelRequest, sortBy);
        return ok();
        /*
        * 1. pagelist words
        * 2. transaction
        * 3. for each word(get id, word, )
        * 4. batch save words (letter, word, word_trans, sentence, sentence_trans)
        * 5. commit
        * */
//        repository.pagedList(modelRequest, page, pageSize, sortBy);
//        return repository.saveBath(modelRequest).thenApplyAsync(n -> ResultUtil.success("size", n), httpExecutionContext.current());
    }

    public CompletionStage<Result> query(Http.Request request, String word) {
        String url = "https://dict.cn/search";
        return ws.url(url).addQueryParameter("q", word).get().thenApply(r -> r.getBody()).thenApply(v -> ok(analysis(v)));
    }

    public String analysis(String html) {
        System.out.println("do analysis");
        return "ok";
    }

    public CompletionStage<Result> load(Http.Request request) {
        WordEn modelRequest = (WordEn) RequestUtil.getRequestJson(request, "model", WordEn.class);
        String sortBy = RequestUtil.getRequestString(request, "model/sortBy").orElse("id asc");
        repository.ws = this.ws;
        return repository.load(modelRequest, sortBy).thenApply(v -> ok());
    }
}
