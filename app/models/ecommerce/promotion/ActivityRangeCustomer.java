package models.ecommerce.promotion;

import io.ebean.Finder;
import models.base.BasicModel;
import models.ecommerce.user.Customer;
import models.ecommerce.user.CustomerArea;
import models.ecommerce.user.CustomerCategory;
import models.ecommerce.user.CustomerTag;

import javax.persistence.*;

/*
活动用户集合
 */
@Entity
@Table
public class ActivityRangeCustomer extends BasicModel {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    public ActivityRange activityRange;

    @Column(nullable = false, columnDefinition = "CHAR(2) DEFAULT 'UU' COMMENT '类别:[UU 用户, UT 标签, UA 区域, UC 类型]'")
    public String customerType;

    @ManyToOne
    public Customer customer;

    @ManyToOne
    public CustomerTag customerTag;

    @ManyToOne
    public CustomerCategory customerCategory;

    @ManyToOne
    public CustomerArea customerArea;

    @Transient
    public Long customerId;

    public static final Finder<Long, ActivityRangeCustomer> find = new Finder<>(ActivityRangeCustomer.class, "ecommerce");

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