package models.words;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
@Entity
public class WordEnPK {

    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '单词英文'")
    public String word;
    @Column(nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '单词类别'")
    public String type;

    public int hashCode() {
        return word.hashCode() + type.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof WordEnPK) {
            WordEnPK pk = (WordEnPK) o;
            return this.word.equals(pk.word) && this.type.equals(pk.type);
        }
        return false;
    }
}
