package repository.words;

import io.ebean.Expr;
import io.ebean.ExpressionList;
import models.base.BasicSimpleModel;
import models.words.WordTrans;
import play.db.ebean.EbeanConfig;
import repository.DatabaseExecutionContext;
import repository.base.BasicSimpleRepository;

import javax.inject.Inject;

public class WordTransRepository extends BasicSimpleRepository<WordTrans> {

    @Inject
    public WordTransRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        super(ebeanConfig, executionContext, "words");
    }

    @Override
    public boolean isNullKey(WordTrans model) {
        return model.pk == null || model.pk.wordEn == null || model.pk.wordType == null;
    }

    @Override
    public Object getKey(WordTrans model) {
        return model.pk;
    }

    @Override
    public ExpressionList<WordTrans> getExpr(WordTrans model) {
        ExpressionList<WordTrans> expressionList = getExpressionList(model);
        if (model.pk != null && model.pk.wordEn != null) {
            expressionList.add(Expr.eq("pk.wordEn", model.pk.wordEn));
        }
        if (model.pk != null && model.pk.wordType != null) {
            expressionList.add(Expr.eq("pk.wordType", model.pk.wordType));
        }
        return expressionList;
    }
}