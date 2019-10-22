package models.words;

import io.ebean.Finder;
import models.base.BasicSimpleModel;

import javax.persistence.*;

@Entity
@Table
public class WordCn extends BasicSimpleModel {

    private static final long serialVersionUID = 1L;

    public static final Finder<Long, WordCn> find = new Finder<>(WordCn.class, "words");

    @Id
    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(16) COMMENT '单词'")
    public String word;

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