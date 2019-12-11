package models.ecommerce.user;

import io.ebean.Finder;
import models.base.BasicModel;
import models.ecommerce.common.Config;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/*
客户标签
 */
@Entity
@Table
public class CustomerTag extends BasicModel {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    public Customer customer;

    @ManyToOne
    @JoinColumn(name = "tag")
    public Config tag;

    public static final Finder<Long, CustomerTag> find = new Finder<>(CustomerTag.class, "ecommerce");

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