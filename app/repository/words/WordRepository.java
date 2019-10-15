package repository.words;

import io.ebean.Expr;
import io.ebean.ExpressionList;
import models.base.BasicSimpleModel;
import models.words.Word;
import play.db.ebean.EbeanConfig;
import repository.DatabaseExecutionContext;
import repository.base.BasicSimpleRepository;

import javax.inject.Inject;

public class WordRepository extends BasicSimpleRepository<Word> {

    @Inject
    public WordRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        super(ebeanConfig, executionContext, "words");
    }

    @Override
    public ExpressionList<Word> getExpr(Word model) {
        ExpressionList<Word> expressionList = getExpressionList(model);
        if (model.wordEn != null) {
            expressionList.add(Expr.eq("wordEn", model.wordEn));
        }
        if (model.wordLetter != null) {
            expressionList.add(Expr.eq("wordLetter", model.wordLetter));
        }
        return expressionList;
    }

    @Override
    public boolean isNullKey(Word model) {
        return model.wordEn == null;
    }

    @Override
    public Object getKey(Word model) {
        return model.wordEn;
    }
}