package models.words;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
@Entity
public class CnWordPK {

    @Column(nullable = false, columnDefinition = "VARCHAR(16) DEFAULT '' COMMENT '单词中文'")
    public String wordCn;
    @Column(nullable = false, columnDefinition = "VARCHAR(64) DEFAULT '' COMMENT '单词拼音'")
    public String wordPy;

    public int hashCode() {
        return wordCn.hashCode() + wordPy.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof CnWordPK) {
            CnWordPK pk = (CnWordPK) o;
            return this.wordCn.equals(pk.wordCn) && this.wordPy.equals(pk.wordPy);
        }
        return false;
    }
}
