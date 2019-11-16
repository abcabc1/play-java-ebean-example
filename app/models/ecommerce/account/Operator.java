package models.ecommerce.account;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.ebean.Finder;
import models.base.BasicModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class Operator extends BasicModel {

    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '编码'")
    public String code;

    @JsonBackReference(value = "pass")
    @Column(nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '111111' COMMENT '密码'")
    public String pass;

    @Column(nullable = false, columnDefinition = "TINYINT UNSIGNED DEFAULT 1  COMMENT '类别[0 admin, 1 user, 2 store]'")
    public Integer type;

    public static final Finder<Long, Operator> find = new Finder<>(Operator.class, "ecommerce");

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