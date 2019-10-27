package models.iplay.sale.promotion;

import io.ebean.Finder;
import models.base.BasicSimpleModel;
import models.iplay.merchandise.Merchandise;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class ActivityMerchandise extends BasicSimpleModel {

    private static final long serialVersionUID = 1L;

    public static final Finder<Long, ActivityMerchandise> find = new Finder<>(ActivityMerchandise.class, "default");

    @ManyToOne
    public Merchandise merchandise;

    @ManyToOne
    public Activity activity;
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