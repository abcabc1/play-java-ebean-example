package models.ecommerce.customer;

import io.ebean.Finder;
import models.base.BasicModel;
import models.ecommerce.account.Operator;
import models.ecommerce.common.Config;

import javax.persistence.*;

/*
客户
 */
@Entity
@Table
public class User extends BasicModel {

    private static final long serialVersionUID = 1L;

    @OneToOne
    public Operator operator;

    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '名称'")
    public String name;

    @ManyToOne
    @JoinColumn(name = "level")
    public Config level;

    public static final Finder<Long, User> find = new Finder<>(User.class, "ecommerce");

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