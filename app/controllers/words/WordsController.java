package controllers.words;

import models.words.*;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.words.ListenWordService;
import service.words.WordEnSentenceService;
import service.words.WordEnService;
import service.words.WordEnExtendService;
import utils.http.RequestUtil;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class WordsController extends Controller {

    private final WordEnService wordEnService;
    private final WordEnExtendService wordEnExtendService;
    private final WordEnSentenceService wordEnSentenceService;
    private final ListenWordService listenWordService;

    @Inject
    public WordsController(WordEnService wordEnService, WordEnExtendService wordEnExtendService, WordEnSentenceService wordEnSentenceService, ListenWordService listenWordService) {
        this.wordEnService = wordEnService;
        this.wordEnExtendService = wordEnExtendService;
        this.wordEnSentenceService = wordEnSentenceService;
        this.listenWordService = listenWordService;
    }

    public Result testTime(Http.Request request) {
        WordCn modelRequest = (WordCn) RequestUtil.getRequestJson(request, "model", WordCn.class);
        String sortBy = RequestUtil.getRequestString(request, "model/sortBy").orElse("word_cn asc");
        wordEnService.testTime(modelRequest, sortBy);
        return play.mvc.Results.ok();
    }

    public Result listen2Word(Http.Request request) {
        Long listenId = RequestUtil.getRequestLong(request, "model/listenId").orElse(0L);
        String wordEns = RequestUtil.getRequestString(request, "model/words").orElse("");
        List<ListenWord> listenWordList = new ArrayList<>();
        for (String wordEn: wordEns.split(",")) {
            ListenWord listenWord = new ListenWord();
            ListenWordPK pk = new ListenWordPK();
            pk.listenId = listenId;
            pk.wordEn = wordEn;
            listenWord.pk = pk;
            listenWord.status = true;
            listenWord.createTime = new Date();
            listenWordList.add(listenWord);
        }
        wordEnService.checkAndSave(listenId, wordEns.split(","));
        listenWordService.saveAll(listenWordList);
        return play.mvc.Results.ok();
    }

    public Result trans2Py(Http.Request request) {
        WordCn modelRequest = (WordCn) RequestUtil.getRequestJson(request, "model", WordCn.class);
        String sortBy = RequestUtil.getRequestString(request, "model/sortBy").orElse("word asc");
        wordEnService.trans2Py(modelRequest, sortBy);
        return play.mvc.Results.ok();
    }

    public Result testService(Http.Request request) {
        WordEnExtend modelRequest = (WordEnExtend) RequestUtil.getRequestJson(request, "model", WordEnExtend.class);
//        String sortBy = RequestUtil.getRequestString(request, "model/sortBy").orElse("code asc");
        return ok(wordEnExtendService.get(modelRequest));
    }

    public CompletionStage<Result> dict2Db(Http.Request request) {
        WordEn modelRequest = (WordEn) RequestUtil.getRequestJson(request, "model", WordEn.class);
        String sortBy = RequestUtil.getRequestString(request, "model/sortBy").orElse("word_en asc");
        wordEnService.dict2Db(modelRequest, sortBy);
        return CompletableFuture.completedFuture(ok());
    }
}
