package utils;


import java.util.HashSet;
import java.util.Iterator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

/**
 * Corpus is a large collection of elements T. Corpus implements Iterable
 * abstract class to iterate to the set of documents.
 *
 * @author vasgat
 */
public class Corpus<T> implements Iterable<T> {

    private HashSet<T> corpus;

    /**
     * Corpus constructor
     */
    public Corpus() {
        this.corpus = new HashSet();
    }

    /**
     * A defined document is added to the corpus
     *
     * @param document
     */
    public void addDocument(T document) {
        corpus.add(document);
    }

    /**
     * Returns the size of the corpus
     *
     * @return the number of documents the Corpus contains
     */
    public int size() {
        return corpus.size();
    }

    /**
     *
     * @return the iterator of the corpus
     */
    @Override
    public Iterator<T> iterator() {
        Iterator<T> it = corpus.iterator();
        return it;
    }

    /**
     *
     * @param predicate
     * @return
     */
    public Iterator<T> iterator(Predicate<T> predicate) {
        HashSet subset = new HashSet(CollectionUtils.select(corpus, predicate));
        return subset.iterator();
    }
}
