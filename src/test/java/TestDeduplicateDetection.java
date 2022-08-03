import exceptions.MalformedCSVException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDeduplicateDetection {

    DuplicateDetection dedup;

    @BeforeEach
    void setUp() {
        try {
            this.dedup = new DuplicateDetection("src/test/resources/test_companies.csv");
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

            assertEquals(0.8, results.getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONArray("duplicate_candidates")
                    .getJSONObject(0)
                    .getDouble("score")
            );

        } catch (MalformedCSVException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
