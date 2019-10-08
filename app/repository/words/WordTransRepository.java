package repository.words;

import io.ebean.Expr;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import io.ebean.Transaction;
import models.base.BasicSimpleModel;
import models.words.WordTrans;
import play.db.ebean.EbeanConfig;
import repository.BasicSimpleRepository;
import repository.DatabaseExecutionContext;

import javax.inject.Inject;
import java.util.*;

public class WordTransRepository extends BasicSimpleRepository<WordTrans> {

    @Inject
    public WordTransRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        super(ebeanConfig, executionContext, "words");
    }

    @Override
    public ExpressionList<? extends BasicSimpleModel> getExpr(WordTrans model) {
        ExpressionList<? extends BasicSimpleModel> expressionList = getExpressionList(model);
        if (model.pk != null && model.pk.code != null) {
            expressionList.add(Expr.eq("pk.code", model.pk.code));
        }
        if (model.pk != null && model.pk.type != null) {
            expressionList.add(Expr.eq("pk.type", model.pk.type));
        }
        return expressionList;
    }

    public Optional<WordTrans> save(WordTrans model)
    {
        Optional<WordTrans> rtn = Optional.empty();
        if (model.pk == null || model.pk.code == null || model.pk.type == null) {
            model.createTime = new Date();
            model.status = true;
            model.insert();
            rtn = Optional.of(model);
        } else {
            if (get(model) != null) {
                model.updateTime = new Date();
                model.update();
                rtn = Optional.of(model);
            }
        }
        return rtn;
    }

    public Optional<WordTrans> get(WordTrans model) {
        return Optional.ofNullable(ebeanServer.find(model.getClass()).setId(model.pk).findOne());
    }

    @SuppressWarnings("unchecked")
    public PagedList<WordTrans> pagedList(WordTrans model, int page, int pageSize, String sortBy) {
        return (PagedList<WordTrans>) getExpr(model)
                .orderBy(sortBy)
                .setFirstRow(page * pageSize)
                .setMaxRows(pageSize)
                .findPagedList();
    }

    @SuppressWarnings("unchecked")
    public List<WordTrans> list(WordTrans model, String sortBy) {
        return (List<WordTrans>) getExpr(model).orderBy(sortBy).findList();
    }
}