import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.compacto.parser.CompactoParser;
import org.compacto.parser.exceptions.CompactoException;
import org.junit.jupiter.api.Test;

import test.model.EmptyObject;
import test.model.EstadoCivil;
import test.model.IntegerStringMap;
import test.model.ListContainer;
import test.model.ListOfEnumObject;
import test.model.ListOfMapOfIntegerAndListOfString;
import test.model.ListOfMapOfIntegerAndListOfXWrapper;
import test.model.ListOfMapOfIntegerAndString;
import test.model.ListOfMapOfObjectAndString;
import test.model.LongAsKeyObjectAsValueMap;
import test.model.MapAsKeyMapAsValueMap;
import test.model.ObjectEncapsulator;
import test.model.ObjectEncapsulatorStringMap;
import test.model.StringAsKeyListAsValueMap;
import test.model.StringAsKeyXWrapperAsValueMap;
import test.model.StringObjectEncapsulator;
import test.model.XWrapper;

public class MapTests {
    Logger logger = Logger.getLogger(MapTests.class.getName());

    @Test
    void testStringifySimpleMap() throws NoSuchAlgorithmException, IllegalArgumentException, IllegalAccessException,
            InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException,
            NoSuchFieldException, ClassNotFoundException, CompactoException {
        Map<Integer, String> estados = new HashMap<>();
        estados.put(1, "PI");
        estados.put(2, "GO");
        estados.put(3, "RJ");
        estados.put(4, "SP");

        ObjectEncapsulator objectEncapsulator = new ObjectEncapsulator();
        objectEncapsulator.setObject(estados);

        CompactoParser parser = new CompactoParser();
        String str = parser.toCompacto(objectEncapsulator, ObjectEncapsulator.class);
        assertTrue(str.equals("{object=(1#PI,2#GO,3#RJ,4#SP)}"));
        // Essa volta é impossível porque o objeto interno não implementa
        // ObjectEncapsulator objectEncapsulatorRes = (ObjectEncapsulator)
        // parser.fromCompacto(str, new ObjectEncapsulator());
        // assertEquals("PI", ((Map<Integer, String>)
        // objectEncapsulatorRes.getObject()).get(1));
    }

    @Test
    void testIntegerStringMap() throws NoSuchAlgorithmException, IllegalArgumentException, IllegalAccessException,
            InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException,
            NoSuchFieldException, ClassNotFoundException, CompactoException {
        Map<Integer, String> estados = new HashMap<>();
        estados.put(1, "PI");
        estados.put(2, "GO");
        estados.put(3, "RJ");
        estados.put(4, "SP");
        IntegerStringMap mapEncapsulator = new IntegerStringMap();
        mapEncapsulator.setMapaDeEstados(estados);

        CompactoParser parser = new CompactoParser();
        String strObject = parser.toCompacto(mapEncapsulator, IntegerStringMap.class);
        assertTrue(strObject.equals("{mapaDeEstados=(1#PI,2#GO,3#RJ,4#SP)}"));

        IntegerStringMap parserResult = (IntegerStringMap) parser.fromCompacto(strObject, IntegerStringMap.class);
        assertNotNull(parserResult);
        assertNotNull(parserResult.getMapaDeEstados());
        assertFalse(parserResult.getMapaDeEstados().isEmpty());
        assertEquals("PI", parserResult.getMapaDeEstados().get(1));
        assertEquals("GO", parserResult.getMapaDeEstados().get(2));
        assertEquals("RJ", parserResult.getMapaDeEstados().get(3));
        assertEquals("SP", parserResult.getMapaDeEstados().get(4));
    }

    @Test
    void testObjectAsKeyStringAsValueMap()
            throws NoSuchAlgorithmException, IllegalArgumentException, IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException,
            CompactoException {
        ObjectEncapsulatorStringMap mapEncapsulator = new ObjectEncapsulatorStringMap();
        Map<XWrapper, String> map = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            XWrapper obj = new XWrapper();
            obj.setX(Integer.valueOf(i + 1));

            String value = String.valueOf((i + 1) * 2);

            map.put(obj, value);
        }
        mapEncapsulator.setMapa(map);

