package models.ecommerce.promotion;

import io.ebean.Finder;
import models.base.BasicModel;
import models.ecommerce.customer.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class RangeUser extends BasicModel {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT '+' COMMENT '黑白名单:[+ 白名单,- 黑名单]'")
    public String blackWhite;

    @ManyToOne
    public Activity activity;

//    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'U' COMMENT '类别:[U 用户,T 标签]'")
//    public String userType;

    @ManyToOne
    public User user;

    public static final Finder<Long, RangeUser> find = new Finder<>(RangeUser.class, "ecommerce");

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