package repository.iplay.account;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.SqlUpdate;
import io.ebean.Transaction;
import models.iplay.account.Endpoint;
import play.db.ebean.EbeanConfig;
import repository.DatabaseExecutionContext;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class EndpointRepository {

    protected final EbeanServer ebeanServer;
    protected final DatabaseExecutionContext executionContext;

    @Inject
    public EndpointRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public CompletionStage<Integer> saveBath(List<Endpoint> models) {
        return supplyAsync(() -> {
            Transaction txn = ebeanServer.beginTransaction();
            try {
                for (int i = 0; i < models.size(); i++) {
                    saveModel(models.get(i));
                }
                txn.commit();
            } finally {
                txn.end();
            }
            return models.size();
        }, executionContext);
    }

    private void saveBatchModel(List<Endpoint> models) {
        for (int i = 0; i < models.size(); i++) {
            saveModel(models.get(i));
        }
    }

    private Optional<Endpoint> saveModel(Endpoint model) {
        Optional<Endpoint> rtn = Optional.empty();
        if (model.pathPattern == null) {
            return rtn;
        }
        if (getModel(model) == null) {
            model.insert();
        } else {
            model.update();
        }
        return Optional.of(model);
    }

    public CompletionStage<Optional<Endpoint>> save(Endpoint model) {
        return supplyAsync(() -> saveModel(model), executionContext);
    }

    public Endpoint getModel(Endpoint model) {
        if (model.pathPattern == null) {
            return null;
        }
        return ebeanServer.find(model.getClass()).setId(model.pathPattern).findOne();
    }

    public void removeAll(String modelClass) {
        String sql = "DELETE FROM " + modelClass.toLowerCase();
        SqlUpdate update = Ebean.createSqlUpdate(sql);
        int modifiedCount = Ebean.execute(update);
        String msg = "There were " + modifiedCount + " rows removed";
    }

    public Integer refresh(List<Endpoint> models) {
        Transaction txn = ebeanServer.beginTransaction();
        txn.setBatchMode(true);
        txn.setBatchSize(100);
        try {
            removeAll("Endpoint");
            saveBatchModel(models);
//            Endpoint endpoint = new Endpoint();
//            save(endpoint);
            txn.commit();
        } finally {
            txn.end();
        }
        return models.size();
    }
}