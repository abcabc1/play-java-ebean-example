package models.ecommerce.promotion;

import io.ebean.Finder;
import models.base.BasicModel;
import models.base.BasicSimpleModel;

import javax.persistence.*;

@Entity
@Table
public class ActivityMj extends BasicSimpleModel {

    private static final long serialVersionUID = 1L;

    @OneToOne
    public Activity activity;

    public static final Finder<Long, ActivityMj> find = new Finder<>(ActivityMj.class, "ecommerce");

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