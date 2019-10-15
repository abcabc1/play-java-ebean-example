package models.words;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
@Entity
public class ListenWordPK {

    @Column(nullable = false, columnDefinition = "VARCHAR(32) DEFAULT '' COMMENT '单词英文'")
    public String wordEn;
    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0 COMMENT '听力ID'")
    public Long listenId;

    public int hashCode() {
        return wordEn.hashCode() + listenId.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof ListenWordPK) {
            ListenWordPK pk = (ListenWordPK) o;
            return this.wordEn.equals(pk.wordEn) && this.listenId == pk.listenId;
        }
        return false;
    }
}
