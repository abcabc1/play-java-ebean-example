package models.words;

import io.ebean.Finder;
import models.base.BasicModel;
import models.base.BasicSimpleModel;

import javax.persistence.*;

@Entity
@Table

public class WordSentence extends BasicSimpleModel {

    private static final long serialVersionUID = 1L;

    /*@Column(unique = true, nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '编码'")
    public String code;*/

    @EmbeddedId
    public WordSentencePK pk;

    /*@ManyToOne
    @JoinColumns({
            @JoinColumn(name="code", referencedColumnName="code"),
            @JoinColumn(name="type", referencedColumnName="type")
    })
    public WordTrans wordTrans;*/
    public String translation;

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