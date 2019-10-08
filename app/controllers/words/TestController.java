package controllers.words;

import models.words.Word;
import models.words.WordTrans;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.words.TestService;
import service.words.WordService;
import utils.http.RequestUtil;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class TestController extends Controller {

    private final TestService testService;
    private final WordService wordService;

    @Inject
    public TestController(TestService testService, WordService wordService) {
        this.testService = testService;
        this.wordService = wordService;
    }

    public Result testService(Http.Request request) {
//        Word modelRequest = (Word) RequestUtil.getRequestJson(request, "model", Word.class);
        WordTrans modelRequest = (WordTrans) RequestUtil.getRequestJson(request, "model", WordTrans.class);
        String sortBy = RequestUtil.getRequestString(request, "model/sortBy").orElse("code asc");
        return ok(testService.test(modelRequest));
    }

    public CompletionStage<Result> dict2Db(Http.Request request) {
        Word modelRequest = (Word) RequestUtil.getRequestJson(request, "model", Word.class);
        String sortBy = RequestUtil.getRequestString(request, "model/sortBy").orElse("code asc");
        wordService.dict2Db(modelRequest, sortBy);
        return CompletableFuture.completedFuture(ok());
    }
}
