package models.ecommerce.promotion.view;

import io.ebean.Finder;
import io.ebean.Model;
import models.ecommerce.merchandise.view.MerchandiseCodeView;
import models.ecommerce.user.view.CustomerCodeView;

import javax.persistence.ManyToOne;

public class ActivityRangeView extends Model {

    private static final long serialVersionUID = 1L;

    public boolean rangeCustomerBlackWhite;

    @ManyToOne
    public CustomerCodeView customerCodeView;

    public boolean rangeMerchandiseBlackWhite;

    @ManyToOne
    public MerchandiseCodeView merchandiseCodeView;

    public static final Finder<Long, ActivityRangeView> find = new Finder<>(ActivityRangeView.class, "default");

    public String toString() {
        return (rangeCustomerBlackWhite ? "+" : "-") + customerCodeView + "|" + (rangeMerchandiseBlackWhite ? "+" : "-") + merchandiseCodeView;
    }
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