package repository.words;

import io.ebean.Expr;
import io.ebean.ExpressionList;
import models.base.BasicCodeModel;
import models.words.Word;
import play.db.ebean.EbeanConfig;
import repository.BasicCodeRepository;
import repository.DatabaseExecutionContext;

import javax.inject.Inject;

public class WordRepository extends BasicCodeRepository<Word> {

    @Inject
    public WordRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        super(ebeanConfig, executionContext, "words");
    }

    @Override
    public ExpressionList<? extends BasicCodeModel> getExpr(Word model) {
        ExpressionList<? extends BasicCodeModel> expressionList = getExpressionList(model);
        if (model.code != null) {
            expressionList.add(Expr.eq("code", model.code));
        }
        if (model.letter != null) {
            expressionList.add(Expr.eq("letter", model.letter));
        }
        return expressionList;
    }
}