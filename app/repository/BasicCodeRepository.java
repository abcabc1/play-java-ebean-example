package repository;

import io.ebean.*;
import models.base.BasicCodeModel;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public abstract class BasicCodeRepository<T extends BasicCodeModel> {

    protected final EbeanServer ebeanServer;
    protected final DatabaseExecutionContext executionContext;

    @Inject
    public BasicCodeRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext, String serverName) {
        this.ebeanServer = Ebean.getServer(serverName == null ? ebeanConfig.defaultServer() : serverName);
        this.executionContext = executionContext;
    }

    @Inject
    public BasicCodeRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public CompletionStage<Optional<? extends BasicCodeModel>> getAsync(T model) {
        return supplyAsync(() -> get(model), executionContext);
    }

    public Optional<? extends BasicCodeModel> get(T model) {
        return Optional.ofNullable(ebeanServer.find(model.getClass()).setId(model.code).findOne());
    }

    public CompletionStage<Optional<T>> saveAsync(T model) {
        return supplyAsync(() -> save(model), executionContext);
    }

    private Optional<T> save(T model) {
        Optional<T> rtn = Optional.empty();
        if (model.code == null) {
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

    public CompletionStage<Integer> saveAllAsync(List<T> models) {
        return supplyAsync(() -> saveAll(models), executionContext);
    }

    public Integer saveAll(List<T> models) {
        Transaction txn = ebeanServer.beginTransaction();
        return ebeanServer.saveAll(models, txn);
    }

    public CompletionStage<Integer> saveBathAsync(List<T> models) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            try {
                for (int i = 0; i < models.size(); i++) {
                    save(models.get(i));
                }
                txn.commit();
            } finally {
                txn.end();
            }
            return models.size();
        }, executionContext);
    }

    public CompletionStage<PagedList<? extends BasicCodeModel>> pagedListAsync(T model, int page, int pageSize, String sortBy) {
        return supplyAsync(() -> pagedList(model, page, pageSize, sortBy), executionContext);
    }

    public PagedList<? extends BasicCodeModel> pagedList(T model, int page, int pageSize, String sortBy) {
        return getExpr(model)
                .orderBy(sortBy)
                .setFirstRow(page * pageSize)
                .setMaxRows(pageSize)
                .findPagedList();
    }

    public CompletionStage<List<? extends BasicCodeModel>> listAsync(T model, String sortBy) {
        return supplyAsync(() -> list(model, sortBy), executionContext);
    }

    public List<? extends BasicCodeModel> list(T model, String sortBy) {
        return getExpr(model).orderBy(sortBy).findList();
    }

    public CompletionStage<Optional<String>> removeAsync(T model) {
        return supplyAsync(() -> remove(model), executionContext);
    }

    public Optional<String> remove(T model) {
        final Optional<? extends BasicCodeModel> modelOptional = Optional.ofNullable(ebeanServer.find(model.getClass()).setId(model.code).findOne());
        modelOptional.ifPresent(Model::delete);
        return modelOptional.map(c -> c.code);
    }

    public abstract ExpressionList<? extends BasicCodeModel> getExpr(T model);

    public ExpressionList<? extends BasicCodeModel> getExpressionList(T model) {
        ExpressionList<? extends BasicCodeModel> expressionList = ebeanServer.find(model.getClass()).where();
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
