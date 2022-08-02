package tfidf.idf;

import tfidf.DocumentFrequency;
import utils.Corpus;

import java.util.HashMap;
import java.util.HashSet;

/**
 * An abstract class for the calculation of IDF (Inverse Document Frequency).
 * There are a number of variations of Inverse Document Frequency that can extend
 * this class.
 *
 * @author vasgat
 */
public abstract class IDF {

    protected Corpus corpus;

    protected HashMap<String, HashSet<String>> df;

    /**
     * Each IDF constructor needs to define a Corpus of TextDocuments
     *
     * @param corpus
     */
    public IDF(Corpus corpus) {
        this.corpus = corpus;
        this.df = DocumentFrequency.calculate(corpus);
    }

    /**
     * Calculates the IDF (Inverse Document Frequency) given the contents of a
     * document as string.
     *
     * @return the calculated term frequency.
     */
    public abstract HashMap calculate();

    public abstract double maxIDF();

    /**
     * returns a small description of the metric
     *
     * @return the small description
     */
    public abstract String info();

    public HashMap<String, HashSet<String>> getDocumentFrequency() {
        return df;
    }
}
