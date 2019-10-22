package models.words;

import io.ebean.Finder;
import models.base.BasicModel;

import javax.persistence.*;

@Entity
@Table
public class ListenDialog extends BasicModel {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false, columnDefinition = "VARCHAR(256) DEFAULT '' COMMENT '听力对话问题英文'")
    public String dialogQuestionEn;
    @Column(nullable = false, columnDefinition = "VARCHAR(256) DEFAULT '' COMMENT '听力对话回答英文'")
    public String dialogAnswerEn;
    @Column(nullable = false, columnDefinition = "VARCHAR(256) DEFAULT '' COMMENT '听力对话问题中文'")
    public String dialogQuestionCn;
    @Column(nullable = false, columnDefinition = "VARCHAR(256) DEFAULT '' COMMENT '听力对话回答中文'")
    public String dialogAnswerCn;

    @ManyToOne
    @JoinColumn(name = "listen")
    public Listen listen;

    public static final Finder<Long, ListenDialog> find = new Finder<>(ListenDialog.class, "words");

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