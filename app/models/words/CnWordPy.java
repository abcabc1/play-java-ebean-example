package models.words;

import io.ebean.Finder;
import models.base.BasicSimpleModel;

import javax.persistence.*;

@Entity
@Table
public class CnWordPy extends BasicSimpleModel {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    public CnWordPK pk;

    @ManyToOne
    @JoinColumn(name = "word_cn", referencedColumnName = "word_cn", insertable = false, updatable = false)
    public CnWord cnWord;

    public static final Finder<Long, CnWordPy> find = new Finder<>(CnWordPy.class, "default");

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