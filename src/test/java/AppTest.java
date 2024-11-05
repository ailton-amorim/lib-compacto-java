import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.compacto.parser.CompactoParser;
import org.compacto.parser.exceptions.CompactoException;
import org.compacto.parser.exceptions.OpeningCharacterNotFound;
import org.junit.jupiter.api.Test;

import test.model.EmptyObject;
import test.model.EmptyObjectWrapper;
import test.model.EstadoCivil;
import test.model.IntegerPoint;
import test.model.ListOfEnumObject;
import test.model.ListOfListContainer;
import test.model.ListOfListOfStringObjectEncapsulator;
import test.model.Pessoa;
import test.model.StringObjectEncapsulator;

/**
 * Unit test for simple App.
 */
class AppTest {
    // TODO: unificar as exceptions
    Logger logger = Logger.getLogger(AppTest.class.getName());

    /**
     * Rigorous Test :-)
     * 
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws CompactoException
     * @throws NoSuchAlgorithmException
     */
    @Test
    void shouldConvertAttributeString() throws CompactoException {
        String input = "{nome=Fulano de Tal}";
        CompactoParser parser = new CompactoParser();
        Pessoa pessoa = (Pessoa) parser.fromCompacto(input, Pessoa.class);
        assertEquals("Fulano de Tal", pessoa.getNome());
        EstadoCivil.values();
    }

    @Test
    void testFromStringToObject() throws CompactoException {
        String input = "{nome=Ailton}";
        CompactoParser parser = new CompactoParser();
        Pessoa result = (Pessoa) parser.fromCompacto(input, Pessoa.class);
        System.out.println(result);
        assertEquals("Ailton", result.getNome());

        input = "{nome=Ailton,idade=33}";
        result = (Pessoa) parser.fromCompacto(input, Pessoa.class);
        System.out.println(result);
        assertEquals("Ailton", result.getNome());
        assertEquals(33, result.getIdade().intValue());

        input = "{nome=Ailton,idade=33,strong=true}";
        result = (Pessoa) parser.fromCompacto(input, Pessoa.class);
        System.out.println(result);
        assertEquals("Ailton", result.getNome());
        assertEquals(33, result.getIdade().intValue());
        assertEquals(Boolean.TRUE, result.strong);

        input = "{nome=Ailton,idade=33,strong=true,diasDeVida=365,pesoEmGramas=70650.333333,pesoEmQuilogramas=70.65}";
        result = (Pessoa) parser.fromCompacto(input, Pessoa.class);
        System.out.println(result);
        assertEquals("Ailton", result.getNome());
        assertEquals(33, result.getIdade().intValue());
        assertEquals(Boolean.TRUE, result.strong);
        assertEquals(365, result.diasDeVida.intValue());
        assertEquals(70.65, result.pesoEmQuilogramas, 0.00001);
        assertEquals(70650.333333, result.pesoEmGramas, 0.01);

        input = "{nome=jack,idade=28,hobbies=[jiu-jitsu,xadrez,leitura]}";
        result = (Pessoa) parser.fromCompacto(input, Pessoa.class);
        System.out.println(result);
        assertEquals("jack", result.getNome());
        assertEquals(28, result.getIdade().intValue());
        assertEquals(3, result.hobbies.size());
        List<String> expected = List.of("jiu-jitsu", "xadrez", "leitura");
        assertTrue(result.hobbies.stream().allMatch(h -> expected.contains(h)));

        input = "{obj={list=[[banana,tomate,laranja],[pirulito,quindin,pé-de-moleque]]}}";
        CompactoParser parserListOfListContainer = new CompactoParser();
        ListOfListContainer obj = (ListOfListContainer) parserListOfListContainer.fromCompacto(input,
                ListOfListContainer.class);
        System.out.println(obj.getObj());
        assertTrue(obj.getObj().getList().get(0).get(0).equals("banana"));
        assertTrue(obj.getObj().getList().get(0).get(1).equals("tomate"));
        assertTrue(obj.getObj().getList().get(0).get(2).equals("laranja"));

        assertTrue(obj.getObj().getList().get(1).get(0).equals("pirulito"));
        assertTrue(obj.getObj().getList().get(1).get(1).equals("quindin"));
        assertTrue(obj.getObj().getList().get(1).get(2).equals("pé-de-moleque"));
    }

