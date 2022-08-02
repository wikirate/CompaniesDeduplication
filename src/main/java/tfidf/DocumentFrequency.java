package tfidf;

import utils.Corpus;
import utils.Document;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * DocumentFrequency class calculates the number of document occurrences of
 * unique tokens within a corpus
 *
 * @author vasgat
 */
public class DocumentFrequency {

    /**
     * Calculates the document frequency of the terms within a corpus.
     *
     * @param corpus input corpus of text documents
     * @return the document frequency of the terms
     */
    public static HashMap<String, HashSet<String>> calculate(Corpus corpus) {
        HashMap<String, HashSet<String>> df = new HashMap();

        Iterator<Document> it = corpus.iterator();

        while (it.hasNext()) {
            Document current = it.next();
            HashSet words = new HashSet(current.bagOfWords.keySet());

            Iterator<String> setOfWords = words.iterator();
            while (setOfWords.hasNext()) {
                String current_word = setOfWords.next();

                if (df.containsKey(current_word)) {
                    df.get(current_word).add(current.id);
                } else {
                    HashSet<String> ids = new HashSet<>();
                    ids.add(current.id);
                    df.put(current_word, ids);
                }
            }
        }
        return df;
    }

}

