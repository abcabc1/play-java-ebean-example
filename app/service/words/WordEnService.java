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


    public void dict2Db(WordEn wordEn, String sortBy) {
        dict(wordRepository.list(wordEn, sortBy).stream().map(v -> v.word).collect(Collectors.toList()));
    }

    public void dict(List<String> list) {
        String url = "https://dict.cn/search";
        for (String s : list) {
            ws.url(url).addQueryParameter("q", s).get().thenApply(r -> r.getBody()).thenAccept(v -> analysis(s, v));
        }
    }

    public void analysis(String word, String html) {
        HashSet<WordEnExtend> wordEnExtendList = new HashSet<>();
        HashSet<WordEnSentence> wordEnSentenceList = new HashSet<>();
        Map<String, List<String>> map = HtmlUtil.extractDictHtml(Jsoup.parse(html));
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            WordEnExtend wordEnExtend = new WordEnExtend();
            WordEnPK wordEnPK = new WordEnPK();
            wordEnPK.word = word;
            String key = entry.getKey();
            wordEnPK.type = key.substring(0, key.indexOf("."));
            wordEnExtend.pk = wordEnPK;
            wordEnExtend.wordCn = key.replaceAll(wordEnPK.type + ".", "");
            wordEnExtend.status = true;
            wordEnExtend.createTime = new Date();
            wordEnExtendList.add(wordEnExtend);
            for (String value : entry.getValue()) {
                String[] values = value.split("<br>");
                WordEnSentence wordEnSentence = new WordEnSentence();
                WordEnSentencePK wordEnSentencePK = new WordEnSentencePK();
                wordEnSentencePK.word = wordEnExtend.pk.word;
                wordEnSentencePK.type = wordEnExtend.pk.type;
                wordEnSentencePK.sentence = values[0];
                wordEnSentence.pk = wordEnSentencePK;
                wordEnSentence.wordEnExtend = wordEnExtend;
                wordEnSentence.sentenceCn = values[1];
                wordEnSentence.status = true;
                wordEnSentence.createTime = new Date();
                wordEnSentenceList.add(wordEnSentence);
            }
        }
        wordTransRepository.saveAll(wordEnExtendList);
        wordSentenceRepository.saveAll(wordEnSentenceList);
    }

    public void checkAndSave(Long listenId, String[] wordEns) {
        Listen model = new Listen();
        model.id = listenId;
        Optional<Listen> listen = listenRepository.get(model);
        List<String> wordEnList = new ArrayList<>();
        List<WordEn> wordList = new ArrayList<>();
        for (String wordEn : wordEns) {
            WordEn word = new WordEn();
            word.word = wordEn;
            if (!wordRepository.get(word).isPresent()) {
                word.status = true;
                word.createTime = new Date();
                word.letter = wordEn.substring(0, 1).toUpperCase();
                word.source = listen.get().source;
                wordEnList.add(wordEn);
                wordList.add(word);
            }
        }
        wordRepository.saveAll(wordList);
        dict(wordEnList);
    }

    public void trans2Py(WordCn modelRequest, String sortBy) {
        List<WordCn> wordCnList = cnWordRepository.list(modelRequest, sortBy);
        List<WordCnExtend> wordCnExtendList = new ArrayList<>();
        for (WordCn wordCn : wordCnList) {
            try {
                WordCnExtend wordCnExtend = new WordCnExtend();
                WordCnPK wordCnPK = new WordCnPK();
                wordCnPK.word = wordCn.word;
                wordCnPK.pinyin = StringUtil.getPy(wordCn.word, " ", PinyinFormat.WITH_TONE_MARK);
                wordCnExtend.pk = wordCnPK;
                wordCnExtend.status = true;
                wordCnExtend.createTime = new Date();
                wordCnExtend.wrongWord = "";
                wordCnExtendList.add(wordCnExtend);
            } catch (PinyinException e) {
                e.printStackTrace();
            }
        }
        cnWordPyRepository.saveAll(wordCnExtendList);
    }

    public void testTime(WordCn modelRequest, String sortBy) {
        List<WordCn> wordCnList = cnWordRepository.list(modelRequest, sortBy);
        System.out.println("size="+ wordCnList.size());
    }
}