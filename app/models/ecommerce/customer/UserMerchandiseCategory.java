package models.ecommerce.customer;

import io.ebean.Finder;
import models.base.BasicModel;
import models.ecommerce.common.Config;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class UserMerchandiseCategory extends BasicModel {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    public User user;

    @ManyToOne
    @JoinColumn(name = "merchandise_category")
    public Config merchandiseCategory;

    public static final Finder<Long, UserMerchandiseCategory> find = new Finder<>(UserMerchandiseCategory.class, "default");

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