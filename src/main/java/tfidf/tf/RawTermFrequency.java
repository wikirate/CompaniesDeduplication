package tfidf.tf;

import tokenization.Tokenizer;

import java.util.HashMap;

/**
 * RawTermFrequency extends abstract class TermFrequency.
 *
 * @author vasgat
 */
public class RawTermFrequency extends TermFrequency {

    /**
     * RawTermFrequency constructor
     *
     * @param tokenizer
     */
    public RawTermFrequency(Tokenizer tokenizer) {
        super(tokenizer);
    }

    /**
     * Calculates the raw term frequency given the contents of the document as
     * string.
     *
     * @param contents input string
     * @return the raw term frequency
     */
    @Override
    public HashMap calculate(String contents) {
        return super.rawFrequency(contents);
    }

    /**
     * returns a small description of the class
     *
     * @return the small description
     */
    @Override
    public String info() {
        return "Raw term frequency: number of times a token appear in a given"
                + " string - document";
    }

}