    @Test
    void shouldConvert_String_to_ListOfListOfObject_and_then_back() throws CompactoException {
        String input = "{list=[[{object=Roberta},{object=Joaquim}],[{object=Josélia},{object=Luma}]]}";
        CompactoParser stringToObject = new CompactoParser();
        ListOfListOfStringObjectEncapsulator result = (ListOfListOfStringObjectEncapsulator) stringToObject
                .fromCompacto(input, ListOfListOfStringObjectEncapsulator.class);
        System.out.println(result);
        assertEquals(2, result.getList().size());
        assertEquals(2, result.getList().get(0).size());
        assertEquals(2, result.getList().get(1).size());

        assertEquals("Roberta", result.getList().get(0).get(0).getObject());
        assertEquals("Joaquim", result.getList().get(0).get(1).getObject());
        assertEquals("Josélia", result.getList().get(1).get(0).getObject());
        assertEquals("Luma", result.getList().get(1).get(1).getObject());

        String objectString = stringToObject.toCompacto(result, ListOfListOfStringObjectEncapsulator.class);
        assertEquals(input, objectString, "Faltou alguma coisa pra converter ou converteu demais");
        logger.info(MessageFormat.format("input={0} and parsed={1}", input, objectString));
    }

    @Test
    void testWithEmptyObject() throws CompactoException {
        CompactoParser parser = new CompactoParser();
        EmptyObject emptyObject = new EmptyObject();
        String emptyObjectSerialized = parser.toCompacto(emptyObject, EmptyObject.class);

        assertEquals("{}", emptyObjectSerialized);

        EmptyObject emptyObjectDeserialized = (EmptyObject) parser.fromCompacto(emptyObjectSerialized,
                EmptyObject.class);
        System.out.println("Saída: " + emptyObjectDeserialized);

        assertNotNull(emptyObjectDeserialized);
    }

    @Test
    void testWithEmptyObjectWrapper()
            throws NoSuchAlgorithmException, IllegalArgumentException, IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException,
            ClassNotFoundException, CompactoException {
        CompactoParser parser = new CompactoParser();
        EmptyObjectWrapper emptyObjectWrapper = new EmptyObjectWrapper(new EmptyObject());
        String emptyObjectWrapperSerialized = parser.toCompacto(emptyObjectWrapper, EmptyObjectWrapper.class);

        assertEquals("{object={}}", emptyObjectWrapperSerialized);

        CompactoParser stringToObject = new CompactoParser();
        EmptyObjectWrapper emptyObjectWrapperDeserialized = (EmptyObjectWrapper) stringToObject
                .fromCompacto(emptyObjectWrapperSerialized, EmptyObjectWrapper.class);
        System.out.println("Saída: " + emptyObjectWrapperDeserialized);

        assertNotNull(emptyObjectWrapperDeserialized);
        assertNotNull(emptyObjectWrapperDeserialized.getObject());
    }

    @Test
    void testGetEnclosingIndex() throws OpeningCharacterNotFound {
        CompactoParser stringToObject = new CompactoParser();
        String input = "{x=0}";
        int index = stringToObject.getEnclosingIndex(input, 0);
        assertEquals(4, index);

        input = "{object={x=0}}";
        index = stringToObject.getEnclosingIndex(input, 0);
        assertEquals(13, index);

        index = stringToObject.getEnclosingIndex(input, 8);
        assertEquals(12, index);

        input = "{object=[{x=0,y=1},{x=5,y=6}]}";
        index = stringToObject.getEnclosingIndex(input, 0);
        assertEquals(29, index);

        System.out.println("--- Testes com listas ---");

        input = "{object=[{x=0,y=1},{x=5,y=6}]}";
        index = stringToObject.getEnclosingIndex(input, 8);
        assertEquals(28, index);
    }

