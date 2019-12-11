package models.ecommerce.promotion;

import io.ebean.Finder;
import models.base.BasicModel;

import javax.persistence.*;
import java.util.List;

/*
活动集合
 */
@Entity
@Table
public class ActivityRange extends BasicModel {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    public Activity activity;

    @Column(nullable = false, columnDefinition = "TINYINT DEFAULT -1 COMMENT '客户黑白名单:[+ 白名单, - 黑名单]'")
    public boolean rangeCustomerBlackWhite;

    @Column(nullable = false, columnDefinition = "TINYINT DEFAULT -1 COMMENT '商品黑白名单:[+ 白名单, - 黑名单]'")
    public boolean rangeMerchandiseBlackWhite;

    @OneToMany
    public List<ActivityRangeMerchandise> activityRangeMerchandiseList;

    @OneToMany
    public List<ActivityRangeCustomer> activityRangeCustomerList;

    public static final Finder<Long, ActivityRange> find = new Finder<>(ActivityRange.class, "default");

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