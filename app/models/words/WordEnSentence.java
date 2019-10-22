package models.words;

import io.ebean.Finder;
import models.base.BasicSimpleModel;

import javax.persistence.*;

@Entity
@Table
public class WordEnSentence extends BasicSimpleModel {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    public WordEnSentencePK pk;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "word", referencedColumnName = "word", insertable = false, updatable = false),
            @JoinColumn(name = "type", referencedColumnName = "type", insertable = false, updatable = false)
    })
    public WordEnExtend wordEnExtend;

    @Column(nullable = false, columnDefinition = "VARCHAR(256) DEFAULT '' COMMENT '单词例句中文'")
    public String sentenceCn;

    @Column(nullable = false, columnDefinition = "TINYINT UNSIGNED DEFAULT 0 COMMENT '数据是否显示 0/1'")
    public boolean isShow;

    public static final Finder<WordEnSentencePK, WordEnSentence> find = new Finder<>(WordEnSentence.class, "words");

/*
    @JsonBackReference(value = "operatorPass")
    @Column(nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '111111' COMMENT '密码'")
    public String operatorPass;
*/
        
 /*
    @ManyToOne
    @JoinColumn(name = "operator_gender")
    public Config operatorGender;

    @ManyToMany(mappedBy = "operators")
    @JsonIgnoreProperties(value = {"operators", "childs"})
    public List<Org> orgs;
 */

/*
    @ManyToMany
    @JoinTable(name = "org_operator")
    @JsonIgnoreProperties(value = {"roles", "orgs"})
    public List<Operator> operators;
*/
}