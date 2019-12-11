package models.ecommerce.promotion;

import io.ebean.Finder;
import models.base.BasicModel;
import models.ecommerce.merchandise.Merchandise;
import models.ecommerce.merchandise.MerchandiseTag;
import models.ecommerce.sale.StoreMerchandise;

import javax.persistence.*;

/*
活动商品集合
 */
@Entity
@Table
public class ActivityRangeMerchandise extends BasicModel {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    public ActivityRange activityRange;

    @Column(nullable = false, columnDefinition = "CHAR(2) DEFAULT 'MM' COMMENT '类别:[MM 商品, MT 标签, MC 类别]'")
    public String merchandiseType;

    @ManyToOne
    public Merchandise merchandise;

    @ManyToOne
    public MerchandiseTag merchandiseTag;

    @ManyToOne
    public StoreMerchandise storeMerchandise;

    @Transient
    public Long merchandiseId;

    public static final Finder<Long, ActivityRangeMerchandise> find = new Finder<>(ActivityRangeMerchandise.class, "default");

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