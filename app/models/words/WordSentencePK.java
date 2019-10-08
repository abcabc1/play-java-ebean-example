package models.words;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Embeddable
@Entity
public class WordSentencePK {

    public String code;
    public String type;
    public String sentence;

    public int hashCode(){
        return code.hashCode() + type.hashCode() + sentence.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof WordSentencePK) {
            WordSentencePK pk = (WordSentencePK)o;
            if (this.code.equals(pk.code) && this.type.equals(pk.type) && this.sentence.equals(pk.sentence)) {
                return true;
            }
        }
        return false;
    }
}