    @Test
    void testObjectEncapsulator()
            throws NoSuchAlgorithmException, IllegalArgumentException, IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException,
            ClassNotFoundException, CompactoException {
        CompactoParser parser = new CompactoParser();
        StringObjectEncapsulator emptyObjectWrapper = new StringObjectEncapsulator("String encapsulada");
        String emptyObjectWrapperSerialized = parser.toCompacto(emptyObjectWrapper, StringObjectEncapsulator.class);
        System.out.println("Serialized: " + emptyObjectWrapperSerialized);

        assertEquals("{object=String encapsulada}", emptyObjectWrapperSerialized);

        StringObjectEncapsulator stringObjectEncapsulatorDeserialized = (StringObjectEncapsulator) parser
                .fromCompacto(emptyObjectWrapperSerialized, StringObjectEncapsulator.class);
        System.out.println("Saída: " + stringObjectEncapsulatorDeserialized);

        assertNotNull(stringObjectEncapsulatorDeserialized);
        assertNotNull(stringObjectEncapsulatorDeserialized.getObject());
        assertEquals("String encapsulada", stringObjectEncapsulatorDeserialized.getObject());
    }

    @Test
    void testConvertEnums() throws CompactoException {
        String input = "{nome=Kassandra,estadoCivil=VIUVA}";
        CompactoParser parser = new CompactoParser();

        Pessoa pessoa = (Pessoa) parser.fromCompacto(input, Pessoa.class);

        assertEquals(EstadoCivil.VIUVA, pessoa.getEstadoCivil());

        String strPessoaSerialized = parser.toCompacto(pessoa, Pessoa.class);
        Pessoa pessoaSerialized = (Pessoa) parser.fromCompacto(strPessoaSerialized, Pessoa.class);

        assertEquals("Kassandra", pessoaSerialized.getNome());
        assertEquals(EstadoCivil.VIUVA, pessoaSerialized.getEstadoCivil());
    }

    @Test
    void testConvertEnumsInLists() throws CompactoException {
        CompactoParser parser = new CompactoParser();

        ListOfEnumObject listOfEnumObject = new ListOfEnumObject();
        List<EstadoCivil> list = new ArrayList<>();
        list.add(EstadoCivil.VIUVA);
        list.add(EstadoCivil.SOLTEIRA);
        list.add(EstadoCivil.CASADA);

        listOfEnumObject.setList(list);

        String listSerialized = parser.toCompacto(listOfEnumObject, ListOfEnumObject.class);
        ListOfEnumObject deserializedObject = (ListOfEnumObject) parser.fromCompacto(listSerialized,
                ListOfEnumObject.class);

        assertEquals(EstadoCivil.VIUVA, deserializedObject.getList().get(0));
        assertEquals(EstadoCivil.SOLTEIRA, deserializedObject.getList().get(1));
        assertEquals(EstadoCivil.CASADA, deserializedObject.getList().get(2));
    }

    @Test
    void testStringWithWithCompactoCharacters() throws CompactoException {
        CompactoParser parser = new CompactoParser();
        StringObjectEncapsulator emptyObjectWrapper = new StringObjectEncapsulator("{[#,;,#s,=?\\-\\/\\#]()}");
        String emptyObjectWrapperSerialized = parser.toCompacto(emptyObjectWrapper, StringObjectEncapsulator.class);
        System.out.println("Serialized: " + emptyObjectWrapperSerialized);

        assertEquals("{object=\\{\\[\\#\\,;\\,\\#s\\,\\=?\\-\\/\\\\#\\]\\(\\)\\}}", emptyObjectWrapperSerialized);

        StringObjectEncapsulator stringObjectEncapsulatorDeserialized = (StringObjectEncapsulator) parser
                .fromCompacto(emptyObjectWrapperSerialized, StringObjectEncapsulator.class);
        System.out.println("Saída: " + stringObjectEncapsulatorDeserialized);

        assertNotNull(stringObjectEncapsulatorDeserialized);
        assertNotNull(stringObjectEncapsulatorDeserialized.getObject());
        assertEquals("{[#,;,#s,=?\\-\\/\\#]()}".toString(), stringObjectEncapsulatorDeserialized.getObject());
    }

    @Test
    void doNotConvertPrivateStaticFinal() throws CompactoException {
        CompactoParser parser = new CompactoParser();
        IntegerPoint point = new IntegerPoint(5, 8);
        String result = parser.toCompacto(point, point.getClass());
        assertFalse(result.contains("serialVersionUID"));
        assertTrue(result.contains("x=5"));
        assertTrue(result.contains("y=8"));
    }
}
