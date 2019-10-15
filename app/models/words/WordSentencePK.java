package models.words;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
@Entity
public class WordSentencePK {

    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '单词英文'")
    public String wordEn;
    @Column(nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '单词类别'")
    public String wordType;
    @Column(nullable = false, columnDefinition = "VARCHAR(256) DEFAULT '' COMMENT '单词例句英文'")
    public String sentenceEn;

    public int hashCode() {
        return wordEn.hashCode() + wordType.hashCode() + sentenceEn.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof WordSentencePK) {
            WordSentencePK pk = (WordSentencePK) o;
            return this.wordEn.equals(pk.wordEn) && this.wordType.equals(pk.wordType) && this.sentenceEn.equals(pk.sentenceEn);
        }
        return false;
    }
}
