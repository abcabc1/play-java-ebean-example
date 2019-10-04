package models.iplay.common.config;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.base.BaseCodeModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class ConfigDetail extends BaseCodeModel {

    @Column(nullable = false, columnDefinition = "VARCHAR(64) DEFAULT '' COMMENT '配置明细名称'")
    public String configDetailName;

    @Column(nullable = false, columnDefinition = "int unsigned DEFAULT 1 COMMENT '节点排序'")
    public Integer configDetailOrder;

    @ManyToOne
    @JoinColumn(name = "config_code")
    @JsonBackReference
    public Config config;

    @ManyToOne
    @JoinColumn(name = "parent_code")
    @JsonBackReference
    public ConfigDetail parent;

    @OneToMany(mappedBy = "parent")
    @JsonManagedReference
    public List<ConfigDetail> childs;
}
