package models.words;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
@Entity
public class WordCnSentencePK {

    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '单词'")
    public String word;
    @Column(nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '单词拼音'")
    public String pinyin;
    @Column(nullable = false, columnDefinition = "VARCHAR(256) DEFAULT '' COMMENT '单词例句'")
    public String sentence;

    public int hashCode() {
        return word.hashCode() + pinyin.hashCode() + sentence.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof WordCnSentencePK) {
            WordCnSentencePK pk = (WordCnSentencePK) o;
            return this.word.equals(pk.word) && this.pinyin.equals(pk.pinyin) && this.sentence.equals(pk.sentence);
        }
        return false;
    }
}
