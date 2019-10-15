package models.words;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.ebean.Finder;
import models.base.BasicSimpleModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"config_code", "config_category"}))
public class Config extends BasicSimpleModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false, columnDefinition = "VARCHAR(32) COMMENT '配置编码'")
    public String configCode;
    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '配置名称'")
    public String configName;
    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '配置类型'")
    public String configCategory;
    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '配置类型名称'")
    public String configCategoryName;
    @Column(nullable = false, columnDefinition = "TINYINT UNSIGNED DEFAULT 0 COMMENT '配置次序'")
    public Integer configOrder;

    @ManyToOne
    @JoinColumn(name = "parent")
    @JsonBackReference
    public Config parent;

    @OneToMany(mappedBy = "parent")
    @JsonManagedReference
    public List<Config> childs;

    public static final Finder<String, Config> find = new Finder<>(Config.class, "words");

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