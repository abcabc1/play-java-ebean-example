package models.iplay.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import models.base.BaseModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Role extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '编码'")
    public String roleCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '名称'")
    public String roleName;

    @ManyToMany
    @JoinTable(name = "role_operator")
    @JsonIgnoreProperties(value = {"roles", "orgs", "childs"})
    public List<Operator> operators;

    @ManyToMany
    @JoinTable(name = "role_menu")
    @JsonIgnoreProperties(value = {"roles", "childs"})
    public List<Menu> menus;

    @ManyToMany
    @JoinTable(name = "role_permission")
    @JsonIgnoreProperties(value = {"roles", "childs"})
    public List<Permission> permissions;
}
