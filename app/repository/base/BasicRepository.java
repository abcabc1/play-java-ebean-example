package repository.base;

import io.ebean.*;
import models.base.BasicModel;
import play.db.ebean.EbeanConfig;
import repository.DatabaseExecutionContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public abstract class BasicRepository<T extends BasicModel> {

    protected final EbeanServer ebeanServer;
    protected final DatabaseExecutionContext executionContext;

    @Inject
    public BasicRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext, String serverName) {
        this.ebeanServer = Ebean.getServer(serverName == null ? ebeanConfig.defaultServer() : serverName);
        this.executionContext = executionContext;
    }

    @Inject
    public BasicRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public CompletionStage<Optional<? extends BasicModel>> getAsync(T model) {
        return supplyAsync(() -> get(model), executionContext);
    }

    @SuppressWarnings("unchecked")
    public Optional<T> get(T model) {
        return (Optional<T>) ebeanServer.find(model.getClass()).setId(model.id).findOneOrEmpty();
    }

    public CompletionStage<Optional<T>> saveAsync(T model) {
        return supplyAsync(() -> save(model), executionContext);
    }

    private Optional<T> save(T model) {
        Optional<T> rtn = Optional.empty();
        if (model.id == null) {
            model.createTime = new Date();
            model.status = true;
            ebeanServer.insert(model);
            rtn = Optional.of(model);
        } else {
            if (get(model) != null) {
                model.updateTime = new Date();
                ebeanServer.update(model);
                rtn = Optional.of(model);
            }
        }
        return rtn;
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

    public CompletionStage<Integer> saveAllAsync(Collection<T> models) {
        return supplyAsync(() -> saveAll(models), executionContext);
    }

    public Integer saveAll(Collection<T> models) {
        Transaction txn = ebeanServer.beginTransaction();
        Integer num = 0;
        try {
            num = ebeanServer.saveAll(models, txn);
            txn.commit();
        } finally {
            txn.close();
        }
        return num;
    }

    public CompletionStage<PagedList<T>> pagedListAsync(T model, int page, int pageSize, String sortBy) {
        return supplyAsync(() -> pagedList(model, page, pageSize, sortBy), executionContext);
    }

    public PagedList<T> pagedList(T model, int page, int pageSize, String sortBy) {
        return getExpr(model)
                .orderBy(sortBy)
                .setFirstRow(page * pageSize)
                .setMaxRows(pageSize)
                .findPagedList();
    }

    public CompletionStage<List<T>> listAsync(T model, String sortBy) {
        return supplyAsync(() -> list(model, sortBy), executionContext);
    }

    public List<T> list(T model, String sortBy) {
        return getExpr(model).orderBy(sortBy).findList();
    }

    public CompletionStage<Optional<Long>> removeAsync(T model) {
        return supplyAsync(() -> remove(model), executionContext);
    }

    public Optional<Long> remove(T model) {
        final Optional<T> modelOptional = get(model);
        modelOptional.ifPresent(Model::delete);
        return modelOptional.map(c -> c.id);
    }

    public abstract ExpressionList<T> getExpr(T model);

    @SuppressWarnings("unchecked")
    public ExpressionList<T> getExpressionList(T model) {
        ExpressionList<T> expressionList = (ExpressionList<T>) ebeanServer.find(model.getClass()).where();
        model.status = Optional.ofNullable(model.status).orElse(true);
        if (model.id != null) {
            expressionList.add(Expr.eq("id", model.id));
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
