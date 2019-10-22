package models.words;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.base.BasicSimpleModel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Config extends BasicSimpleModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(32) COMMENT '节点编码'")
    public String modelCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(64) DEFAULT '' COMMENT '节点名称'")
    public String modelName;

    @Column(nullable = false, columnDefinition = "TINYINT UNSIGNED DEFAULT 1 COMMENT '节点排序'")
    public Integer modelOrder;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '' COMMENT '节点排序链'")
    public String modelOrderSeq;

    @Column(nullable = false, columnDefinition = "VARCHAR(2048) DEFAULT '' COMMENT '节点编码链'")
    public String modelCodeSeq;

    @Column(nullable = false, columnDefinition = "TINYINT UNSIGNED DEFAULT 1 COMMENT '节点级别'")
    public Integer modelLevel;

    @ManyToOne
    @JoinColumn(name = "parent_code")
    @JsonBackReference
    public Config parent;

    @OneToMany(mappedBy = "parent")
    @JsonManagedReference
    public List<Config> childs;
}