package models.words;

import io.ebean.Finder;
import models.base.BasicSimpleModel;

import javax.persistence.*;

@Entity
@Table
public class Word extends BasicSimpleModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '单词英文'")
    public String wordEn;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT '' COMMENT '单词首字母'")
    public String wordLetter;

    @ManyToOne
    @JoinColumn(name = "source")
    public Config source;

/*    @ManyToMany
    @JoinTable(name = "listen_word")
    @JsonIgnoreProperties(value = {"words"})
    public List<Listen> listens;*/

    public static final Finder<String, Word> find = new Finder<>(Word.class, "words");

    /*@OneToMany(mappedBy = "word")
    @JsonManagedReference
    public List<WordTrans> wordTransList;*/
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
