package repository.iplay.goods;

import io.ebean.Expr;
import io.ebean.ExpressionList;
import models.base.BaseModel;
import models.iplay.goods.Merchandise;
import play.db.ebean.EbeanConfig;
import repository.BasicRepository;
import repository.DatabaseExecutionContext;

import javax.inject.Inject;

public class MerchandiseRepository extends BasicRepository<Merchandise> {

    @Inject
    public MerchandiseRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        super(ebeanConfig, executionContext);
    }

    @Override
    public ExpressionList<? extends BaseModel> getExpr(Merchandise model) {
        ExpressionList<? extends BaseModel> expressionList = getExpressionList(model);
        if (model.id != null) {
            expressionList.add(Expr.eq("id", model.id));
        }
        return expressionList;
    }
    
/*
    @Override
    public CompletionStage<Optional<Menu>> save(Merchandise model) {
        return supplyAsync(() -> {
            Optional<Merchandise> rtn = Optional.empty();
            if (model.id == null) {
                model.createTime = new Date();
                model.status = true;
                shiftParent(model);
                model.save();
                rtn = Optional.of(model);
            } else {
                Merchandise modelTemp = (Merchandise) getModel(model);
                if (modelTemp != null) {
                    model.updateTime = new Date();
                    model.modelCode = modelTemp.modelCode;
                    model.modelLevel = modelTemp.modelLevel;
                    model.modelOrder = Optional.ofNullable(model.modelOrder).orElse(modelTemp.modelOrder);
                    model.modelOrderSeq = modelTemp.parent.modelOrderSeq + "." + StringUtil.rounding(model.modelOrder, 3);
                    if (model.parent != null) {
                        shiftParent(model);
                    }
                    model.update();
                    resetChild(modelTemp, model, "Menu");
                    rtn = Optional.of(model);
                }
            }
            return rtn;
        }, executionContext);
    }

    public void shiftParent(Merchandise model) {
        model.modelOrder = Optional.ofNullable(model.modelOrder).orElse(1);
        if (model.parent == null || model.parent.id == null) {
            model.modelLevel = 1;
            model.modelCodeSeq = model.modelCode;
            model.modelOrderSeq = StringUtil.rounding(model.modelOrder, 3);
        } else {
            Merchandise parent = (Merchandise) getModel(model.parent);
            if (parent != null) {
                model.modelLevel = parent.modelLevel + 1;
                model.modelCodeSeq = parent.modelCodeSeq + "." + model.modelCode;
                model.modelOrderSeq = parent.modelOrderSeq + "." + StringUtil.rounding(model.modelOrder, 3);
            }
        }
    }

    public void resetChild(Merchandise modelFrom, Merchandise modelTo, String modelClass) {
        System.out.println(modelTo.modelCodeSeq + ":" + modelFrom.modelCodeSeq);
        System.out.println(modelTo.modelLevel + ":" + modelFrom.modelLevel);
        System.out.println(modelTo.modelOrderSeq + ":" + modelFrom.modelOrderSeq);
        String sql = "UPDATE " + modelClass.toUpperCase() + " SET model_order_seq = REPLACE(model_order_seq, :modelOrderSeqFrom, :modelOrderSeqTo), MODEL_CODE_SEQ = REPLACE(MODEL_CODE_SEQ, :modelCodeSeqFrom, :modelCodeSeqTo), MODEL_LEVEL= MODEL_LEVEL + :modelLevel WHERE MODEL_CODE_SEQ LIKE :modelCodeSeqFromWhere";
        SqlUpdate update = Ebean.createSqlUpdate(sql);
        update.setParameter("modelCodeSeqFrom", modelFrom.modelCodeSeq + ".");
        update.setParameter("modelCodeSeqFromWhere", modelFrom.modelCodeSeq + ".%");
        update.setParameter("modelCodeSeqTo", modelTo.modelCodeSeq + ".");
        update.setParameter("modelOrderSeqFrom", modelFrom.modelOrderSeq + ".");
        update.setParameter("modelOrderSeqTo", modelTo.modelOrderSeq + ".");
        update.setParameter("modelLevel", modelTo.modelLevel - modelFrom.modelLevel);
        int modifiedCount = Ebean.execute(update);
        String msg = "There were " + modifiedCount + " rows updated";
    }
*/
}