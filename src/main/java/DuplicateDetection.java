import exceptions.MalformedCSVException;
import org.apache.commons.collections4.Predicate;
import tfidf.TFIDF;
import utils.*;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class DuplicateDetection {

    private String csvFilePath;

    private static double DEFAULT_NAME_SIM_THRES = 0.87;

    private static double DEFAULT_ADDRESS_SIM_THRES = 0.5;

    private String outputFilePath;

    private double nameSimThreshold;
    private double addressSimThreshold;

    public DuplicateDetection(String csvFilePath) {
        this.csvFilePath = csvFilePath;
        this.outputFilePath = Paths.get(csvFilePath).getParent().toString();
        nameSimThreshold = DEFAULT_NAME_SIM_THRES;
        addressSimThreshold = DEFAULT_ADDRESS_SIM_THRES;
    }

    public void setNameSimThreshold(double threshold) {
        this.nameSimThreshold = threshold;
    }

    public void setAddressSimThreshold(double threshold) {
        this.addressSimThreshold = threshold;
    }

    public void run() throws MalformedCSVException, IOException {
        OutputStream os = new FileOutputStream(outputFilePath + "/Duplicates-" + System.currentTimeMillis() + ".csv");
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));

        Corpus companyCorpus = createCorpus(FIELD.NAME);
        TFIDF tfidf = new TFIDF(companyCorpus);
        tfidf.calculate();

        Corpus addressCorpus = createCorpus(FIELD.ADDRESS);
        TFIDF tfidf_address = new TFIDF(addressCorpus);
        tfidf_address.calculate();

        int counter = 0;
        long start_time = 0;
        long end_time = 0;

        Iterator<CompanyDocument> it = companyCorpus.iterator();
        while (it.hasNext()) {
            CompanyDocument current = it.next();

            HashMap<String, Double> idf_terms = new HashMap<>();
            for (String term : current.bagOfWords.keySet()) {
                idf_terms.put(term, tfidf.getTermIDF(term));
            }

            int noOfTerms = 1;
            if (current.bagOfWords.size() >= 5) {
                noOfTerms = 3;
            } else if (current.bagOfWords.size() <= 2) {
                noOfTerms = 1;
            }

            HashSet<String> importantTerms = new HashSet(idf_terms.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList()).subList(0, noOfTerms));

            Predicate<CompanyDocument> headquartersPredicate = companyDocument -> {
                boolean containsTerms = false;
                for (String term : companyDocument.bagOfWords.keySet())
                    if (importantTerms.contains(term)) {
                        containsTerms = true;
                        break;
                    }
                return (companyDocument.headquarters == null || companyDocument.headquarters.equals(current.headquarters))
                        && !current.id.equals(companyDocument.id) && containsTerms;
            };

            List<CompanyDocument> duplicates = new ArrayList<>();
            Iterator<CompanyDocument> it2 = companyCorpus.iterator(headquartersPredicate);
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
                    duplicates.add(doc);
                } else if (similarity >= nameSimThreshold) {
                    double address_similarity = tfidf_address.similarity(current.id, doc.id);
                    if (address_similarity >= addressSimThreshold) {
                        duplicates.add(doc);
                    }
                }
            }
            writer.println(current.id + ";" + current.name + ";" + current.headquarters + ";" + current.address);
            for (CompanyDocument duplicate : duplicates) {
                writer.println(";;;;" + duplicate.id + ";" + duplicate.name + ";" + duplicate.headquarters + ";" + duplicate.address);
            }
        }
    }

    private Corpus<CompanyDocument> createCorpus(FIELD field) throws MalformedCSVException, IOException {
        CSVCompanyReader companyReader = new CSVCompanyReader(csvFilePath);
        Corpus corpus = companyReader.getCorpus(field);
        return corpus;
    }
}
