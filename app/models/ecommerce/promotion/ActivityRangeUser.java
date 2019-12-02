package models.ecommerce.promotion;

import io.ebean.Finder;
import models.base.BasicModel;
import models.ecommerce.customer.User;
import models.ecommerce.customer.UserArea;
import models.ecommerce.customer.UserCategory;
import models.ecommerce.customer.UserTag;

import javax.persistence.*;

/*
活动用户集合
 */
@Entity
@Table
public class ActivityRangeUser extends BasicModel {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    public ActivityRange activityRange;

    @Column(nullable = false, columnDefinition = "CHAR(2) DEFAULT 'UU' COMMENT '类别:[UU 用户, UT 标签, UA 区域, UC 类型]'")
    public String userType;

    @ManyToOne
    public User user;

    @ManyToOne
    public UserTag userTag;

    @ManyToOne
    public UserCategory userCategory;

    @ManyToOne
    public UserArea userArea;

    @Transient
    public String code;

    public static final Finder<Long, ActivityRangeUser> find = new Finder<>(ActivityRangeUser.class, "ecommerce");

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