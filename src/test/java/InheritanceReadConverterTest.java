import static org.junit.jupiter.api.Assertions.assertTrue;

import org.compacto.parser.CompactoParser;
import org.compacto.parser.exceptions.CompactoException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import test.model.ParentClass;

public class InheritanceReadConverterTest {
    private static CompactoParser parser;

    @BeforeAll
    static void setUp() {
        parser = new CompactoParser();
    }

    @Test
    void testShouldReadStringFromCompactoFormat() throws CompactoException {
        String strInput = "{parentValue=15,grandValue=120}";
        ParentClass parentClass = (ParentClass) parser.fromCompacto(strInput, ParentClass.class);
        assertTrue(parentClass.getParentValue().equals(15));
        assertTrue(parentClass.getGrandValue().equals(120));
    }
}
