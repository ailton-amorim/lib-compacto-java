

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.compacto.parser.CompactoParser;
import org.compacto.parser.exceptions.CompactoException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import test.model.ParentClass;
import test.model.ParentListObject;

public class InheritanceWriteConversionTest {
    static CompactoParser compactoParser;

    @BeforeAll
    static void setUp() {
        compactoParser = new CompactoParser();
    }

    @Test
    public void testShouldConvertParentFields() throws CompactoException {
        ParentClass parentClass = new ParentClass();
        parentClass.setParentValue(10);
        parentClass.setGrandValue(12);
        String strOutput = compactoParser.toCompacto(parentClass, getClass());
        assertTrue(strOutput.contains("parentValue=10"));
        assertTrue(strOutput.contains("grandValue=12"));
    }

    @Test
    public void testShouldConvertListInSuperclass() throws CompactoException {
        List<Integer> intList = Arrays.asList(4, 5, 6);
        ParentListObject parentListObject = new ParentListObject();
        parentListObject.setParentList(intList);
        List<String> strList = Arrays.asList("Xícara", "Abotoador", "Androide");
        parentListObject.setMyList(strList);

        String output = compactoParser.toCompacto(parentListObject, parentListObject.getClass());

        assertTrue(output.contains("parentList=["));
        assertTrue(output.contains("myList=["));
    }
}