package models.words;

import io.ebean.Finder;
import models.base.BasicModel;

import javax.persistence.*;

@Entity
@Table
public class Listen extends BasicModel {

    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(64) DEFAULT '' COMMENT '听力主题'")
    public String listenTopic;

    @Column(nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '听力级别'")
    public String listenLevel;

    @ManyToOne
    @JoinColumn(name = "source")
    public Config source;

    public static final Finder<Long, Listen> find = new Finder<>(Listen.class, "words");

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

/*    @ManyToMany(mappedBy = "listens")
    @JsonIgnoreProperties(value = {"listens"})
    public List<Word> words;*/
}