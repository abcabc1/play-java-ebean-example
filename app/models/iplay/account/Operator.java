package models.iplay.account;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.ebean.Finder;
import models.base.BaseModel;
import models.iplay.common.config.ConfigDetail;

import javax.persistence.*;
import java.util.List;


@Entity
@Table
public class Operator extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '编码'")
    public String operatorCode;

    @JsonBackReference(value = "operatorPass")
    @Column(nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '111111' COMMENT '密码'")
    public String operatorPass;

    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '名称'")
    public String operatorName;

    @ManyToOne
    @JoinColumn(name = "operator_gender")
    public ConfigDetail operatorGender;

    @ManyToMany(mappedBy = "operators")
    @JsonIgnoreProperties(value = {"operators", "childs"})
    public List<Org> orgs;

    @ManyToMany(mappedBy = "operators")
    @JsonIgnoreProperties(value = {"operators"})
    public List<Role> roles;

    public static final Finder<Long, Operator> find = new Finder<>(Operator.class);
}
