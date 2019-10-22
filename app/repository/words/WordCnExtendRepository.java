package repository.words;

import io.ebean.Expr;
import io.ebean.ExpressionList;
import models.words.WordCnExtend;
import play.db.ebean.EbeanConfig;
import repository.DatabaseExecutionContext;
import repository.base.BasicSimpleRepository;

import javax.inject.Inject;

public class WordCnExtendRepository extends BasicSimpleRepository<WordCnExtend> {

    @Inject
    public WordCnExtendRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        super(ebeanConfig, executionContext, "words");
    }

    @Override
    public boolean isNullKey(WordCnExtend model) {
        return model.pk == null || model.pk.word == null || model.pk.pinyin == null;
    }

    @Override
    public Object getKey(WordCnExtend model) {
        return model.pk;
    }

    @Override
    public ExpressionList<WordCnExtend> getExpr(WordCnExtend model) {
        ExpressionList<WordCnExtend> expressionList = getExpressionList(model);
        if (model.pk != null && model.pk.word != null) {
            expressionList.add(Expr.eq("pk.wordCn", model.pk.word));
        }
        if (model.pk != null && model.pk.pinyin != null) {
            expressionList.add(Expr.eq("pk.wordType", model.pk.pinyin));
        }
        return expressionList;
    }
}