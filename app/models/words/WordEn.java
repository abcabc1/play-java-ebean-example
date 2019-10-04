package models.words;

import io.ebean.Finder;
import models.base.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "word_en2", uniqueConstraints = @UniqueConstraint(columnNames = {"word", "sentence"}))
public class WordEn extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT '' COMMENT '首字母'")
    public String letter;
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT '' COMMENT '单词'")
    public String word;
    @Column(nullable = false, columnDefinition = "VARCHAR(256) DEFAULT '' COMMENT '单词解释'")
    public String wordTrans;
    @Column(nullable = false, columnDefinition = "VARCHAR(256) DEFAULT '' COMMENT '例句'")
    public String sentence;
    @Column(nullable = false, columnDefinition = "VARCHAR(256) DEFAULT '' COMMENT '例句翻译'")
    public String sentenceTrans;

    public static final Finder<Long, WordEn> find = new Finder<>(WordEn.class, "words");
}
