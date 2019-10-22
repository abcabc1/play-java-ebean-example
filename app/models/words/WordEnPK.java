package models.words;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
@Entity
public class WordPK {

    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '单词英文'")
    public String wordEn;
    @Column(nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '单词类别'")
    public String wordType;

    public int hashCode() {
        return wordEn.hashCode() + wordType.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof WordPK) {
            WordPK pk = (WordPK) o;
            return this.wordEn.equals(pk.wordEn) && this.wordType.equals(pk.wordType);
        }
        return false;
    }
}
