package models.words;

import models.base.BaseCodeModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class Word extends BaseCodeModel {
/*
    @Column(nullable = false, columnDefinition = "VARCHAR(64) DEFAULT '' COMMENT '配置名称'")
    public String configName;
    
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
*/
}
