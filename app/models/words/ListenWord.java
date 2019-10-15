package models.words;

import io.ebean.Finder;
import models.base.BasicSimpleModel;

import javax.persistence.*;

@Entity
@Table
public class ListenWord extends BasicSimpleModel {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    public ListenWordPK pk;

    @ManyToOne
    @JoinColumn(name = "word_en", referencedColumnName = "word_en", insertable = false, updatable = false)
    public Word word;

    @ManyToOne
    @JoinColumn(name = "listen_id")
    public Listen listen;

    public static final Finder<ListenWordPK, ListenWord> find = new Finder<>(ListenWord.class, "words");

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