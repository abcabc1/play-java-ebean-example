package service.words;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.typesafe.config.Config;
import models.words.*;
import org.jsoup.Jsoup;
import play.cache.AsyncCacheApi;
import play.i18n.MessagesApi;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import repository.words.*;
import utils.html.HtmlUtil;
import utils.string.StringUtil;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

public class WordService {

    protected final Config config;
    protected final AsyncCacheApi cache;
    protected final WSClient ws;
    protected final HttpExecutionContext httpExecutionContext;
    protected final MessagesApi messagesApi;
    protected final WordTransRepository wordTransRepository;
    protected final WordRepository wordRepository;
    protected final WordSentenceRepository wordSentenceRepository;
    protected final ListenRepository listenRepository;
    protected final CnWordPyRepository cnWordPyRepository;
    protected final CnWordRepository cnWordRepository;

    @Inject
    public WordService(Config config, AsyncCacheApi cache, WSClient ws, HttpExecutionContext httpExecutionContext,
                       MessagesApi messagesApi, WordRepository wordRepository, WordTransRepository wordTransRepository, WordSentenceRepository wordSentenceRepository, ListenRepository listenRepository, CnWordPyRepository cnWordPyRepository, CnWordRepository cnWordRepository) {
        this.config = config;
        this.cache = cache;
        this.ws = ws;
        this.httpExecutionContext = httpExecutionContext;
        this.messagesApi = messagesApi;
        this.wordRepository = wordRepository;
        this.wordTransRepository = wordTransRepository;
        this.wordSentenceRepository = wordSentenceRepository;
        this.cnWordPyRepository = cnWordPyRepository;
        this.listenRepository = listenRepository;
        this.cnWordRepository = cnWordRepository;
    }


    public void dict2Db(Word word, String sortBy) {
        dict(wordRepository.list(word, sortBy).stream().map(v -> v.wordEn).collect(Collectors.toList()));
    }

    public void dict(List<String> list) {
        String url = "https://dict.cn/search";
        for (String s : list) {
            ws.url(url).addQueryParameter("q", s).get().thenApply(r -> r.getBody()).thenAccept(v -> analysis(s, v));
        }
    }

    public void analysis(String word, String html) {
        HashSet<WordTrans> wordTransList = new HashSet<>();
        HashSet<WordSentence> wordSentenceList = new HashSet<>();
        Map<String, List<String>> map = HtmlUtil.extractDictHtml(Jsoup.parse(html));
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            WordTrans wordTrans = new WordTrans();
            WordPK wordPK = new WordPK();
            wordPK.wordEn = word;
            String key = entry.getKey();
            wordPK.wordType = key.substring(0, key.indexOf("."));
            wordTrans.pk = wordPK;
            wordTrans.wordCn = key.replaceAll(wordPK.wordType + ".", "");
            wordTrans.status = true;
            wordTrans.createTime = new Date();
            wordTransList.add(wordTrans);
            for (String value : entry.getValue()) {
                String[] values = value.split("<br>");
                WordSentence wordSentence = new WordSentence();
                WordSentencePK wordSentencePK = new WordSentencePK();
                wordSentencePK.wordEn = wordTrans.pk.wordEn;
                wordSentencePK.wordType = wordTrans.pk.wordType;
                wordSentencePK.sentenceEn = values[0];
                wordSentence.pk = wordSentencePK;
                wordSentence.wordTrans = wordTrans;
                wordSentence.sentenceCn = values[1];
                wordSentence.status = true;
                wordSentence.createTime = new Date();
                wordSentenceList.add(wordSentence);
            }
        }
        wordTransRepository.saveAll(wordTransList);
        wordSentenceRepository.saveAll(wordSentenceList);
    }

    public void checkAndSave(Long listenId, String[] wordEns) {
        Listen model = new Listen();
        model.id = listenId;
        Optional<Listen> listen = listenRepository.get(model);
        List<String> wordEnList = new ArrayList<>();
        List<Word> wordList = new ArrayList<>();
        for (String wordEn : wordEns) {
            Word word = new Word();
            word.wordEn = wordEn;
            if (!wordRepository.get(word).isPresent()) {
                word.status = true;
                word.createTime = new Date();
                word.wordLetter = wordEn.substring(0, 1).toUpperCase();
                word.source = listen.get().source;
                wordEnList.add(wordEn);
                wordList.add(word);
            }
        }
        wordRepository.saveAll(wordList);
        dict(wordEnList);
    }

    public void trans2Py(CnWord modelRequest, String sortBy) {
        List<CnWord> cnWordList = cnWordRepository.list(modelRequest, sortBy);
        List<CnWordPy> cnWordPyList = new ArrayList<>();
        for (CnWord cnWord : cnWordList) {
            try {
                CnWordPy cnWordPy = new CnWordPy();
                CnWordPK cnWordPK = new CnWordPK();
                cnWordPK.wordCn = cnWord.wordCn;
                cnWordPK.wordPy = StringUtil.getPy(cnWord.wordCn, " ", PinyinFormat.WITH_TONE_MARK);
                cnWordPy.pk = cnWordPK;
                cnWordPy.status = true;
                cnWordPy.createTime = new Date();
                cnWordPyList.add(cnWordPy);
            } catch (PinyinException e) {
                e.printStackTrace();
            }
        }
        cnWordPyRepository.saveAll(cnWordPyList);
    }
}