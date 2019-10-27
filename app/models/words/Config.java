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
    @Column(nullable = false, columnDefinition = "VARCHAR(32) COMMENT '配置编码'")
    public String code;
    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '配置名称'")
    public String name;
    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '配置类型'")
    public String category;
    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '配置类型名称'")
    public String categoryName;
    @Column(nullable = false, columnDefinition = "TINYINT UNSIGNED DEFAULT 0 COMMENT '配置次序'")
    public Integer configOrder;

    @ManyToOne
    @JoinColumn(name = "parent")
    @JsonBackReference
    public Config parent;

    @OneToMany(mappedBy = "parent")
    @JsonManagedReference
    public List<Config> childs;
}