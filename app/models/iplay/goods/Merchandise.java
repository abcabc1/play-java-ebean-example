package models.iplay.goods;

import io.ebean.Finder;
import models.base.BaseModel;
import models.iplay.customer.Store;

import javax.persistence.*;

@Entity
@Table
public class Merchandise extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '编码'")
    public String merchandiseCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '名称'")
    public String merchandiseName;

    @Column(nullable = false, columnDefinition = "decimal(11,5) DEFAULT '0.00000' COMMENT '价格'")
    public Long merchandisePrice;

    @Column(nullable = false, columnDefinition = "decimal(11,2) DEFAULT '0.00000' COMMENT '库存'")
    public Long merchandiseStorage;

    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '经营简码'")
    public String businessCode;

    @ManyToOne
    @JoinColumn(name = "goods_id")
    public Goods goods;

    @ManyToOne
    @JoinColumn(name = "store_id")
    public Store store;

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

    public static final Finder<Long, Merchandise> find = new Finder<>(Merchandise.class);
}