package models.iplay.customer;

import io.ebean.Finder;
import models.base.BaseModel;
import models.iplay.account.Operator;
import models.iplay.common.config.Config;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Customer extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '编码'")
    public String customerCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '名称'")
    public String customerName;

    @OneToOne
    @JoinColumn(name = "operator_id", nullable = false)
    public Operator operator;

    @ManyToOne
    @JoinColumn(name = "store_id")
    public Store store;

    @ManyToOne
    @JoinColumn(name = "customer_type")
    public Config customerType;

    @OneToMany(mappedBy = "customer")
    public List<Address> addressList;
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

    public static final Finder<Long, Customer> find = new Finder<>(Customer.class);
}