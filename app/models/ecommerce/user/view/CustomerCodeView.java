package models.ecommerce.user.view;

import io.ebean.Model;

/*
客户集合
 */
public class CustomerCodeView extends Model {

    private static final long serialVersionUID = 1L;

    public long customerId;
    public long customerTagId;
    public long customerAreaId;
    public long customerCategoryId;
    public long id;

    public String toString() {
        return new StringBuilder().append("customerId: ").append(customerId).append(", customerTagId: ").append(customerTagId).append(", customerAreaId: ").append(customerAreaId).append(", customerCategoryId: ").append(customerCategoryId).toString();
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