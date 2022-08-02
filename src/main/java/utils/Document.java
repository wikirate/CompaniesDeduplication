package utils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import tfidf.tf.RawTermFrequency;
import tfidf.tf.TermFrequency;
import tokenization.BasicTokenizer;
import tokenization.Tokenizer;

import java.util.HashMap;

/**
 *
 * @author vasgat
 */
public abstract class Document {

    /**
     * unique document id
     */
    public String id;

    /**
     * the contents of the document
     */
    public String contents;

    /**
     * The document as bagOfWords vector with the raw term frequency of each
     * word
     */
    public HashMap<String, Integer> bagOfWords;

    /**
     * returns a small description of the metric
     *
     * @return the small description
     */
    public String info() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * returns a string identifier of the metric
     *
     * @return the string identifier of the metric
     */
    @Override
    public String toString() {
        return "[Document|" + id + "]: " + bagOfWords;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31)
                .append(id)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Document)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Document rhs = (Document) obj;
        return new EqualsBuilder()
                .append(id, rhs.id)
                .isEquals();
    }
}

