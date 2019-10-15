package controllers.words;

import models.words.*;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import service.words.ListenWordService;
import service.words.WordSentenceService;
import service.words.WordService;
import service.words.WordTransService;
import utils.http.RequestUtil;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class WordsController extends Controller {

    private final WordService wordService;
    private final WordTransService wordTransService;
    private final WordSentenceService wordSentenceService;
    private final ListenWordService listenWordService;

    @Inject
    public WordsController(WordService wordService, WordTransService wordTransService, WordSentenceService wordSentenceService, ListenWordService listenWordService) {
        this.wordService = wordService;
        this.wordTransService = wordTransService;
        this.wordSentenceService = wordSentenceService;
        this.listenWordService = listenWordService;
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
        wordService.checkAndSave(listenId, wordEns.split(","));
        listenWordService.saveAll(listenWordList);
        return play.mvc.Results.ok();
    }

    public Result trans2Py(Http.Request request) {
        WordPy modelRequest = (WordPy) RequestUtil.getRequestJson(request, "model", WordPy.class);
        String sortBy = RequestUtil.getRequestString(request, "model/sortBy").orElse("code asc");
        wordService.trans2Py(modelRequest, sortBy);
        return play.mvc.Results.ok();
    }

    public Result testService(Http.Request request) {
        WordTrans modelRequest = (WordTrans) RequestUtil.getRequestJson(request, "model", WordTrans.class);
//        String sortBy = RequestUtil.getRequestString(request, "model/sortBy").orElse("code asc");
        return ok(wordTransService.get(modelRequest));
    }

    public CompletionStage<Result> dict2Db(Http.Request request) {
        Word modelRequest = (Word) RequestUtil.getRequestJson(request, "model", Word.class);
        String sortBy = RequestUtil.getRequestString(request, "model/sortBy").orElse("word_en asc");
        wordService.dict2Db(modelRequest, sortBy);
        return CompletableFuture.completedFuture(ok());
    }
}
