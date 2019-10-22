package models.words;

import io.ebean.Finder;
import models.base.BasicSimpleModel;

import javax.persistence.*;

@Entity
@Table
public class WordEnExtend extends BasicSimpleModel {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    public WordEnPK pk;

    @ManyToOne
    @JoinColumn(name = "word", referencedColumnName = "word", insertable = false, updatable = false)
    public WordEn wordEn;

    @Column(nullable = false, columnDefinition = "VARCHAR(64) DEFAULT '' COMMENT '单词中文'")
    public String wordCn;

    public static final Finder<WordEnPK, WordEnExtend> find = new Finder<>(WordEnExtend.class, "words");
/*

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
