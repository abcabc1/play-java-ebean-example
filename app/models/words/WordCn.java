package models.words;

import io.ebean.Finder;
import models.base.BasicSimpleModel;

import javax.persistence.*;

@Entity
@Table
public class CnWord extends BasicSimpleModel {

    private static final long serialVersionUID = 1L;

    public static final Finder<Long, CnWord> find = new Finder<>(CnWord.class, "words");

    @Id
    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(16) COMMENT '单词中文'")
    public String wordCn;

    @ManyToOne
    @JoinColumn(name = "source")
    public Config source;

    @ManyToOne
    @JoinColumn(name = "knowledge")
    public Config knowledge;
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