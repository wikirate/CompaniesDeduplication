import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tokenization.BasicTokenizer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBasicTokenizer {

    BasicTokenizer basicTokenizer;

    @BeforeEach
    void setUp() {
        this.basicTokenizer = new BasicTokenizer();
    }

    @Test
    void test() {
        assertArrayEquals(new String[]{"adidas", "ag", "123"}, basicTokenizer.tokenize("Adidas AG 123   "));
    }
}
