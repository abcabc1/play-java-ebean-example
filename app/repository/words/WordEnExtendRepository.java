package repository.words;

import io.ebean.Expr;
import io.ebean.ExpressionList;
import models.words.WordEnExtend;
import play.db.ebean.EbeanConfig;
import repository.DatabaseExecutionContext;
import repository.base.BasicSimpleRepository;

import javax.inject.Inject;

public class WordTransRepository extends BasicSimpleRepository<WordEnExtend> {

    @Inject
    public WordTransRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        super(ebeanConfig, executionContext, "words");
    }

    @Override
    public boolean isNullKey(WordEnExtend model) {
        return model.pk == null || model.pk.word == null || model.pk.type == null;
    }

    @Override
    public Object getKey(WordEnExtend model) {
        return model.pk;
    }

    @Override
    public ExpressionList<WordEnExtend> getExpr(WordEnExtend model) {
        ExpressionList<WordEnExtend> expressionList = getExpressionList(model);
        if (model.pk != null && model.pk.word != null) {
            expressionList.add(Expr.eq("pk.word", model.pk.word));
        }
        if (model.pk != null && model.pk.type != null) {
            expressionList.add(Expr.eq("pk.type", model.pk.type));
        }
        return expressionList;
    }
}