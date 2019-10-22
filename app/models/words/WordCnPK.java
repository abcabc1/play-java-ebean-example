package models.words;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
@Entity
public class WordCnPK {

    @Column(nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '单词中文'")
    public String word;
    @Column(nullable = false, columnDefinition = "VARCHAR(64) DEFAULT '' COMMENT '单词拼音'")
    public String pinyin;

    public int hashCode() {
        return word.hashCode() + pinyin.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof WordCnPK) {
            WordCnPK pk = (WordCnPK) o;
            return this.word.equals(pk.word) && this.pinyin.equals(pk.pinyin);
        }
        return false;
    }
}
