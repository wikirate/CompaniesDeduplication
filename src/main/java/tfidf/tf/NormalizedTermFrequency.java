package tfidf.tf;

import tokenization.Tokenizer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * NormalizedTermFrequency extends TF. It calculates Term Frequency as
 * logarithmically scaled frequency: tf(t,d) = 1 + log(f(t,d)), or zero if
 * f(t,d) is zero.
 *
 * @author vasgat
 */
public class NormalizedTermFrequency extends TermFrequency {

    /**
     * NormalizedTermFrequency constructor
     *
     * @param tokenizer
     */
    public NormalizedTermFrequency(Tokenizer tokenizer) {
        super(tokenizer);
    }

    /**
     * Calculates the normalized term frequency (log normalization) given the
     * contents of the document as string.
     *
     * @param contents input string
     * @return the normalized term frequency of a document
     */
    @Override
    public HashMap calculate(String contents) {
        HashMap<String, Integer> raw_tf = super.rawFrequency(contents);
        HashMap<String, Double> log_tf = new HashMap();

        Iterator it = raw_tf.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) it.next();

            log_tf.put(entry.getKey(), 1D + Math.log(entry.getValue().doubleValue()));
        }

        return log_tf;
    }

    /**
     * returns a small description of the class
     *
     * @return the small description
     */
    @Override
    public String info() {
        return "Logarithmically scaled term frequency: "
                + "tf(t,d) = 1 + log(f(t,d)), or zero if ft,d is zero";
    }

}
