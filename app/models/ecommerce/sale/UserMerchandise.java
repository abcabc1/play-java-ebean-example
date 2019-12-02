package models.ecommerce.sale;

import io.ebean.Finder;
import models.base.BasicModel;
import models.ecommerce.customer.User;
import models.ecommerce.merchandise.Merchandise;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/*
客户商品
 */
@Entity
@Table
public class UserMerchandise extends BasicModel {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "user")
    public User user;

    @ManyToOne
    @JoinColumn(name = "merchandise")
    public Merchandise merchandise;
    public static final Finder<Long, UserMerchandise> find = new Finder<>(UserMerchandise.class, "default");

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