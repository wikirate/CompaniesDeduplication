package utils;

import exceptions.MalformedCSVException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CSVCompanyReader {

    public static DELIMITER DEFAULT_DELIMITER = DELIMITER.SEMICOLON;

    private String path;

    public static List<String> MAIN_FIELDS = Arrays.asList("id", "name", "address", "headquarters");

    public CSVCompanyReader(String path) {
        this.path = path;
    }

    public Corpus getCorpus(FIELD analysisField) throws IOException, MalformedCSVException {
        Corpus corpus = new Corpus();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        HashMap<String, Integer> columns = new HashMap<>();

        List<String> registries = new ArrayList<>();
        boolean first_row = true;
        while ((line = br.readLine()) != null) {
            if (first_row) {
                String[] names = line.split(DEFAULT_DELIMITER.value());
                for (int i = 0; i < names.length; i++) {
                    columns.put(names[i].toLowerCase().trim(), i);
                    if (!MAIN_FIELDS.contains(names[i])) {
                        registries.add(names[i]);
                    }
                }

                first_row = false;
            } else {
                String[] values = line.split(DEFAULT_DELIMITER.value(), -1);

                String id = columns.get("id") != null ? values[columns.get("id")] : null;
                String name = columns.get("name") != null ? values[columns.get("name")] : null;
                String headquarters = columns.get("headquarters") != null ? values[columns.get("headquarters")] : null;
                String address = columns.get("address") != null ? values[columns.get("address")] : null;

                if (name == null) {
                    throw new MalformedCSVException();
                }

                CompanyDocument companyDocument = new CompanyDocument.Builder(name)
                        .id(id)
                        .headquarters(headquarters)
                        .address(address)
                        .analyze(analysisField)
                        .build();

                for (String registry : registries) {
                    companyDocument.addRegistryIdentifier(registry, values[columns.get(registry)]);
                }

                corpus.addDocument(companyDocument);
            }
        }
        return corpus;
    }

}
