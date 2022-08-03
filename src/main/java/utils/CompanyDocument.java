package utils;

import org.json.JSONArray;
import org.json.JSONObject;
import tfidf.tf.RawTermFrequency;
import tfidf.tf.TermFrequency;
import tokenization.BasicTokenizer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CompanyDocument extends Document {

    public String name;
    public String headquarters;
    public String address;
    private FIELD field;
    public HashMap<String, String> registries;

    private CompanyDocument(Builder builder) {
        super.id = builder.id == null ? UUID.randomUUID().toString() : builder.id;
        this.name = builder.name;
        this.headquarters = builder.headquarters;
        this.address = builder.address;
        this.field = builder.field;
        this.registries = builder.registries;
        if (builder.field == FIELD.NAME) {
            super.contents = builder.name;
        } else if (builder.field == FIELD.ADDRESS) {
            super.contents = builder.address;
        } else {
            super.contents = builder.name + " " + builder.address;
        }
        super.bagOfWords = builder.termFrequency.calculate(super.contents);
    }

    public void addRegistryIdentifier(String registryName, String identifier) {
        if (identifier == null || identifier.equals(""))
            return;
        this.registries.put(registryName, identifier);
    }

    public static class Builder {
        private String id;
        private String name;
        private String headquarters;
        private String address;
        private FIELD field;
        private TermFrequency termFrequency;
        private HashMap<String, String> registries;

        public Builder(String name) {
            if (!name.equals(""))
                this.name = name;
            this.termFrequency = new RawTermFrequency(new BasicTokenizer());
            this.field = FIELD.NAME;
            this.registries = new HashMap<>();
        }

        public Builder id(String id) {
            if (!id.equals(""))
                this.id = id;
            return this;
        }

        public Builder headquarters(String headquarters) {
            if (!headquarters.equals(""))
                this.headquarters = headquarters;
            return this;
        }

        public Builder address(String address) {
            if (!address.equals(""))
                this.address = address;
            return this;
        }

        public Builder registryIdentifier(String registryName, String identifier) {
            if (identifier == null || identifier.equals(""))
                return this;
            registries.put(registryName, identifier);
            return this;
        }

        public Builder analyze(FIELD field) {
            this.field = field;
            return this;
        }

        public Builder termFrequency(TermFrequency termFrequency) {
            this.termFrequency = termFrequency;
            return this;
        }

        public CompanyDocument build() {
            return new CompanyDocument(this);
        }
    }

    @Override
    public String toString() {
        return "[Document|" + id + "]: " + bagOfWords + "\n " + "name: " + name + "\n address: " + address + "\n headquarters: " + headquarters + "\n analysis field: " + field + "\n other identifiers: " + registries;
    }

    public JSONObject toJson() {
        JSONObject company = new JSONObject();
        company.put("id", this.id);
        company.put("name", this.name);
        company.put("headquarters", this.headquarters);
        company.put("address", this.address);

        JSONArray integrations = new JSONArray();
        for (Map.Entry<String, String> integration : this.registries.entrySet()) {
            integrations.put(new JSONObject(integration.getKey(), integration.getValue()));
        }
        company.put("integrations", integrations);
        return company;
    }

}
