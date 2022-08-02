import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tfidf.tf.RawTermFrequency;
import tokenization.BasicTokenizer;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestRawTermFrequency {

    RawTermFrequency rawTermFrequency;

    @BeforeEach
    void setUp() {
        this.rawTermFrequency = new RawTermFrequency(new BasicTokenizer());
    }

    @Test
    void test() {
        HashMap<String, Integer> expected = new HashMap<>();
        expected.put("adidas", 1);
        expected.put("ag", 1);
        expected.put("123", 1);
        HashMap<String, Integer> actual = rawTermFrequency.calculate("Adidas AG 123   ");
        assertArrayEquals(expected.keySet().toArray(), actual.keySet().toArray());
        assertArrayEquals(expected.values().toArray(), actual.values().toArray());
    }
}
