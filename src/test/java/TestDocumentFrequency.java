import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.CompanyDocument;
import utils.Corpus;
import tfidf.DocumentFrequency;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDocumentFrequency {
    Corpus<CompanyDocument> corpus;
    DocumentFrequency df;

    @BeforeEach
    void setUp() {
        CompanyDocument adidas = new CompanyDocument.Builder("Adidas AG").id("123").headquarters("Germany").build();
        CompanyDocument adidas_UK = new CompanyDocument.Builder("Adidas UK").id("124").headquarters("United Kingdom").build();
        CompanyDocument pt_adidas = new CompanyDocument.Builder("PT Adidas Incorporated").id("125").headquarters("Singapore").build();

        this.corpus = new Corpus();
        corpus.addDocument(adidas);
        corpus.addDocument(adidas_UK);
        corpus.addDocument(pt_adidas);
    }

    @Test
    void test() {
        HashMap<String, HashSet<String>> df = DocumentFrequency.calculate(corpus);
        assertEquals(df.get("adidas").size(), 3);
    }
}
