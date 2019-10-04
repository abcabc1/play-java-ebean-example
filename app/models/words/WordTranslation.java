package models.words;

import io.ebean.Finder;
import models.base.BaseModel;

import javax.persistence.*;

@Entity
@Table
public class WordTranslation extends BaseModel {

    private static final long serialVersionUID = 1L;
/*
    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '编码'")
    public String code;*/

    @ManyToOne
    @JoinColumn(name = "code")
    public Word word;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '编码'")
    public String translation;

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

    public static final Finder<Long, WordTranslation> find = new Finder<>(WordTranslation.class, "words");
}