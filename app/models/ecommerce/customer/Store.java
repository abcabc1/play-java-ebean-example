package models.ecommerce.customer;

import io.ebean.Finder;
import models.base.BasicModel;
import models.ecommerce.account.Operator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class Store extends BasicModel {

    private static final long serialVersionUID = 1L;

    @OneToOne
    public Operator operator;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '编码'")
    public String code;

    @Column(nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '名称'")
    public String name;

    @Column(nullable = false, columnDefinition = "VARCHAR(6) DEFAULT '' COMMENT '区域编码'")
    public String area;

    public static final Finder<Long, Store> find = new Finder<>(Store.class, "ecommerce");

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