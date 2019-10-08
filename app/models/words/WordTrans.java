package models.words;

import io.ebean.Finder;
import models.base.BasicSimpleModel;

import javax.persistence.*;

@Entity
@Table
public class WordTrans extends BasicSimpleModel {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    public WordPK pk;

    @ManyToOne
    @JoinColumn(name = "code")
    public Word word;
    public String translation;

    public static final Finder<WordPK, WordTrans> find = new Finder<>(WordTrans.class, "words");
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
