package tfidf.idf;

import tfidf.DocumentFrequency;
import utils.Corpus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * InverseDocumentFrequency class extends IDF and consists the basic
 * implementation of inverse document frequency. It is the logarithmically
 * scaled inverse fraction of the documents that contain the token, obtained by
 * dividing the total number of documents by the number of documents containing
 * the token, and then taking the logarithm of that quotient.
 *
 * @author vasgat
 */
public class InverseDocumentFrequency extends IDF {

    /**
     * InverseDocumentFrequency constructor
     *
     * @param corpus
     */
    public InverseDocumentFrequency(Corpus corpus) {
        super(corpus);
    }

    /**
     * Calculates the basic Inverse Document Frequency of each token contained
     * in a corpus of documents.
     *
     * @return the IDF of each token in a corpus
     */
    @Override
    public HashMap<String, Double> calculate() {
        HashMap<String, Double> idf = new HashMap();

        Iterator it = df.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, HashSet<String>> entry = (Map.Entry<String, HashSet<String>>) it.next();

            idf.put(entry.getKey(), 1 + Math.log((corpus.size() / (1.0 * entry.getValue().size()))));
        }

        return idf;
    }

    /**
     * returns a small description of the class
     *
     * @return the small description
     */
    @Override
    public String info() {
        return "The inverse document frequency is a measure of how much "
                + "information the word provides, that is, whether the term is "
                + "common or rare across all documents of a corpus. It is the logarithmically "
                + "scaled inverse fraction of the documents that contain the token,"
                + " obtained by dividing the total number of documents by the number "
                + "of documents containing the token, and then taking the logarithm "
                + "of that quotient.";
    }

    @Override
    public double maxIDF() {
        return Math.log(corpus.size() * 1D);
    }
}
