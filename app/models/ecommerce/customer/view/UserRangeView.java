package models.ecommerce.customer.view;

import io.ebean.Model;

/*
客户集合
 */
public class UserRangeView extends Model {

    private static final long serialVersionUID = 1L;

    public Long userId;
    public Long userTagId;
    public Long userAreaId;
    public Long userCategoryId;

    public String toString(){
        return new StringBuilder().append("userId: ").append(userId).append(", userTagId: ").append(userTagId).append(", userAreaId: ").append(userAreaId).append(", userCategoryId: ").append(userCategoryId).toString();
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