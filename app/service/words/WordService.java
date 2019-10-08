package service.words;

import com.typesafe.config.Config;
import models.words.*;
import org.jsoup.Jsoup;
import play.cache.AsyncCacheApi;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import repository.words.WordRepository;
import repository.words.WordSentenceRepository;
import repository.words.WordTransRepository;
import utils.html.HtmlUtil;

import javax.inject.Inject;
import java.util.*;

public class WordService {

    protected final Config config;
    protected final AsyncCacheApi cache;
    protected final WSClient ws;
    protected final HttpExecutionContext httpExecutionContext;
    protected final MessagesApi messagesApi;
    protected final WordTransRepository wordTransRepository;
    protected final WordRepository wordRepository;
    protected final WordSentenceRepository wordSentenceRepository;

    @Inject
    public WordService(Config config, AsyncCacheApi cache, WSClient ws, HttpExecutionContext httpExecutionContext,
                       MessagesApi messagesApi, WordRepository wordRepository, WordTransRepository wordTransRepository, WordSentenceRepository wordSentenceRepository) {
        this.config = config;
        this.cache = cache;
        this.ws = ws;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
        this.wordRepository = wordRepository;
        this.wordTransRepository = wordTransRepository;
        this.wordSentenceRepository = wordSentenceRepository;
    }

    public void dict2Db(Word word, String sortBy) {
        List list = list(word, sortBy);
        dict(list);

    }

    public List list(Word word, String sortBy) {
        return wordRepository.list(word, sortBy);
    }

    @SuppressWarnings("unchecked")
    public void dict(List list) {
        String url = "https://dict.cn/search";
        for (Object model : list) {
            String queryWord = ((Word) model).code;
            ws.url(url).addQueryParameter("q", queryWord).get().thenApply(r -> r.getBody()).thenAccept(v -> analysis(queryWord, v));
        }
    }

    public void analysis(String word, String html) {
        HashSet<WordTrans> wordTransList = new HashSet<>();
        HashSet<WordSentence> wordSentenceList = new HashSet<>();
        Map<String, List<String>> map = HtmlUtil.extractDictHtml(Jsoup.parse(html));
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            WordTrans wordTrans = new WordTrans();
            WordPK wordPK = new WordPK();
            wordPK.code = word;
            String key = entry.getKey();
            wordPK.type = key.substring(0, key.indexOf("."));
            wordTrans.pk = wordPK;
            wordTrans.translation = key.replaceAll(wordPK.type + ".", "");
            wordTrans.status = true;
            wordTrans.createTime = new Date();
            wordTransList.add(wordTrans);
            for (String value : entry.getValue()) {
                String[] values = value.split("<br>");
                WordSentence wordSentence = new WordSentence();
                WordSentencePK wordSentencePK = new WordSentencePK();
                wordSentencePK.code = wordTrans.pk.code;
                wordSentencePK.type = wordTrans.pk.type;
                wordSentencePK.sentence = values[0];
                wordSentence.pk = wordSentencePK;
//                wordSentence.wordTrans = wordTrans;
                wordSentence.translation = values[1];
                wordSentence.status = true;
                wordSentence.createTime = new Date();
                wordSentenceList.add(wordSentence);
            }
        }
        wordTransRepository.saveAll(wordTransList);
//        wordSentenceRepository.saveAll(wordSentenceList);
    }
}