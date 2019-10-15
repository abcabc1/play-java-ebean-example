package models.words;

import io.ebean.Finder;
import models.base.BasicSimpleModel;

import javax.persistence.*;

@Entity
@Table
public class WordChinese extends BasicSimpleModel {

    private static final long serialVersionUID = 1L;

    public static final Finder<Long, WordChinese> find = new Finder<>(WordChinese.class, "default");

    @Id
    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(16) COMMENT '单词中文'")
    public String wordCn;

    @Column(nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '单词拼音'")
    public String py;

    @ManyToOne
    @JoinColumn(name = "source")
    public Config source;
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