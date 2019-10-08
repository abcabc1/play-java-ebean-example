package models.words;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
@Entity
public class WordPK {

    public String code;
    public String type;

    public int hashCode(){
        return code.hashCode() + type.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof WordPK) {
            WordPK pk = (WordPK)o;
            if (this.code.equals(pk.code) && this.type.equals(pk.type)) {
                return true;
            }
        }
        return false;
    }
}
