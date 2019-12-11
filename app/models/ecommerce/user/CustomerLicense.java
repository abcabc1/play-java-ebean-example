package models.ecommerce.user;

import io.ebean.Finder;
import models.base.BasicModel;
import models.ecommerce.common.Config;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/*
客户证照
 */
@Entity
@Table
public class CustomerLicense extends BasicModel {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    public Customer customer;

    @ManyToOne
    public Config license;

    public static final Finder<Long, CustomerLicense> find = new Finder<>(CustomerLicense.class, "ecommerce");

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