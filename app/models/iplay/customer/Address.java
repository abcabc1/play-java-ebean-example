package models.iplay.customer;

import io.ebean.Finder;
import models.base.BaseModel;

import javax.persistence.*;

@Entity
@Table
public class Address extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '' COMMENT '地址'")
    public String addressContent;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '' COMMENT '省/直辖市'")
    public String provinceCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '' COMMENT '省/直辖市名称'")
    public String provinceName;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '' COMMENT '市'")
    public String cityCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '' COMMENT '市名称'")
    public String cityName;

    @Column( nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '' COMMENT '区'")
    public String districtCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '' COMMENT '区名称'")
    public String districtName;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    public Customer customer;

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

    public static final Finder<Long, Address> find = new Finder<>(Address.class);
}