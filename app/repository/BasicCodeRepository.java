package repository;

import io.ebean.*;
import models.base.BaseCodeModel;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public abstract class BasicCodeRepository<T extends BaseCodeModel> {

    protected final EbeanServer ebeanServer;
    protected final DatabaseExecutionContext executionContext;

    @Inject
    public BasicCodeRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public CompletionStage<Optional<? extends BaseCodeModel>> get(T model) {
        return supplyAsync(() -> Optional.ofNullable(getModel(model)), executionContext);
    }

    public BaseCodeModel getModel(T model) {
        return ebeanServer.find(model.getClass()).setId(model.code).findOne();
    }

    public CompletionStage<Integer> saveBath(List<T> models) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            try {
                for (int i = 0; i < models.size(); i++) {
                    saveModel(models.get(i));
                }
                txn.commit();
            }
            finally {
                txn.end();
            }
            return models.size();
        }, executionContext);
    }

    private Optional<T> saveModel(T model) {
        Optional<T> rtn = Optional.empty();
        if (model.code == null) {
            model.createTime = new Date();
            model.status = true;
            model.insert();
            rtn = Optional.of(model);
        } else {
            if (getModel(model) != null) {
                model.updateTime = new Date();
                model.update();
                rtn = Optional.of(model);
            }
        }
        return rtn;
    }

    public CompletionStage<Optional<T>> save(T model) {
        return supplyAsync(() -> saveModel(model), executionContext);
    }

    public CompletionStage<PagedList<? extends BaseCodeModel>> pagedList(T model, int page, int pageSize, String sortBy) {
        return supplyAsync(() ->
                getExpr(model)
                        .orderBy(sortBy)
                        .setFirstRow(page * pageSize)
                        .setMaxRows(pageSize)
                        .findPagedList(), executionContext);
    }

    public CompletionStage<List<? extends BaseCodeModel>> list(T model, String sortBy) {
        return supplyAsync(() -> getExpr(model).orderBy(sortBy).findList(), executionContext);
    }

    public CompletionStage<Optional<String>> remove(T model) {
        return supplyAsync(() -> {
            final Optional<? extends BaseCodeModel> modelOptional = Optional.ofNullable(ebeanServer.find(model.getClass()).setId(model.code).findOne());
            modelOptional.ifPresent(Model::delete);
            return modelOptional.map(c -> c.code);
        }, executionContext);
    }

    public abstract ExpressionList<? extends BaseCodeModel> getExpr(T model) ;

    public ExpressionList<? extends BaseCodeModel> getExpressionList(T model) {
        ExpressionList<? extends BaseCodeModel> expressionList = ebeanServer.find(model.getClass()).where();
        model.status = Optional.ofNullable(model.status).orElse(true);
        if (model.code != null) {
            expressionList.add(Expr.eq("code", model.code));
        }
        if (model.timeFrom != null && model.timeTo != null) {
            expressionList.add(Expr.between("createTime", model.timeFrom, model.timeTo));
        }
        if (model.status != null) {
            expressionList.add(Expr.eq("status", model.status));
        }
        return expressionList;
    }
}
