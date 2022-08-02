import exceptions.MalformedCSVException;
import org.apache.commons.collections4.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tokenization.BasicTokenizer;
import utils.CSVCompanyReader;
import utils.CompanyDocument;
import utils.Corpus;
import utils.FIELD;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCSVCompanyReader {

    CSVCompanyReader companyReader;

    @BeforeEach
    void setUp() {
        this.companyReader = new CSVCompanyReader("src/test/resources/test_companies.csv");
    }

    @Test
    void test() {
        try {
            Corpus<CompanyDocument> corpus = companyReader.getCorpus(FIELD.NAME);
            CompanyDocument company = corpus.iterator(new Predicate<CompanyDocument>() {
                @Override
                public boolean evaluate(CompanyDocument companyDocument) {
                    return companyDocument.id.equals("5800");
                }
            }).next();

            assertEquals("8739914", company.registries.get("open_corporates"));
            assertEquals("0000320187", company.registries.get("sec_cik"));
            assertEquals("US2020349PNHW2P", company.registries.get("oar_id"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MalformedCSVException e) {
            throw new RuntimeException(e);
        }

    }
}
