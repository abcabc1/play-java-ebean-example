package models.words;

import io.ebean.Finder;
import models.base.BasicModel;

import javax.persistence.*;

@Entity
@Table
public class WordAnalysis extends BasicModel {

    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(256) DEFAULT '' COMMENT '辨析单词,逗号分隔'")
    public String code;

    @ManyToOne
    @JoinColumn(name = "knowledge")
    public Config knowledge;

    @ManyToOne
    @JoinColumn(name = "source")
    public Config source;

    public static final Finder<Long, WordAnalysis> find = new Finder<>(WordAnalysis.class, "default");

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