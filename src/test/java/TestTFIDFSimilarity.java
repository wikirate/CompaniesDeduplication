import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tfidf.TFIDF;
import utils.CompanyDocument;
import utils.Corpus;
import utils.Document;
import utils.FIELD;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTFIDFSimilarity {

    Corpus<Document> companyCorpus;
    TFIDF tfidf;

    @BeforeEach
    void setUp() {
        companyCorpus = new Corpus<>();
        companyCorpus.addDocument(new CompanyDocument.Builder("Adidas AG")
                .id("c1")
                .headquarters("Germany")
                .address("Adi-Dassler-Str. 1, 91074 Herzogenaurach, Germany")
                .analyze(FIELD.NAME)
                .build());

        companyCorpus.addDocument(new CompanyDocument.Builder("Adidas UK")
                .id("c2")
                .headquarters("United Kingdom")
                .address("The Adidas Centre, Pepper Road, Hazel Grove, Stockport, Cheshire., SK7 5SA")
                .analyze(FIELD.NAME)
                .build());

        companyCorpus.addDocument(new CompanyDocument.Builder("Adidas Golf")
                .id("c3")
                .analyze(FIELD.NAME)
                .build());

        companyCorpus.addDocument(new CompanyDocument.Builder("Nike Inc.")
                .id("c4")
                .address("ONE BOWERMAN DR BEAVERTON 97005-6453")
                .headquarters("Oregon (United States)")
                .analyze(FIELD.NAME)
                .build());

        companyCorpus.addDocument(new CompanyDocument.Builder("Adidas AG")
                .id("c5")
                .headquarters("Germany")
                .address("Adi-Dassler-Str. 1, 91074 Herzogenaurach, Germany")
                .analyze(FIELD.NAME)
                .build());

        companyCorpus.addDocument(new CompanyDocument.Builder("Adidas")
                .id("c6")
                .headquarters("Germany")
                .address("Adi Dassler Strasse 1, 91074 Herzogenaurach, Germany")
                .analyze(FIELD.NAME)
                .build());

        tfidf = new TFIDF(companyCorpus);
        tfidf.calculate();
    }

    @Test
    void test() {
        assertEquals(1, tfidf.similarity("c1", "c5"));
    }
}
