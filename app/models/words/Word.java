package models.words;

import io.ebean.Finder;
import models.base.BasicCodeModel;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class Word extends BasicCodeModel {

    private static final long serialVersionUID = 1L;

    public String letter;

    public static final Finder<String, Word> find = new Finder<>(Word.class, "words");
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
