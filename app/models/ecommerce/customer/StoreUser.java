package models.ecommerce.customer;

import io.ebean.Finder;
import models.base.BasicModel;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/*
商户客户
 */
@Entity
@Table
public class StoreUser extends BasicModel {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    public Store store;

    @ManyToOne
    public User user;

    public static final Finder<Long, StoreUser> find = new Finder<>(StoreUser.class, "ecommerce");

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