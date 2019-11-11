package models.ecommerce.sale.promotion;

import io.ebean.Finder;
import models.base.BasicModel;
import models.ecommerce.customer.User;
import models.ecommerce.customer.UserTag;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class ActivityUser extends BasicModel {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT '+' COMMENT '黑白名单:[+ 白名单,- 黑名单]'")
    public String type;

    @ManyToOne
    public Activity activity;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'U' COMMENT '类别:[U 用户,T 标签]'")
    public String userType;

    @ManyToOne
    public User user;

    @ManyToOne
    public UserTag userTag;

    public static final Finder<Long, ActivityUser> find = new Finder<>(ActivityUser.class, "iplay");

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