        CompactoParser parser = new CompactoParser();
        String str = parser.toCompacto(mapEncapsulator, mapEncapsulator.getClass());

        assertTrue(str.contains("{x=1}#2"));
        assertTrue(str.contains("{x=2}#4"));
        assertTrue(str.contains("{x=3}#6"));

        ObjectEncapsulatorStringMap parsedMapEncapsulatorStringMap = (ObjectEncapsulatorStringMap) parser
                .fromCompacto(str, ObjectEncapsulatorStringMap.class);
        assertNotNull(parsedMapEncapsulatorStringMap);
        assertNotNull(parsedMapEncapsulatorStringMap.getMapa());

        List<Object> keys = Arrays.asList(parsedMapEncapsulatorStringMap.getMapa().keySet().toArray());
        Collections.sort(keys, Comparator.comparingInt((o) -> {
            if (o instanceof XWrapper) {
                return ((XWrapper) o).getX();
            }
            return 0;
        }));

        assertEquals("2", parsedMapEncapsulatorStringMap.getMapa().get(keys.get(0)));
        assertEquals("4", parsedMapEncapsulatorStringMap.getMapa().get(keys.get(1)));
        assertEquals("6", parsedMapEncapsulatorStringMap.getMapa().get(keys.get(2)));
    }

    @Test
    void testStringAsKeyObjectAsValueMap()
            throws NoSuchAlgorithmException, IllegalArgumentException, IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException,
            CompactoException {
        StringAsKeyXWrapperAsValueMap mapEncapsulator = new StringAsKeyXWrapperAsValueMap();
        Map<String, XWrapper> map = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            String key = String.valueOf("Chave-" + (i + 1) * 2);

            XWrapper obj = new XWrapper();
            obj.setX(Integer.valueOf(i + 1));

            map.put(key, obj);
        }
        mapEncapsulator.setMapa(map);

        CompactoParser parser = new CompactoParser();
        String str = parser.toCompacto(mapEncapsulator, StringAsKeyXWrapperAsValueMap.class);

        String expectedString = "{mapa=(Chave-2#{x=1},Chave-4#{x=2},Chave-6#{x=3})}";
        assertTrue(str.contains("Chave-2#{x=1}"));
        assertTrue(str.contains("Chave-4#{x=2}"));
        assertTrue(str.contains("Chave-6#{x=3}"));

        StringAsKeyXWrapperAsValueMap parsedMapEncapsulatorStringMap = (StringAsKeyXWrapperAsValueMap) parser
                .fromCompacto(expectedString, StringAsKeyXWrapperAsValueMap.class);
        assertNotNull(parsedMapEncapsulatorStringMap);
        assertNotNull(parsedMapEncapsulatorStringMap.getMapa());

        List<Object> keys = Arrays.asList(parsedMapEncapsulatorStringMap.getMapa().keySet().toArray());
        Collections.sort(keys, new Comparator<Object>() {
            @Override
            public int compare(Object a, Object b) {
                if (a instanceof String && b instanceof String) {
                    return ((String) a).compareTo((String) b);
                }
                return 0;
            }
        });

        assertEquals(1, parsedMapEncapsulatorStringMap.getMapa().get(keys.get(0)).getX());
        assertEquals(2, parsedMapEncapsulatorStringMap.getMapa().get(keys.get(1)).getX());
        assertEquals(3, parsedMapEncapsulatorStringMap.getMapa().get(keys.get(2)).getX());
    }

    @Test
    void testStringAsKeyListAsValueMap()
            throws NoSuchAlgorithmException, IllegalArgumentException, IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException,
            CompactoException {
        StringAsKeyListAsValueMap object = new StringAsKeyListAsValueMap();
        Map<String, List<String>> mapa = new HashMap<>();
        List<String> paises = Arrays.asList("Brasil", "Noruega", "Portugal", "Irlanda", "Costa Rica");
        List<String> marcas = Arrays.asList("Havaianas", "Samsung", "Dell", "Lenovo", "Bic");
        mapa.put("paises", paises);
        mapa.put("marcas", marcas);
        object.setMapa(mapa);

        CompactoParser parser = new CompactoParser();
        String strObject = parser.toCompacto(object, StringAsKeyListAsValueMap.class);
        assertTrue(!strObject.isEmpty());

        StringAsKeyListAsValueMap parsedObject = (StringAsKeyListAsValueMap) parser.fromCompacto(strObject,
                StringAsKeyListAsValueMap.class);
        assertTrue(parsedObject.getMapa().containsKey("marcas") && parsedObject.getMapa().containsKey("paises"));
        assertEquals("Brasil", parsedObject.getMapa().get("paises").get(0));
        assertEquals("Noruega", parsedObject.getMapa().get("paises").get(1));
        assertEquals("Portugal", parsedObject.getMapa().get("paises").get(2));
        assertEquals("Irlanda", parsedObject.getMapa().get("paises").get(3));
        assertEquals("Costa Rica", parsedObject.getMapa().get("paises").get(4));

        assertEquals("Havaianas", parsedObject.getMapa().get("marcas").get(0));
        assertEquals("Samsung", parsedObject.getMapa().get("marcas").get(1));
        assertEquals("Dell", parsedObject.getMapa().get("marcas").get(2));
        assertEquals("Lenovo", parsedObject.getMapa().get("marcas").get(3));
        assertEquals("Bic", parsedObject.getMapa().get("marcas").get(4));
    }

    @Test
    void testLongAsKeyEmptyObjectAsValueMap()
            throws NoSuchAlgorithmException, IllegalArgumentException, IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException,
            CompactoException {
        LongAsKeyObjectAsValueMap object = new LongAsKeyObjectAsValueMap(new HashMap<Long, EmptyObject>());
        EmptyObject o1 = new EmptyObject();
        object.getMapa().put(11l, o1);
        object.getMapa().put(22l, new EmptyObject());
        object.getMapa().put(33l, new EmptyObject());

        CompactoParser parser = new CompactoParser();
        String strObject = parser.toCompacto(object, object.getClass());

        LongAsKeyObjectAsValueMap parsedObject = (LongAsKeyObjectAsValueMap) parser.fromCompacto(strObject,
                LongAsKeyObjectAsValueMap.class);
        assertNotNull(parsedObject);
        assertNotNull(parsedObject.getMapa());
        assertEquals(3, parsedObject.getMapa().size());
        assertTrue(parsedObject.getMapa().containsKey(11l) && parsedObject.getMapa().containsKey(22l)
                && parsedObject.getMapa().containsKey(33l));
        assertEquals(o1, parsedObject.getMapa().get(11l));
        assertEquals(o1, parsedObject.getMapa().get(22l));
        assertEquals(o1, parsedObject.getMapa().get(33l));
    }

    @Test
    void testMapAsKeyMapAsValueMap()
            throws NoSuchAlgorithmException, IllegalArgumentException, IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException,
            CompactoException {
        MapAsKeyMapAsValueMap object = new MapAsKeyMapAsValueMap();
        Map<String, StringObjectEncapsulator> mapTeam1 = new HashMap<>();
        mapTeam1.put("Meia", new StringObjectEncapsulator("Juan"));
        mapTeam1.put("Atacante", new StringObjectEncapsulator("Carlos"));
        mapTeam1.put("Zagueiro", new StringObjectEncapsulator("Roberto"));
        mapTeam1.put("Goleiro", new StringObjectEncapsulator("Caio"));

        Map<String, StringObjectEncapsulator> mapTeam2 = new HashMap<>();
        mapTeam2.put("Meia", new StringObjectEncapsulator("Amarillo"));
        mapTeam2.put("Atacante", new StringObjectEncapsulator("Diego"));
        mapTeam2.put("Zagueiro", new StringObjectEncapsulator("Anthony"));
        mapTeam2.put("Goleiro", new StringObjectEncapsulator("Jorge"));

        Map<Integer, Map<String, StringObjectEncapsulator>> mapaCentral = new HashMap<>();
        mapaCentral.put(1, mapTeam1);
        mapaCentral.put(2, mapTeam2);

        object.setMapa(mapaCentral);

        CompactoParser parser = new CompactoParser();
        String strObjParsed = (String) parser.toCompacto(object, object.getClass());

        MapAsKeyMapAsValueMap parsedObject = (MapAsKeyMapAsValueMap) parser.fromCompacto(strObjParsed,
                MapAsKeyMapAsValueMap.class);
        assertNotNull(parsedObject);
        assertNotNull(parsedObject.getMapa());
        assertTrue(parsedObject.getMapa().get(1) != null);
        assertTrue(parsedObject.getMapa().get(2) != null);
        assertTrue(parsedObject.getMapa().get(1).get("Meia").getObject().equals("Juan"));
        assertTrue(parsedObject.getMapa().get(1).get("Atacante").getObject().equals("Carlos"));
        assertTrue(parsedObject.getMapa().get(1).get("Zagueiro").getObject().equals("Roberto"));
        assertTrue(parsedObject.getMapa().get(1).get("Goleiro").getObject().equals("Caio"));

        assertTrue(parsedObject.getMapa().get(2).get("Meia").getObject().equals("Amarillo"));
        assertTrue(parsedObject.getMapa().get(2).get("Atacante").getObject().equals("Diego"));
        assertTrue(parsedObject.getMapa().get(2).get("Zagueiro").getObject().equals("Anthony"));
        assertTrue(parsedObject.getMapa().get(2).get("Goleiro").getObject().equals("Jorge"));
    }

    @Test
    void testListOfMapsOfIntegerAndString()
            throws NoSuchAlgorithmException, IllegalArgumentException, IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException,
            CompactoException {
        ListOfMapOfIntegerAndString object = new ListOfMapOfIntegerAndString();
        List<Map<Integer, String>> lista = new ArrayList<>();
        Map<Integer, String> mapa = new HashMap<>();
        mapa.put(1, "abc");
        mapa.put(2, "xyz");
        mapa.put(9, "ktp");
        lista.add(mapa);
        object.setLista(lista);
        String expectedString = "{lista=[(1#abc,2#xyz,9#ktp)]}";
        CompactoParser parser = new CompactoParser();
        String resultString = parser.toCompacto(object, object.getClass());
        assertEquals(expectedString, resultString);

        ListOfMapOfIntegerAndString resultObject = (ListOfMapOfIntegerAndString) parser.fromCompacto(resultString,
                ListOfMapOfIntegerAndString.class);
        assertNotNull(resultObject.getLista());
        assertEquals(object.getLista().size(), resultObject.getLista().size());
        assertTrue(resultObject.getLista().get(0).containsKey(1) && resultObject.getLista().get(0).containsKey(2)
                && resultObject.getLista().get(0).containsKey(9));
        assertEquals(object.getLista().get(0).get(1), resultObject.getLista().get(0).get(1));
        assertEquals(object.getLista().get(0).get(2), resultObject.getLista().get(0).get(2));
        assertEquals(object.getLista().get(0).get(9), resultObject.getLista().get(0).get(9));
    }

    @Test
    void testListOfMapOfObjectAndString()
            throws NoSuchAlgorithmException, IllegalArgumentException, IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException,
            CompactoException {
        ListOfMapOfObjectAndString object = new ListOfMapOfObjectAndString();
        List<Map<XWrapper, String>> lista = new ArrayList<>();
        Map<XWrapper, String> mapa = new HashMap<>();
        mapa.put(new XWrapper(5), "Cinco");
        mapa.put(new XWrapper(1), "Um");
        lista.add(mapa);

        Map<XWrapper, String> mapa2 = new HashMap<>();
        mapa2.put(new XWrapper(4), "quatro");
        mapa2.put(new XWrapper(0), "uno");
        lista.add(mapa2);

        object.setLista(lista);

        String expected = "{lista=[({x=5}#Cinco,{x=1}#Um),({x=4}#quatro,{x=0}#uno)]}";
        String expected1 = "{lista=[({x=5}#Cinco,{x=1}#Um),({x=0}#uno,{x=4}#quatro)]}";
        String expected2 = "{lista=[({x=1}#Um,{x=5}#Cinco),({x=0}#uno,{x=4}#quatro)]}";
        String expected3 = "{lista=[({x=0}#uno,{x=4}#quatro),({x=1}#Um,{x=5}#Cinco)]}";
        String expected4 = "{lista=[({x=4}#quatro,{x=0}#uno),({x=5}#Cinco,{x=1}#Um)]}";

        String expected5 = "{lista=[({x=1}#Um,{x=5}#Cinco),({x=4}#quatro,{x=0}#uno)]}";
        String expected6 = "{lista=[({x=1}#Um,{x=5}#Cinco),({x=4}#quatro,{x=0}#uno)]}";
        String expected7 = "{lista=[({{x=4}#quatro,x=0}#uno),({x=1}#Um,{x=5}#Cinco)]}";
        String expected8 = "{lista=[({x=4}#quatro,{x=0}#uno),({x=1}#Um,{x=5}#Cinco)]}";

        CompactoParser parser = new CompactoParser();
        String resultString = parser.toCompacto(object, object.getClass());
        System.out.println("Observa: " + resultString);
        assertTrue(
                resultString.equals(expected) ||
                        resultString.equals(expected1) ||
                        resultString.equals(expected2) ||
                        resultString.equals(expected3) ||
                        resultString.equals(expected4) ||
                        resultString.equals(expected5) ||
                        resultString.equals(expected6) ||
                        resultString.equals(expected7) ||
                        resultString.equals(expected8),
                resultString);

        ListOfMapOfObjectAndString resultObject = (ListOfMapOfObjectAndString) parser.fromCompacto(resultString,
                ListOfMapOfObjectAndString.class);
        assertNotNull(resultObject);
        assertNotNull(resultObject.getLista());
        assertEquals(object.getLista().size(), resultObject.getLista().size());
        assertEquals(object.getLista().get(0).get(5), resultObject.getLista().get(0).get(5));
        // assertEquals(object.getLista().get(0).get(3),
        // resultObject.getLista().get(0).get(3));
        assertEquals(object.getLista().get(0).get(1), resultObject.getLista().get(0).get(1));

        assertEquals(object.getLista().get(1).get(4), resultObject.getLista().get(1).get(4));
        // assertEquals(object.getLista().get(1).get(2),
        // resultObject.getLista().get(1).get(2));
        assertEquals(object.getLista().get(1).get(0), resultObject.getLista().get(1).get(0));
    }

    @Test
    void testListOfMapsOfIntegerAndListOfObjctsSimplified() throws InstantiationException, InvocationTargetException,
            NoSuchMethodException, SecurityException, NoSuchFieldException, ClassNotFoundException,
            NoSuchAlgorithmException, IllegalArgumentException, IllegalAccessException, CompactoException {
        List<Map<Integer, List<String>>> listOfMaps = new ArrayList<>();

        Map<Integer, List<String>> mapa = new HashMap<>();

        List<String> lista1 = new ArrayList<>();
        lista1.add("a");
        lista1.add("b");

        List<String> lista2 = new ArrayList<>();
        lista2.add("c");
        lista2.add("d");

        mapa.put(1, lista1);
        mapa.put(2, lista2);

        listOfMaps.add(mapa);

        Map<Integer, List<String>> mapa2 = new HashMap<>();

        List<String> listaVogaisMaiusculas = new ArrayList<>();
        listaVogaisMaiusculas.add("A");
        listaVogaisMaiusculas.add("E");

        List<String> listaKY = new ArrayList<>();
        listaKY.add("K");
        listaKY.add("Y");

        mapa2.put(1, listaVogaisMaiusculas);
        mapa2.put(2, listaKY);

        listOfMaps.add(mapa2);

        ListOfMapOfIntegerAndListOfString object = new ListOfMapOfIntegerAndListOfString();
        object.setLista(listOfMaps);

        CompactoParser parser = new CompactoParser();
        String strObject = parser.toCompacto(object, object.getClass());
        String expectedObject = "{lista=[(1#[a,b],2#[c,d]),(1#[A,E],2#[K,Y])]}";
        assertEquals(expectedObject, strObject);

        ListOfMapOfIntegerAndListOfString result = (ListOfMapOfIntegerAndListOfString) parser.fromCompacto(strObject,
                ListOfMapOfIntegerAndListOfString.class);
        assertNotNull(result);
        assertEquals(object.getLista().size(), result.getLista().size());
        assertEquals("a", result.getLista().get(0).get(1).get(0));
    }

    @Test
    void testListOfMapOfIntegerAndListOfXWrapper()
            throws NoSuchAlgorithmException, IllegalArgumentException, IllegalAccessException, InstantiationException,
            InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, CompactoException {
        ListOfMapOfIntegerAndListOfXWrapper object = new ListOfMapOfIntegerAndListOfXWrapper();
        List<Map<Integer, List<XWrapper>>> lista = new ArrayList<>();

        Map<Integer, List<XWrapper>> mapa1 = new HashMap<>();
        List<XWrapper> listX1 = new ArrayList<>();
        listX1.add(new XWrapper(1));
        listX1.add(new XWrapper(2));

        List<XWrapper> listX2 = new ArrayList<>();
        listX2.add(new XWrapper(3));
        listX2.add(new XWrapper(4));

        mapa1.put(1, listX1);
        mapa1.put(2, listX2);

        Map<Integer, List<XWrapper>> mapa2 = new HashMap<>();
        List<XWrapper> listX3 = new ArrayList<>();
        listX3.add(new XWrapper(10));
        listX3.add(new XWrapper(20));

        List<XWrapper> listX4 = new ArrayList<>();
        listX4.add(new XWrapper(30));
        listX4.add(new XWrapper(40));

        mapa2.put(1, listX3);
        mapa2.put(2, listX4);

        lista.add(mapa1);
        lista.add(mapa2);

        object.setLista(lista);

        CompactoParser parser = new CompactoParser();
        String strResult = parser.toCompacto(object, object.getClass());
        ListOfMapOfIntegerAndListOfXWrapper resultObject = (ListOfMapOfIntegerAndListOfXWrapper) parser
                .fromCompacto(strResult, ListOfMapOfIntegerAndListOfXWrapper.class);
        assertNotNull(resultObject);
        assertEquals(object.getLista().size(), resultObject.getLista().size());
        assertEquals(object.getLista().get(0).size(), resultObject.getLista().get(0).size());
        assertEquals(object.getLista().get(1).size(), resultObject.getLista().get(1).size());

        assertEquals(object.getLista().get(0).get(1).get(0).getX(),
                resultObject.getLista().get(0).get(1).get(0).getX());
        assertEquals(object.getLista().get(0).get(1).get(1).getX(),
                resultObject.getLista().get(0).get(1).get(1).getX());

        assertEquals(object.getLista().get(1).get(1).get(0).getX(),
                resultObject.getLista().get(1).get(1).get(0).getX());
        assertEquals(object.getLista().get(1).get(1).get(1).getX(),
                resultObject.getLista().get(1).get(1).get(1).getX());
    }

    @Test
    void testListOfMapsOfIntegerAndListOfObjcts() throws InstantiationException, InvocationTargetException,
            NoSuchMethodException, SecurityException, NoSuchFieldException, ClassNotFoundException {
        List<Map<Integer, List<ListOfEnumObject>>> superObject = new ArrayList<>();

        Map<Integer, List<ListOfEnumObject>> map1 = new HashMap<Integer, List<ListOfEnumObject>>();

        List<ListOfEnumObject> parentList1 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ListOfEnumObject l1 = new ListOfEnumObject();
            List<EstadoCivil> listEstadoCivil = new ArrayList<>();
            listEstadoCivil.addAll(Arrays.asList(EstadoCivil.values()));
            l1.setList(listEstadoCivil);

            parentList1.add(l1);
        }
        map1.put(1, parentList1);

        List<ListOfEnumObject> parentList2 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ListOfEnumObject l2 = new ListOfEnumObject();
            List<EstadoCivil> listEstadoCivil = new ArrayList<>();
            listEstadoCivil.addAll(Arrays.asList(EstadoCivil.values()));
            l2.setList(listEstadoCivil);

            parentList2.add(l2);
        }
        map1.put(2, parentList2);

        // ---------------------------------------------------------------
        Map<Integer, List<ListOfEnumObject>> map2 = new HashMap<Integer, List<ListOfEnumObject>>();

        List<ListOfEnumObject> parentList3 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ListOfEnumObject l1 = new ListOfEnumObject();
            List<EstadoCivil> listEstadoCivil = new ArrayList<>();
            listEstadoCivil.addAll(Arrays.asList(EstadoCivil.values()));
            l1.setList(listEstadoCivil);

            parentList3.add(l1);
        }
        map2.put(1, parentList3);

        List<ListOfEnumObject> parentList4 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ListOfEnumObject l2 = new ListOfEnumObject();
            List<EstadoCivil> listEstadoCivil = new ArrayList<>();
            listEstadoCivil.addAll(Arrays.asList(EstadoCivil.values()));
            l2.setList(listEstadoCivil);

            parentList4.add(l2);
        }
        map2.put(2, parentList4);

        superObject.add(map1);
        superObject.add(map2);

        ListContainer encapsulator = new ListContainer();
        encapsulator.setObject(superObject);

        CompactoParser parser = new CompactoParser();
        try {
            String strObject = parser.toCompacto(encapsulator, encapsulator.getClass());
            assertTrue(strObject.contains("CASADA"));
            assertTrue(strObject.contains("SOLTEIRA"));
            assertTrue(strObject.contains("DIVORCIADA"));

            ListContainer retListContainer = (ListContainer) parser.fromCompacto(strObject, ListContainer.class);
            assertNotNull(retListContainer.getObject());
            assertFalse(retListContainer.getObject().isEmpty());
            assertEquals(encapsulator.getObject().size(), retListContainer.getObject().size());

            // certifica que os mapas estão preenchidos
            assertFalse(retListContainer.getObject().get(0).isEmpty());
            assertFalse(retListContainer.getObject().get(1).isEmpty());

            // certifica que os mapas possuem as chaves inseridas
            assertTrue(retListContainer.getObject().get(0).containsKey(1));
            assertTrue(retListContainer.getObject().get(0).containsKey(2));
            assertFalse(retListContainer.getObject().get(0).containsKey(3));

            // certifica que os mapas possuem as chaves inseridas
            assertTrue(retListContainer.getObject().get(1).containsKey(1));
            assertTrue(retListContainer.getObject().get(1).containsKey(2));
            assertFalse(retListContainer.getObject().get(1).containsKey(3));

            // certifica que as listas nos mapas são preenchidas
            assertFalse(retListContainer.getObject().get(0).get(1).isEmpty());// map at 0 has in key 1 a filled list
            assertFalse(retListContainer.getObject().get(0).get(2).isEmpty());// map at 0 has in key 2 a filled list
            assertFalse(retListContainer.getObject().get(1).get(1).isEmpty());// map at 1 has in key 1 a filled list
            assertFalse(retListContainer.getObject().get(1).get(2).isEmpty());// map at 1 has in key 2 a filled list

            // certifica que o tamanho das listas está correto
            assertEquals(EstadoCivil.values().length,
                    retListContainer.getObject().get(0).get(1).get(0).getList().size());
            assertEquals(EstadoCivil.values().length,
                    retListContainer.getObject().get(0).get(2).get(0).getList().size());
            assertEquals(EstadoCivil.values().length,
                    retListContainer.getObject().get(1).get(1).get(0).getList().size());
            assertEquals(EstadoCivil.values().length,
                    retListContainer.getObject().get(1).get(2).get(0).getList().size());

            // certifica que as listas possuem os valores corretos
            // map at 0 has in key 1 a filled list
            assertTrue(retListContainer.getObject().get(0).get(1).get(0).getList().contains(EstadoCivil.CASADA));
            assertTrue(retListContainer.getObject().get(0).get(1).get(0).getList().contains(EstadoCivil.DIVORCIADA));
            assertTrue(retListContainer.getObject().get(0).get(1).get(0).getList().contains(EstadoCivil.SOLTEIRA));
            assertTrue(retListContainer.getObject().get(0).get(1).get(0).getList().contains(EstadoCivil.UNIAO_ESTAVEL));
            assertTrue(retListContainer.getObject().get(0).get(1).get(0).getList().contains(EstadoCivil.VIUVA));

            assertTrue(retListContainer.getObject().get(0).get(1).get(1).getList().contains(EstadoCivil.CASADA));
            assertTrue(retListContainer.getObject().get(0).get(1).get(1).getList().contains(EstadoCivil.DIVORCIADA));
            assertTrue(retListContainer.getObject().get(0).get(1).get(1).getList().contains(EstadoCivil.SOLTEIRA));
            assertTrue(retListContainer.getObject().get(0).get(1).get(1).getList().contains(EstadoCivil.UNIAO_ESTAVEL));
            assertTrue(retListContainer.getObject().get(0).get(1).get(1).getList().contains(EstadoCivil.VIUVA));

            assertTrue(retListContainer.getObject().get(0).get(2).get(0).getList().contains(EstadoCivil.CASADA));
            assertTrue(retListContainer.getObject().get(0).get(2).get(0).getList().contains(EstadoCivil.DIVORCIADA));
            assertTrue(retListContainer.getObject().get(0).get(2).get(0).getList().contains(EstadoCivil.SOLTEIRA));
            assertTrue(retListContainer.getObject().get(0).get(2).get(0).getList().contains(EstadoCivil.UNIAO_ESTAVEL));
            assertTrue(retListContainer.getObject().get(0).get(2).get(0).getList().contains(EstadoCivil.VIUVA));

            assertTrue(retListContainer.getObject().get(0).get(2).get(1).getList().contains(EstadoCivil.CASADA));
            assertTrue(retListContainer.getObject().get(0).get(2).get(1).getList().contains(EstadoCivil.DIVORCIADA));
            assertTrue(retListContainer.getObject().get(0).get(2).get(1).getList().contains(EstadoCivil.SOLTEIRA));
            assertTrue(retListContainer.getObject().get(0).get(2).get(1).getList().contains(EstadoCivil.UNIAO_ESTAVEL));
            assertTrue(retListContainer.getObject().get(0).get(2).get(1).getList().contains(EstadoCivil.VIUVA));

            assertTrue(retListContainer.getObject().get(1).get(1).get(0).getList().contains(EstadoCivil.CASADA));
            assertTrue(retListContainer.getObject().get(1).get(1).get(0).getList().contains(EstadoCivil.DIVORCIADA));
            assertTrue(retListContainer.getObject().get(1).get(1).get(0).getList().contains(EstadoCivil.SOLTEIRA));
            assertTrue(retListContainer.getObject().get(1).get(1).get(0).getList().contains(EstadoCivil.UNIAO_ESTAVEL));
            assertTrue(retListContainer.getObject().get(1).get(1).get(0).getList().contains(EstadoCivil.VIUVA));

            assertTrue(retListContainer.getObject().get(1).get(1).get(1).getList().contains(EstadoCivil.CASADA));
            assertTrue(retListContainer.getObject().get(1).get(1).get(1).getList().contains(EstadoCivil.DIVORCIADA));
            assertTrue(retListContainer.getObject().get(1).get(1).get(1).getList().contains(EstadoCivil.SOLTEIRA));
            assertTrue(retListContainer.getObject().get(1).get(1).get(1).getList().contains(EstadoCivil.UNIAO_ESTAVEL));
            assertTrue(retListContainer.getObject().get(1).get(1).get(1).getList().contains(EstadoCivil.VIUVA));

            assertTrue(retListContainer.getObject().get(1).get(2).get(0).getList().contains(EstadoCivil.CASADA));
            assertTrue(retListContainer.getObject().get(1).get(2).get(0).getList().contains(EstadoCivil.DIVORCIADA));
            assertTrue(retListContainer.getObject().get(1).get(2).get(0).getList().contains(EstadoCivil.SOLTEIRA));
            assertTrue(retListContainer.getObject().get(1).get(2).get(0).getList().contains(EstadoCivil.UNIAO_ESTAVEL));
            assertTrue(retListContainer.getObject().get(1).get(2).get(0).getList().contains(EstadoCivil.VIUVA));

            assertTrue(retListContainer.getObject().get(1).get(2).get(1).getList().contains(EstadoCivil.CASADA));
            assertTrue(retListContainer.getObject().get(1).get(2).get(1).getList().contains(EstadoCivil.DIVORCIADA));
            assertTrue(retListContainer.getObject().get(1).get(2).get(1).getList().contains(EstadoCivil.SOLTEIRA));
            assertTrue(retListContainer.getObject().get(1).get(2).get(1).getList().contains(EstadoCivil.UNIAO_ESTAVEL));
            assertTrue(retListContainer.getObject().get(1).get(2).get(1).getList().contains(EstadoCivil.VIUVA));
        } catch (Exception e) {
            assertTrue(false, "Não deveria lançar Exception");
        }
    }
}
