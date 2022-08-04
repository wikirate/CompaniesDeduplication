import exceptions.MalformedCSVException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDeduplicateDetection {

    DuplicateDetection dedup;

    @BeforeEach
    void setUp() {
        try {
            this.dedup = new DuplicateDetectionImpl("src/test/resources/test_companies.csv");
        } catch (MalformedCSVException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void test() {
        try {
            dedup.run(5);
            JSONObject results = dedup.getJSONResults();
            System.out.println(results.toString(4));

            assertEquals(0.8, results.getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONArray("duplicate_candidates")
                    .getJSONObject(0)
                    .getDouble("score")
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
