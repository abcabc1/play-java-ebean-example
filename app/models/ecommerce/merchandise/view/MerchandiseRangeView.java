package models.ecommerce.merchandise.view;

import io.ebean.Model;

/*
商品集合
 */
public class MerchandiseRangeView extends Model {

    private static final long serialVersionUID = 1L;

    public Long merchandiseId;
    public Long merchandiseTagId;
    public Long merchandiseStoreId;

    public String toString() {
        return new StringBuilder().append("merchandiseId: ").append(merchandiseId).append(", merchandiseTagId: ").append(merchandiseTagId).append(", merchandiseStoreId: ").append(merchandiseStoreId).toString();
    }

/*
    @JsonBackReference(value = "operatorPass")
    @Column(nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '111111' COMMENT '密码'")
    public String operatorPass;ø
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