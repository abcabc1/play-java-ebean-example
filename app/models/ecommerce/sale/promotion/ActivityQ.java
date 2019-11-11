package models.ecommerce.sale.promotion;

import io.ebean.Finder;
import models.base.BasicModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class ActivityQ extends BasicModel {

    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '编码'")
    public String code;

    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '名称'")
    public String name;

    @OneToOne
    public Activity activity;

    public static final Finder<Long, ActivityQ> find = new Finder<>(ActivityQ.class, "iplay");

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