package tfidf.tf;

import tokenization.Tokenizer;

import java.util.HashMap;

/**
 * An abstract class for the calculation of TF (term frequency). There are a
 * number of variations of Term Frequency that can extend this class.
 *
 * @author vasgat
 */
public abstract class TermFrequency {

    protected Tokenizer tokenizer;

    /**
     * Each TF constructor needs to define a tokenizer.
     *
     * @param tokenizer
     */
    public TermFrequency(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    /**
     * Calculates the TF (term frequency) given the contents of a document as
     * string.
     *
     * @param contents input string
     * @return the calculated term frequency.
     */
    public abstract HashMap calculate(String contents);

    /**
     * returns a small description of the metric
     *
     * @return the small description
     */
    public abstract String info();

    /**
     * Calculates the raw Term Frequency of a given document
     *
     * @param contents input string
     * @return the term frequency (number of times a unique token appear within
     * a given document)
     */
    protected HashMap<String, Integer> rawFrequency(String contents) {
        String[] tokens = tokenizer.tokenize(contents);
        HashMap<String, Integer> tf = new HashMap();

        for (int i = 0; i < tokens.length; i++) {
            if (!tf.containsKey(tokens[i])) {
                tf.put(tokens[i], 1);
            } else {
                tf.put(tokens[i], tf.get(tokens[i]) + 1);
            }
        }

        return tf;
    }
}
