package models.ecommerce.promotion;

import io.ebean.Finder;
import models.base.BasicModel;
import models.ecommerce.common.Config;
import models.ecommerce.merchandise.Merchandise;
import models.ecommerce.merchandise.MerchandiseTag;

import javax.persistence.*;

@Entity
@Table
public class RangeMerchandise extends BasicModel {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT '+' COMMENT '黑白名单:[+ 白名单, - 黑名单]'")
    public String blackWhite;

    @ManyToOne
    public Activity activity;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'MM' COMMENT '类别:[MM 商品, MG 标签]'")
    public String merchandiseType;

    @ManyToOne
    public Merchandise merchandise;

    @ManyToOne
    @JoinColumn(name = "tag")
    public Config tag;

    public static final Finder<Long, RangeMerchandise> find = new Finder<>(RangeMerchandise.class, "ecommerce");

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