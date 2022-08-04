import exceptions.MalformedCSVException;
import javafx.util.Pair;
import org.apache.commons.collections4.Predicate;
import org.json.JSONArray;
import org.json.JSONObject;
import tfidf.TFIDF;
import utils.*;

import java.io.*;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class DuplicateDetectionImpl implements DuplicateDetection {

    private String csvFilePath;

    private static double DEFAULT_NAME_SIM_THRES = 0.87;

    private static double DEFAULT_ADDRESS_SIM_THRES = 0.5;

    private String outputFilePath;

    private double nameSimThreshold;
    private double addressSimThreshold;
    private TFIDF tfidf;
    private TFIDF tfidf_addresses;

    private Corpus companiesCorpus;

    private Corpus addressesCorpus;

    private HashMap<String, HashMap<String, Double>> results;

    public DuplicateDetectionImpl(String csvFilePath) throws MalformedCSVException, IOException {
        this.csvFilePath = csvFilePath;
        this.outputFilePath = Paths.get(csvFilePath).getParent().toString();

        this.nameSimThreshold = DEFAULT_NAME_SIM_THRES;
        this.addressSimThreshold = DEFAULT_ADDRESS_SIM_THRES;

        this.companiesCorpus = createCorpus(FIELD.NAME);
        this.tfidf = new TFIDF(this.companiesCorpus);
        tfidf.calculate();

        this.addressesCorpus = createCorpus(FIELD.ADDRESS);
        this.tfidf_addresses = new TFIDF(this.addressesCorpus);
        tfidf_addresses.calculate();

        results = new HashMap<>();
    }

    public void setNameSimThreshold(double threshold) {
        this.nameSimThreshold = threshold;
    }

    public void setAddressSimThreshold(double threshold) {
        this.addressSimThreshold = threshold;
    }

    @Override
    public void run(int numOfThreads) throws InterruptedException, ExecutionException {
        long start_time = System.currentTimeMillis();

        System.out.println("Starting deduplication of dataset with " + companiesCorpus.size() + " documents.");
        System.out.println("This might take a while...");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numOfThreads);
        List<Future<Pair<String, HashMap<String, Double>>>> handles = new ArrayList<>();

        Iterator<CompanyDocument> it = companiesCorpus.iterator();
        while (it.hasNext()) {

            CompanyDocument current = it.next();

            handles.add(executor.submit(new Callable<Pair<String, HashMap<String, Double>>>() {
                @Override
                public Pair<String, HashMap<String, Double>> call() {
                    HashSet<String> importantTerms = getMostImportantTerms(current);

                    Predicate<CompanyDocument> filter = companyDocument -> {
                        boolean containsTerms = false;
                        for (String term : companyDocument.bagOfWords.keySet())
                            if (importantTerms.contains(term)) {
                                containsTerms = true;
                                break;
                            }
                        return (companyDocument.headquarters == null || companyDocument.headquarters.equals(current.headquarters))
                                && !current.id.equals(companyDocument.id) && containsTerms;
                    };
                    HashMap<String, Double> duplicates = new HashMap<>();
                    Iterator<CompanyDocument> it2 = companiesCorpus.iterator(filter);
                    while (it2.hasNext()) {
                        CompanyDocument doc = it2.next();
                        boolean continue_to_next = false;
                        for (String identifier : current.registries.keySet()) {
                            if (doc.registries.get(identifier) != null && !doc.registries.get(identifier).equals(current.registries.get(identifier))) {
                                continue_to_next = true;
                                break;
                            }
                        }
                        if (continue_to_next) continue;

                        double similarity = tfidf.similarity(current.id, doc.id);

                        if (doc.address == null && similarity >= 0.92) {
                            duplicates.put(doc.id, similarity * 0.8);
                        } else if (similarity >= nameSimThreshold) {
                            double address_similarity = tfidf_addresses.similarity(current.id, doc.id);
                            if (address_similarity >= addressSimThreshold) {
                                duplicates.put(doc.id, 0.8 * similarity + 0.2 * address_similarity);
                            }
                        }
                    }
                    return new Pair<String, HashMap<String, Double>>(current.id, duplicates);
                }
            }));
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.DAYS);

        for (int t = 0, n = handles.size(); t < n; t++) {
            Pair<String, HashMap<String, Double>> candidates = handles.get(t).get();
            if (candidates.getValue().size() > 0) {
                results.put(candidates.getKey(), candidates.getValue());
            }
        }
        long end_time = System.currentTimeMillis();

        double exec_time = (end_time - start_time) / 1000.0;
        System.out.println("Execution Time in seconds: " + exec_time);
        System.out.println("Deduplication completed. In total " + results.size() + " entities found with possible duplicates");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
    }

    @Override
    public void store(OUTPUT_FORMAT output_format) throws FileNotFoundException, UnsupportedEncodingException {
        System.out.println("Storing Results on a " + output_format + " file...");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if (output_format == OUTPUT_FORMAT.CSV) {
            OutputStream os = new FileOutputStream(outputFilePath + "/Duplicates-" + timestamp.toString().replace(".", "").replace(":", "") + ".csv");
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));

            for (Map.Entry<String, HashMap<String, Double>> candidates : results.entrySet()) {
                CompanyDocument mainEntity = (CompanyDocument) companiesCorpus.getDocument(candidates.getKey());
                writer.println(mainEntity.id + ";" + mainEntity.name + ";" + mainEntity.headquarters + ";" + mainEntity.address);
                for (Map.Entry<String, Double> candidate : candidates.getValue().entrySet()) {
                    CompanyDocument duplicate = (CompanyDocument) companiesCorpus.getDocument(candidate.getKey());
                    writer.println(";;;;" + duplicate.id + ";" + duplicate.name + ";" + duplicate.headquarters + ";" + candidate.getValue() + ";" + duplicate.address);
                }
            }
            writer.close();
        } else {
            JSONObject json = getJSONResults();

            OutputStream os = new FileOutputStream(outputFilePath + "/Duplicates-" + timestamp.toString().replace(".", "").replace(":", "") + ".json");
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.println(json.toString(4));
            writer.close();
        }
    }

    private HashSet<String> getMostImportantTerms(Document document) {
        HashMap<String, Double> idf_terms = new HashMap<>();
        for (String term : document.bagOfWords.keySet()) {
            idf_terms.put(term, tfidf.getTermIDF(term));
        }

        int noOfTerms = 1;
        if (document.bagOfWords.size() >= 5) {
            noOfTerms = 3;
        } else if (document.bagOfWords.size() <= 2) {
            noOfTerms = 1;
        }

        return new HashSet(idf_terms.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()).subList(0, noOfTerms));

    }

    @Override
    public JSONObject getJSONResults() {
        JSONObject json = new JSONObject();
        json.put("task", "deduplication");
        JSONArray json_results = new JSONArray();

        for (Map.Entry<String, HashMap<String, Double>> candidates : results.entrySet()) {
            CompanyDocument main_entity = (CompanyDocument) companiesCorpus.getDocument(candidates.getKey());
            JSONObject company = main_entity.toJson();
            JSONArray duplicate_candidates = new JSONArray();
            for (Map.Entry<String, Double> candidate : candidates.getValue().entrySet()) {
                CompanyDocument duplicate = (CompanyDocument) companiesCorpus.getDocument(candidate.getKey());
                JSONObject duplicate_candidate = duplicate.toJson();
                duplicate_candidate.put("score", candidate.getValue());
                duplicate_candidates.put(duplicate_candidate);
            }
            company.put("duplicate_candidates", duplicate_candidates);
            json_results.put(company);
        }
        json.put("results", json_results);
        return json;
    }

    private Corpus<CompanyDocument> createCorpus(FIELD field) throws MalformedCSVException, IOException {
        CSVCompanyReader companyReader = new CSVCompanyReader(csvFilePath);
        Corpus corpus = companyReader.getCorpus(field);
        return corpus;
    }
}
