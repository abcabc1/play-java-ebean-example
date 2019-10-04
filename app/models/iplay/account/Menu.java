package models.iplay.account;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.base.BaseModel;
import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Menu extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '节点编码'")
    public String modelCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '节点名称'")
    public String modelName;

    @Column(nullable = false, columnDefinition = "int(10) unsigned DEFAULT 1 COMMENT '节点排序'")
    public Integer modelOrder;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '' COMMENT '节点排序链'")
    public String modelOrderSeq;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '' COMMENT '节点编码链'")
    public String modelCodeSeq;

    @Column(nullable = false, columnDefinition = "int(10) unsigned DEFAULT 1 COMMENT '节点级别'")
    public Integer modelLevel;

    @ManyToOne
    @JoinColumn(name = "parent_code")
    @JsonBackReference
    public Menu parent;

    @OneToMany(mappedBy = "parent")
    @JsonManagedReference
    public List<Menu> childs;

    @ManyToMany(mappedBy = "menus")
    @JsonIgnoreProperties(value = "menus")
    public List<Role> roles;
}
