package models.words;

import io.ebean.Finder;
import models.base.BasicSimpleModel;

import javax.persistence.*;

@Entity
@Table
public class WordCnExtend extends BasicSimpleModel {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    public WordCnPK pk;

    @ManyToOne
    @JoinColumn(name = "word", referencedColumnName = "word", insertable = false, updatable = false)
    public WordCn wordCn;

    @Column(nullable = false, columnDefinition = "VARCHAR(64) DEFAULT '' COMMENT '错误单词'")
    public String wrongWord;

    public static final Finder<Long, WordCnExtend> find = new Finder<>(WordCnExtend.class, "words");

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