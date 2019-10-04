package repository.iplay.account;

import io.ebean.Expr;
import io.ebean.ExpressionList;
import models.base.BaseModel;
import models.iplay.account.Role;
import play.db.ebean.EbeanConfig;
import repository.BasicRepository;
import repository.DatabaseExecutionContext;

import javax.inject.Inject;

public class RoleRepository extends BasicRepository<Role> {

    @Inject
    public RoleRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        super(ebeanConfig, executionContext);
    }

    @Override
    public ExpressionList<? extends BaseModel> getExpr(Role model) {
        ExpressionList<? extends BaseModel> expressionList = getExpressionList(model);
        if (model.status != null) {
            expressionList.add(Expr.eq("status", model.status));
        }
        return expressionList;
    }
}
