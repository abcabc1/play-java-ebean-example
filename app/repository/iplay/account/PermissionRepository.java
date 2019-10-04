package repository.iplay.account;

import io.ebean.ExpressionList;
import models.base.BaseModel;
import models.iplay.account.Permission;
import play.db.ebean.EbeanConfig;
import repository.BasicRepository;
import repository.DatabaseExecutionContext;

import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table
public class PermissionRepository extends BasicRepository<Permission> {

    @Inject
    public PermissionRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        super(ebeanConfig, executionContext);
    }

    @Override
    public ExpressionList<? extends BaseModel> getExpr(Permission model) {
        ExpressionList<? extends BaseModel> expressionList = getExpressionList(model);
        return expressionList;
    }
}