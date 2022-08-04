import org.json.JSONObject;
import utils.OUTPUT_FORMAT;

public interface DuplicateDetection {

    void run(int numberOfThreads) throws Exception;

    JSONObject getJSONResults();

    void store(OUTPUT_FORMAT output_format) throws Exception;
}
