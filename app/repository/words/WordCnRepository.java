package repository.words;

import io.ebean.Expr;
import io.ebean.ExpressionList;
import models.words.WordCn;
import play.db.ebean.EbeanConfig;
import repository.DatabaseExecutionContext;
import repository.base.BasicSimpleRepository;

import javax.inject.Inject;

public class CnWordRepository extends BasicSimpleRepository<WordCn> {

    @Inject
    public CnWordRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        super(ebeanConfig, executionContext, "words");
    }

    @Override
    public ExpressionList<WordCn> getExpr(WordCn model) {
        ExpressionList<WordCn> expressionList = getExpressionList(model);
        if (model.timeFrom != null && model.timeTo == null) {
            expressionList.add(Expr.ge("createTime", model.timeFrom));
        }
        if (model.timeFrom == null && model.timeTo != null) {
            expressionList.add(Expr.le("createTime", model.timeTo));
        }
        if (model.timeFrom != null && model.timeTo != null) {
            expressionList.add(Expr.between("createTime", model.timeFrom, model.timeTo));
        }
        if (model.word != null) {
            expressionList.add(Expr.eq("word", model.word));
        }
        return expressionList;
    }

    @Override
    public boolean isNullKey(WordCn model) {
        return model.word == null;
    }

    @Override
    public Object getKey(WordCn model) {
        return model.word;
    }
    
/*
    @Override
    public CompletionStage<Optional<Menu>> save(WordPy model) {
        return supplyAsync(() -> {
            Optional<WordPy> rtn = Optional.empty();
            if (model.id == null) {
                model.createTime = new Date();
                model.status = true;
                shiftParent(model);
                model.save();
                rtn = Optional.of(model);
            } else {
                WordPy modelTemp = (WordPy) getModel(model);
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

    public void shiftParent(WordPy model) {
        model.modelOrder = Optional.ofNullable(model.modelOrder).orElse(1);
        if (model.parent == null || model.parent.id == null) {
            model.modelLevel = 1;
            model.modelCodeSeq = model.modelCode;
            model.modelOrderSeq = StringUtil.rounding(model.modelOrder, 3);
        } else {
            WordPy parent = (WordPy) getModel(model.parent);
            if (parent != null) {
                model.modelLevel = parent.modelLevel + 1;
                model.modelCodeSeq = parent.modelCodeSeq + "." + model.modelCode;
                model.modelOrderSeq = parent.modelOrderSeq + "." + StringUtil.rounding(model.modelOrder, 3);
            }
        }
    }

    public void resetChild(WordPy modelFrom, WordPy modelTo, String modelClass) {
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