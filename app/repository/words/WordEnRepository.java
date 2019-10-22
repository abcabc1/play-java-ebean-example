package repository.words;

import io.ebean.Expr;
import io.ebean.ExpressionList;
import models.words.WordEn;
import play.db.ebean.EbeanConfig;
import repository.DatabaseExecutionContext;
import repository.base.BasicSimpleRepository;

import javax.inject.Inject;

public class WordEnRepository extends BasicSimpleRepository<WordEn> {

    @Inject
    public WordEnRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        super(ebeanConfig, executionContext, "words");
    }

    @Override
    public ExpressionList<WordEn> getExpr(WordEn model) {
        ExpressionList<WordEn> expressionList = getExpressionList(model);
        if (model.word != null) {
            expressionList.add(Expr.eq("word", model.word));
        }
        if (model.letter != null) {
            expressionList.add(Expr.eq("letter", model.letter));
        }
        return expressionList;
    }

    @Override
    public boolean isNullKey(WordEn model) {
        return model.word == null;
    }

    @Override
    public Object getKey(WordEn model) {
        return model.word;
    }
}