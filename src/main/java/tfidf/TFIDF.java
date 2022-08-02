package tfidf;

import similarities.CosineSimilarity;
import tfidf.idf.IDF;
import tfidf.idf.InverseDocumentFrequency;
import tfidf.tf.NormalizedTermFrequency;
import tfidf.tf.TermFrequency;
import tokenization.BasicTokenizer;
import utils.Corpus;
import utils.Document;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * TFIDF class implements Term Frequency Inverse Document Frequency statistic.
 * TF-IDF is a numerical statistic that is intended to reflect how important a
 * word is to a document in a collection or corpus.
 *
 * @author vasgat
 */
public class TFIDF {

    private Corpus corpus;
    public IDF idf;
    private TermFrequency termFrequency;
    private HashMap<String, HashMap<String, Double>> tfidf;
    private HashMap<String, Double> idf_weights;

    /**
     * TFIDF constructor
     *
     * @param corpus
     */
    public TFIDF(Corpus<Document> corpus) {
        this.corpus = corpus;
        this.termFrequency = new NormalizedTermFrequency(new BasicTokenizer());
    }

    /**
     * set preferable variation of IDF
     *
     * @param idf
     */
    public void setIDF(IDF idf) {
        this.idf = idf;
    }

    /**
     * set preferable variation of TF
     *
     * @param tf
     */
    public void setTermFrequency(TermFrequency tf) {
        this.termFrequency = tf;
    }

    /**
     * Calculates the tf-idf weights in the given corpus
     */
    public void calculate() {
        tfidf = new HashMap();

        if (idf == null) {
            idf = new InverseDocumentFrequency(corpus);
        }

        idf_weights = idf.calculate();

        Iterator<Document> collection = corpus.iterator();

        while (collection.hasNext()) {
            Document document = collection.next();

            Iterator tokens = termFrequency.calculate(document.contents).entrySet().iterator();

            HashMap<String, Double> tfidf_weights = new HashMap();

            while (tokens.hasNext()) {
                Map.Entry<String, Double> tf_weights
                        = (Map.Entry<String, Double>) tokens.next();

                double tfidf_weight = tf_weights.getValue() * idf_weights.get(tf_weights.getKey());

                tfidf_weights.put(tf_weights.getKey(), tfidf_weight);
            }
            tfidf.put(document.id, tfidf_weights);
        }

    }

    /**
     * Calculate tfidf weights of new incoming document
     *
     * @param newDoc
     */
    public void calculate(Document newDoc) {
        Iterator tokens = termFrequency.calculate(newDoc.contents).entrySet().iterator();

        HashMap<String, Double> tfidf_weights = new HashMap();

        while (tokens.hasNext()) {
            Map.Entry<String, Double> tf_weights
                    = (Map.Entry<String, Double>) tokens.next();
            double tfidf_weight;
            if (idf_weights.containsKey(tf_weights.getKey())) {
                tfidf_weight = tf_weights.getValue() * idf_weights.get(tf_weights.getKey());
            } else {
                tfidf_weight = idf.maxIDF();
            }

            tfidf_weights.put(tf_weights.getKey(), tfidf_weight);
        }
        tfidf.put(newDoc.id, tfidf_weights);
    }

    /**
     * returns the tf*idf weights of each token of given document
     *
     * @param document_id document identifier
     * @return the tf*idf weights
     */
    public HashMap<String, Double> tfidf(String document_id) {
        return tfidf.get(document_id);
    }

    /**
     * returns the tf*idf weight of a token in a given document
     *
     * @param document_id document identifier
     * @param token
     * @return the tf*idf weight of the token
     */
    public double tfidf(String document_id, String token) {
        return tfidf.get(document_id).get(token);
    }

    /**
     * Returns a string representation of the object.
     *
     * @return the string representation of the object
     */
    public String toString() {
        return "tfidf weights: " + tfidf;
    }

    /**
     * returns a small description
     *
     * @return the small description
     */
    public String info() {
        return " term frequency–inverse document frequency, is a numerical "
                + "statistic that is intended to reflect how important a word "
                + "is to a document in a collection or corpus";
    }

    /**
     * Calculates tf-idf similarity between two given documents. It is actually
     * the calculated Cosine Similarity by using tf*idf weights.
     *
     * @param doc1_id the identifier of document 1
     * @param doc2_id the identifier of document 2
     * @return the tf-idf similarity as a double (values between 0-1)
     */
    public double similarity(String doc1_id, String doc2_id) {
        CosineSimilarity cosine_similarity = new CosineSimilarity();
        return cosine_similarity.calculate(tfidf.get(doc1_id), tfidf.get(doc2_id));
    }

    public double getTermIDF(String term) {
        return idf_weights.get(term);
    }

}
