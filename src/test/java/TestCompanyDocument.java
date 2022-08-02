import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.CompanyDocument;
import utils.FIELD;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCompanyDocument {

    CompanyDocument companyDocument;

    @BeforeEach
    void setUp() {
        this.companyDocument = new CompanyDocument.Builder("Adidas AG")
                .headquarters("Germany")
                .address("Meuerstrasse 182, 10258, Berlin")
                .analyze(FIELD.NAME)
                .build();
    }

    @Test
    void test() {
        assertEquals("Adidas AG", companyDocument.contents);
    }
}
