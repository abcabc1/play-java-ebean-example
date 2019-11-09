package models.iplay.sale.promotion;

import io.ebean.Finder;
import models.base.BasicSimpleModel;
import models.iplay.merchandise.Merchandise;
import models.iplay.merchandise.MerchandiseTag;
import scala.Char;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class ActivityMerchandise extends BasicSimpleModel {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT '+' COMMENT '黑白名单:[+ 白名单,- 黑名单]'")
    public String type;

    @ManyToOne
    public Activity activity;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'M' COMMENT '类别:[M 商品,T标签]'")
    public String merchandiseType;

    @ManyToOne
    public Merchandise merchandise;

    @ManyToOne
    public MerchandiseTag merchandiseTag;

    public static final Finder<Long, ActivityMerchandise> find = new Finder<>(ActivityMerchandise.class, "iplay");

/*
    @JsonBackReference(value = "operatorPass")
    @Column(nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '111111' COMMENT '密码'")
    public String operatorPass;
*/
        
 /*
    @ManyToOne
    @JoinColumn(name = "operator_gender")
    public Config operatorGender;

    @ManyToMany(mappedBy = "operators")
    @JsonIgnoreProperties(value = {"operators", "childs"})
    public List<Org> orgs;
 */

/*
    @ManyToMany
    @JoinTable(name = "org_operator")
    @JsonIgnoreProperties(value = {"roles", "orgs"})
    public List<Operator> operators;
*/
}