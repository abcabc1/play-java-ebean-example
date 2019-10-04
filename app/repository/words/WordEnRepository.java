package repository.words;

import io.ebean.Expr;
import io.ebean.ExpressionList;
import models.base.BaseModel;
import models.words.WordEn;
import play.db.ebean.EbeanConfig;
import play.libs.ws.WSClient;
import repository.BasicRepository;
import repository.DatabaseExecutionContext;
import utils.FileUtil;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class WordEnRepository extends BasicRepository<WordEn> {

    public WSClient ws;

    @Inject
    public WordEnRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        super(ebeanConfig, executionContext, "words");
    }

    @Override
    public ExpressionList<? extends BaseModel> getExpr(WordEn model) {
        ExpressionList<? extends BaseModel> expressionList = getExpressionList(model);
        if (model.id != null) {
            expressionList.add(Expr.eq("id", model.id));
        }
        if (model.letter != null) {
            expressionList.add(Expr.eq("letter", model.letter));
        }
        if (model.word != null) {
            expressionList.add(Expr.eq("word", model.word));
        }
        if (model.sentence != null) {
            expressionList.add(Expr.eq("sentence", model.sentence));
        }
        return expressionList;
    }

    public void loadFromFile(WordEn model, String sortBy) {
        int page = 0, pageSize = 1000;
        List<? extends BaseModel> list = getExpr(model)
                .orderBy(sortBy)
                .setFirstRow(page * pageSize)
                .setMaxRows(pageSize)
                .findList();
        ArrayList<WordEn> wordEns = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            WordEn wordEn = (WordEn) list.get(i);
            wordEns.addAll(loadFile(i + 1, wordEn.id, wordEn.letter, wordEn.word, wordEn.wordTrans));
        }
        ebeanServer.beginTransaction();
        try {
            for (WordEn wordEn : wordEns) {
                if (wordEn.id == null) {
                    ebeanServer.insert(wordEn);
                } else {
                    ebeanServer.update(wordEn);
                }
            }
            ebeanServer.commitTransaction();
        } catch (Exception e) {
            ebeanServer.rollbackTransaction();
        } finally {
            ebeanServer.endTransaction();
        }
    }

    private List<WordEn> loadFile(int index, Long id, String letter, String word, String wordTrans) {
        String filePath = "/Users/yibozheng/Desktop/高中/Untitled-" + index;
        List<WordEn> list = new ArrayList<>();
        try {
            boolean isFirst = true;
            List<String> fileWords = (List<String>) FileUtil.read(filePath);
            for (String fileWord : fileWords) {
                WordEn wordEn = new WordEn();
                String[] strs = fileWord.split("@");
                if (isFirst) {
                    wordEn.id = id;
                    isFirst = false;
                }
                wordEn.letter = letter;
                wordEn.word = word;
                wordEn.wordTrans = wordTrans;
                wordEn.sentence = strs[0];
                wordEn.sentenceTrans = strs[1];
                wordEn.status = true;
                wordEn.createTime = new Date();
                list.add(wordEn);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return list;
        }
    }

    public CompletionStage<String> load(WordEn modelRequest, String sortBy) {
        return supplyAsync(() -> {
            StringBuffer sb = new StringBuffer();
            int dictSize = 0;
            List<? extends BaseModel> list = loadFromDb(modelRequest, sortBy);
            sb.append("load ").append(list.size()).append(" words from DB");
            ArrayList words = new ArrayList();
            for (BaseModel wordEn: list) {
//                List temp = extractFromDict(wordEn);
//                words.addAll(temp);
//                dictSize = dictSize + temp.size();
                sb.append("extract ").append(dictSize).append(" sentences from dict");
            }
//            sb.append("save ").append(save2DB(words)).append(" word 2 DB");
            return sb.toString();
        }, executionContext);
    }

    private CompletionStage<List> extractFromDict(WordEn wordEn) {
        String url = "https://dict.cn/search";
        return ws.url(url).addQueryParameter("q", wordEn.word).get().thenApply(r -> r.getBody()).thenApply(v -> (analysisDict(v)));
    }

    private List analysisDict(String htmlContent) {
        return null;
    }

    private List<? extends BaseModel> loadFromDb(WordEn modelRequest, String sortBy) {
        return getExpr(modelRequest).orderBy(sortBy).findList();
    }

/*
    @Override
    public CompletionStage<Optional<Menu>> save(WordEn model) {
        return supplyAsync(() -> {
            Optional<WordEn> rtn = Optional.empty();
            if (model.id == null) {
                model.createTime = new Date();
                model.status = true;
                shiftParent(model);
                model.save();
                rtn = Optional.of(model);
            } else {
                WordEn modelTemp = (WordEn) getModel(model);
                if (modelTemp != null) {
                    model.updateTime = new Date();
                    model.modelCode = modelTemp.modelCode;
                    model.modelLevel = modelTemp.modelLevel;
                    model.modelOrder = Optional.ofNullable(model.modelOrder).orElse(modelTemp.modelOrder);
                    model.modelOrderSeq = modelTemp.parent.modelOrderSeq + "." + StringUtil.rounding(model.modelOrder, 3);
                    if (model.parent != null) {
                        shiftParent(model);
                    }
                    model.update();
                    resetChild(modelTemp, model, "Menu");
                    rtn = Optional.of(model);
                }
            }
            return rtn;
        }, executionContext);
    }

    public void shiftParent(WordEn model) {
        model.modelOrder = Optional.ofNullable(model.modelOrder).orElse(1);
        if (model.parent == null || model.parent.id == null) {
            model.modelLevel = 1;
            model.modelCodeSeq = model.modelCode;
            model.modelOrderSeq = StringUtil.rounding(model.modelOrder, 3);
        } else {
            WordEn parent = (WordEn) getModel(model.parent);
            if (parent != null) {
                model.modelLevel = parent.modelLevel + 1;
                model.modelCodeSeq = parent.modelCodeSeq + "." + model.modelCode;
                model.modelOrderSeq = parent.modelOrderSeq + "." + StringUtil.rounding(model.modelOrder, 3);
            }
        }
    }

    public void resetChild(WordEn modelFrom, WordEn modelTo, String modelClass) {
        System.out.println(modelTo.modelCodeSeq + ":" + modelFrom.modelCodeSeq);
        System.out.println(modelTo.modelLevel + ":" + modelFrom.modelLevel);
        System.out.println(modelTo.modelOrderSeq + ":" + modelFrom.modelOrderSeq);
        String sql = "UPDATE " + modelClass.toUpperCase() + " SET model_order_seq = REPLACE(model_order_seq, :modelOrderSeqFrom, :modelOrderSeqTo), MODEL_CODE_SEQ = REPLACE(MODEL_CODE_SEQ, :modelCodeSeqFrom, :modelCodeSeqTo), MODEL_LEVEL= MODEL_LEVEL + :modelLevel WHERE MODEL_CODE_SEQ LIKE :modelCodeSeqFromWhere";
        SqlUpdate update = Ebean.createSqlUpdate(sql);
        update.setParameter("modelCodeSeqFrom", modelFrom.modelCodeSeq + ".");
        update.setParameter("modelCodeSeqFromWhere", modelFrom.modelCodeSeq + ".%");
        update.setParameter("modelCodeSeqTo", modelTo.modelCodeSeq + ".");
        update.setParameter("modelOrderSeqFrom", modelFrom.modelOrderSeq + ".");
        update.setParameter("modelOrderSeqTo", modelTo.modelOrderSeq + ".");
        update.setParameter("modelLevel", modelTo.modelLevel - modelFrom.modelLevel);
        int modifiedCount = Ebean.execute(update);
        String msg = "There were " + modifiedCount + " rows updated";
    }
*/
}