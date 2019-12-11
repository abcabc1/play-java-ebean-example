package models.ecommerce.merchandise.view;

import io.ebean.Model;

/*
商品集合
 */
public class MerchandiseCodeView extends Model {

    private static final long serialVersionUID = 1L;

    public Long merchandiseId;
    public Long merchandiseTagId;
    public Long merchandiseStoreId;

    public String toString() {
        return new StringBuilder().append("merchandiseId: ").append(merchandiseId).append(", merchandiseTagId: ").append(merchandiseTagId).append(", merchandiseStoreId: ").append(merchandiseStoreId).toString();
    }

    public static MerchandiseCodeView buildFromCacheValue(String s) {
        Long id = Long.valueOf(s.substring(2));
        MerchandiseCodeView merchandiseCodeView = new MerchandiseCodeView();
        switch (s.substring(0, 2)) {
            case "MM":
                merchandiseCodeView.merchandiseId = id;
            case "MT":
                merchandiseCodeView.merchandiseTagId = id;
            case "MS":
                merchandiseCodeView.merchandiseStoreId = id;
        }
        return merchandiseCodeView;
    }

    public static boolean match(MerchandiseCodeView merchandiseCodeViewA, MerchandiseCodeView merchandiseCodeViewB) {
        return merchandiseCodeViewA.merchandiseId == merchandiseCodeViewB.merchandiseId
                || merchandiseCodeViewA.merchandiseTagId == merchandiseCodeViewB.merchandiseTagId
                || merchandiseCodeViewA.merchandiseStoreId == merchandiseCodeViewB.merchandiseStoreId;
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