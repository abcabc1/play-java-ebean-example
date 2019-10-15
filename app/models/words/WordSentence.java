package models.words;

import io.ebean.Finder;
import models.base.BasicSimpleModel;

import javax.persistence.*;

@Entity
@Table
public class WordSentence extends BasicSimpleModel {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    public WordSentencePK pk;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "word_en", referencedColumnName = "word_en", insertable = false, updatable = false),
            @JoinColumn(name = "word_type", referencedColumnName = "word_type", insertable = false, updatable = false)
    })
    public WordTrans wordTrans;

    @Column(nullable = false, columnDefinition = "VARCHAR(256) DEFAULT '' COMMENT '单词例句中文'")
    public String sentenceCn;

    public static final Finder<WordSentencePK, WordSentence> find = new Finder<>(WordSentence.class, "words");

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