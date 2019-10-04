package models.iplay.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import models.base.BaseModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Permission extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '编码'")
    public String permissionCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '名称'")
    public String permissionName;

    @ManyToMany
    @JoinTable(name = "permission_endpoint")
    @JsonIgnoreProperties(value = {"permissions"})
    public List<Endpoint> endpoints;

    @ManyToMany(mappedBy = "permissions")
    @JsonIgnoreProperties(value = {"permissions","menus","operators"})
    public List<Role> roles;

}
