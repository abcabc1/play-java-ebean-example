package repository.words;

import io.ebean.Expr;
import io.ebean.ExpressionList;
import models.words.CnWordPy;
import models.words.WordTrans;
import play.db.ebean.EbeanConfig;
import repository.DatabaseExecutionContext;
import repository.base.BasicSimpleRepository;

import javax.inject.Inject;

public class CnWordPyRepository extends BasicSimpleRepository<CnWordPy> {

    @Inject
    public CnWordPyRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        super(ebeanConfig, executionContext, "words");
    }

    @Override
    public boolean isNullKey(CnWordPy model) {
        return model.pk == null || model.pk.wordCn == null || model.pk.wordPy == null;
    }

    @Override
    public Object getKey(CnWordPy model) {
        return model.pk;
    }

    @Override
    public ExpressionList<CnWordPy> getExpr(CnWordPy model) {
        ExpressionList<CnWordPy> expressionList = getExpressionList(model);
        if (model.pk != null && model.pk.wordCn != null) {
            expressionList.add(Expr.eq("pk.wordCn", model.pk.wordCn));
        }
        if (model.pk != null && model.pk.wordPy != null) {
            expressionList.add(Expr.eq("pk.wordType", model.pk.wordPy));
        }
        return expressionList;
    }
}