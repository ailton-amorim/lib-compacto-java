package org.compacto.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.compacto.parser.annotations.CompactoProperty;
import org.compacto.parser.exceptions.CompactoException;
import org.compacto.parser.exceptions.InvalidObjectException;
import org.compacto.parser.exceptions.MalFormatException;
import org.compacto.parser.exceptions.OpeningCharacterNotFound;
import org.compacto.parser.model.CompactoSerializable;
import org.compacto.parser.model.ListCompactoSerializable;

public class CompactoParser extends BaseCompactoParser {
    Logger logger = Logger.getLogger(getClass().getName());

    private List<Class> numericalValueClasses = Arrays.asList(
            Integer.class, Long.class, Float.class, Double.class,
            BigDecimal.class, BigInteger.class, Boolean.class);

    @Override
    public String toCompacto(Object object, Type resolvedType) throws CompactoException {
        StringBuilder sb = new StringBuilder();
        try {
            stringfy(sb, object, resolvedType);
        } catch (Exception e) {
            throw new CompactoException(e);
        }
        return sb.toString();
    }

    public String toCompacto(List<CompactoSerializable> list) throws ClassNotFoundException, CompactoException {
        ListCompactoSerializable output = new ListCompactoSerializable(list);
        return toCompacto(output, output.getClass());
    }

    @SuppressWarnings({ "rawtypes" })
    protected void stringfy(StringBuilder sb, Object obj, Type type)
            throws NoSuchAlgorithmException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
        if (obj instanceof List) {
            sb.append(CHAR_OPEN_LIST);
            Iterator iterator = ((List) obj).iterator();
            while (iterator.hasNext()) {
                Object item = iterator.next();
                if (item == null) {
                    sb.append("null");
                    if (iterator.hasNext())
                        sb.append(CHAR_CLOSE_VALUE);
                    continue;
                }
                if (item instanceof List || item instanceof Map) {
                    stringfy(sb, item, item.getClass());
                } else if (numericalValueClasses.contains(item.getClass())) {
                    sb.append(item.toString());
                } else if (item.getClass() == String.class) {
                    sb.append(escapeCharacters(item.toString()));
                } else if (item.getClass().isEnum()) {
                    sb.append(((Enum) item).name());
                } else {
                    stringifyObject(sb, item);
                }
                if (iterator.hasNext())
                    sb.append(CHAR_CLOSE_VALUE);
            }
            sb.append(CHAR_CLOSE_LIST);
        } else if (numericalValueClasses.contains(obj.getClass())) {
            sb.append(obj.toString());
        } else if (obj.getClass() == String.class) {
            sb.append(escapeCharacters(obj.toString()));
        } else if (obj instanceof Map) {
            stringifyMap(sb, obj, ((Type) obj.getClass()));
        } else if (obj instanceof Object) {
            sb.append(CHAR_OPEN_OBJECT);
            Map<String, Field> fields = getAllFields(obj);
            Set<String> keyFields = fields.keySet();
            Iterator keyFieldsIterator = keyFields.iterator();
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(keyFieldsIterator.next());
                field.setAccessible(true);

                String key = getAnnotationValueOrFieldName(field);
                sb.append(key);
                sb.append(CHAR_EQUALITY);

                if (field.getType() == String.class) {
                    sb.append(escapeCharacters(field.get(obj).toString()));
                } else if (field.getType().isPrimitive() || numericalValueClasses.contains(field.getType())) {
                    sb.append(field.get(obj));
                } else if (field.get(obj) instanceof List || field.get(obj) instanceof Map) {
                    stringfy(sb, field.get(obj), Class.forName(field.getType().getTypeName()));
                } else if (field.getType().isEnum()) {
                    sb.append(((Enum) field.get(obj)).name());
                } else if (field.get(obj) instanceof Object) {
                    stringfy(sb, field.get(obj), Class.forName(field.getType().getTypeName()));
                } else {// objeto não tratado
                    sb.append("null");
                }
                if (i < fields.size() - 1)
                    sb.append(CHAR_CLOSE_VALUE);
            }
            sb.append(CHAR_CLOSE_OBJECT);
        } else {
            throw createException(5, null, null, obj);
        }
    }

    private String stringifyKey(Object obj)
            throws NoSuchAlgorithmException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
        if (obj == null) {
            return "null";
        }

        StringBuilder sbRet = new StringBuilder();

        stringfy(sbRet, obj, obj.getClass());

        return sbRet.toString();
    }

    private void stringifyObject(StringBuilder sb, Object obj)
            throws IllegalArgumentException, IllegalAccessException, NoSuchAlgorithmException, ClassNotFoundException {
        sb.append(CHAR_OPEN_OBJECT);
        List<Field> fields = getAllFields(obj).values().stream().toList();
        for (int i = 0; i < fields.size(); i++) {
            Field f = fields.get(i);
            f.setAccessible(true);

            if (f.get(obj) == null) {
                String key = getAnnotationValueOrFieldName(f);
                sb.append(key);
                sb.append(CHAR_EQUALITY);
                sb.append("null");
                if (i < fields.size() - 1)
                    sb.append(CHAR_CLOSE_VALUE);
                continue;
            }

            String key = getAnnotationValueOrFieldName(f);

            if (f.getType() == String.class) {
                sb.append(key);
                sb.append(CHAR_EQUALITY);
                sb.append(escapeCharacters(f.get(obj).toString()));
            } else if (f.getType().isPrimitive() || numericalValueClasses.contains(f.getType())) {
                sb.append(key);
                sb.append(CHAR_EQUALITY);
                sb.append(String.valueOf(f.get(obj)));
            } else if (f.get(obj) instanceof List || f.get(obj) instanceof Map) {
                sb.append(key);
                sb.append(CHAR_EQUALITY);
                stringfy(sb, f.get(obj), obj.getClass());
            } else if (f.getType().isEnum()) {
                for (Object enumConstant : Class.forName(f.getType().getTypeName()).getEnumConstants()) {
                    if (((Enum) enumConstant).compareTo((Enum) f.get(obj)) == 0) {
                        sb.append(key);
                        sb.append(CHAR_EQUALITY);
                        sb.append(escapeCharacters(f.get(obj).toString()));
                        break;
                    }
                }
            } else if (f.get(obj) instanceof Object) {
                sb.append(key);
                sb.append(CHAR_EQUALITY);
                stringifyObject(sb, f.get(obj));
            }

            if (i < fields.size() - 1)
                sb.append(CHAR_CLOSE_VALUE);
        }
        sb.append(CHAR_CLOSE_OBJECT);
    }

    private String getAnnotationValueOrFieldName(Field field) {
        Annotation[] annotations = field.getDeclaredAnnotations();
        CompactoProperty myAnnotation = null;

        for (Annotation annotation : annotations) {
            if (annotation instanceof CompactoProperty) {
                myAnnotation = (CompactoProperty) annotation;
                break;
            }
        }
        if (myAnnotation != null) {
            return myAnnotation.value();
        }
        return field.getName();
    }

    private String escapeCharacters(String value) {
        return value
                .replace(CHAR_OPEN_OBJECT, "\\" + CHAR_OPEN_OBJECT)
                .replace(CHAR_CLOSE_OBJECT, "\\" + CHAR_CLOSE_OBJECT)
                .replace(CHAR_EQUALITY, "\\" + CHAR_EQUALITY)
                .replace(CHAR_CLOSE_VALUE, "\\" + CHAR_CLOSE_VALUE)
                .replace(CHAR_KEY_SEPARATOR, "\\" + CHAR_KEY_SEPARATOR)
                .replace(CHAR_OPEN_LIST, "\\" + CHAR_OPEN_LIST)
                .replace(CHAR_CLOSE_LIST, "\\" + CHAR_CLOSE_LIST)
                .replace(CHAR_OPEN_MAP, "\\" + CHAR_OPEN_MAP)
                .replace(CHAR_CLOSE_MAP, "\\" + CHAR_CLOSE_MAP);

    }

    private String unescapeCharacters(String value) {
        return value
                .replace("\\" + CHAR_OPEN_OBJECT, CHAR_OPEN_OBJECT)
                .replace("\\" + CHAR_CLOSE_OBJECT, CHAR_CLOSE_OBJECT)
                .replace("\\" + CHAR_EQUALITY, CHAR_EQUALITY)
                .replace("\\" + CHAR_CLOSE_VALUE, CHAR_CLOSE_VALUE)
                .replace("\\" + CHAR_KEY_SEPARATOR, CHAR_KEY_SEPARATOR)
                .replace("\\" + CHAR_OPEN_LIST, CHAR_OPEN_LIST)
                .replace("\\" + CHAR_CLOSE_LIST, CHAR_CLOSE_LIST)
                .replace("\\" + CHAR_OPEN_MAP, CHAR_OPEN_MAP)
                .replace("\\" + CHAR_CLOSE_MAP, CHAR_CLOSE_MAP);
    }

    @Override
    public Object fromCompacto(String input, Type type) throws CompactoException {
        Object result = null;
        try {
            try {
                result = Class.forName(type.getTypeName()).getConstructor().newInstance();
            } catch(Exception e) {
                throw new CompactoException("Invalid class constructor");
            }

            for (int i = 0; i < input.length(); i++) {
                if (input.charAt(i) == CH_OPEN_OBJECT) {
                    int nextCloseObj = -1;
                    try {
                        nextCloseObj = getEnclosingIndex(input, i);
                    } catch (OpeningCharacterNotFound e) {
                        e.printStackTrace();
                    }
                    if (nextCloseObj == -1) {
                        throw createException(1, input, CHAR_CLOSE_OBJECT, null);
                    }
                    String objInput = input.substring(i + 1, nextCloseObj);
                    try {
                        instantiateAttributes(result, objInput);
                    } catch (OpeningCharacterNotFound e) {
                        throw createException(-1, input, objInput, e);
                    }
                    i = nextCloseObj;
                }
            }
        } catch (Exception e) {
            throw new CompactoException(e);
        }

        return result;
    }

    private Integer getNextEqualityIndex(String input, int start) {
        for (int i = start; i < input.length(); i++) {
            if (input.charAt(i) == CH_EQUALITY) {
                if (i == 0)
                    return 0;
                if ((i - 1) >= 0) {
                    if (input.charAt((i - 1)) != '\\') {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    private void instantiateAttributes(Object obj, String input)
            throws IllegalArgumentException, IllegalAccessException, SecurityException,
            InstantiationException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException,
            OpeningCharacterNotFound {
        Map<String, Field> objFields = getAllFields(obj);
        for (int i = 0; i < input.length(); i++) {
            int nextEqual = getNextEqualityIndex(input, i);
            String varName = input.substring(i, nextEqual);
            if (varName.isEmpty()) {
                throw createException(2, input, null, null);
            }
            String varValue = input.substring(nextEqual + 1);// pega até o fim da string

            if (varValue.charAt(0) == CH_OPEN_OBJECT || varValue.charAt(0) == CH_OPEN_LIST
                    || varValue.charAt(0) == CH_OPEN_MAP) {
                int nextClose = -1;
                try {
                    nextClose = getEnclosingIndex(varValue, 0);
                } catch (OpeningCharacterNotFound e) {
                    e.printStackTrace();
                }
                varValue = varValue.substring(1, nextClose);
                i += (varName.length() + 1 + 2 + varValue.length());// +1 do igual e +2 do abre e fecha
            } else {
                i = nextEqual;
                boolean foundTheValue = false;
                do {
                    if (Arrays.asList(CH_VALUE_SEPARATOR, CH_CLOSE_LIST, CH_CLOSE_OBJECT).contains(input.charAt(i))) {
                        if (i > 0 && input.charAt(i - 1) != '\\') {
                            varValue = input.substring(nextEqual + 1, i);
                            foundTheValue = true;
                        }
                    }
                    if (!foundTheValue)
                        i++;
                } while (i < input.length() && !foundTheValue);
            }

            Field field = objFields.get(varName);
            if (field == null)
                continue;

            field.setAccessible(true);
            if (varValue.equals("null") || ((Class) field.getType()).isArray()) {
                field.set(obj, null);
            } else if (field.getType() == String.class) {
                field.set(obj, unescapeCharacters(varValue));
            } else if (field.getType() == Integer.class) {
                field.set(obj, Integer.parseInt(varValue));
            } else if (field.getType() == Long.class) {
                field.set(obj, Long.parseLong(varValue));
            } else if (field.getType() == Float.class) {
                field.set(obj, Float.parseFloat(varValue));
            } else if (field.getType() == Double.class) {
                field.set(obj, Double.parseDouble(varValue));
            } else if (field.getType() == Boolean.class) {
                field.set(obj, Boolean.parseBoolean(varValue));
            } else if (field.getType() == BigInteger.class) {
                field.set(obj, new BigInteger(varValue));
            } else if (field.getType() == BigDecimal.class) {
                field.set(obj, new BigDecimal(varValue));
            } else if (field.getType() == List.class) {
                if (field.getGenericType() instanceof ParameterizedType) {
                    List list = createListOfType(field.getClass());
                    field.set(obj, list);

                    populateList(list, ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0],
                            varValue);
                } else {
                    logger.warning("Will not read not parameterized generic list/map");
                }
            } else if (field.getType() == Map.class) {
                if (field.getGenericType() instanceof ParameterizedType) {
                    Map map = createMapOfType(
                            ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getClass(),
                            ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[1].getClass());
                    field.set(obj, map);

                    populateMap(map,
                            ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0],
                            ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[1],
                            varValue);
                } else {
                    // TODO: tratamento padrão, criar mapa vazio OU simplesmente logar
                    logger.warning("Will not read not parameterized generic list/map");
                }
            } else if (field.getType().isEnum()) {
                for (Object enumConstant : field.getType().getEnumConstants()) {
                    if (((Enum) enumConstant).name().equals(varValue)) {
                        field.set(obj, enumConstant);
                        break;
                    }
                }
            } else {// if (Object.class.isAssignableFrom(field.getType())) {
                Object newObject = field.getType().getDeclaredConstructor().newInstance();
                field.set(obj, newObject);
                instantiateAttributes(newObject, varValue);
            }
        }
    }

    /**
     * 
     * @param obj
     * @param field
     * @param input
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws ClassNotFoundException
     * @throws OpeningCharacterNotFound
     * @throws NoSuchFieldException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void populateList(List list, Type type, String input)
            throws IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException,
            NoSuchMethodException, SecurityException, ClassNotFoundException, OpeningCharacterNotFound {
        int endOfLastItem = 0;
        for (int i = 0; i <= input.length(); i++) {
            if (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType() == List.class) {
                if (i < input.length() && input.charAt(i) == CH_OPEN_LIST) {
                    int nextClose = -1;
                    try {
                        nextClose = getEnclosingIndex(input, i);
                    } catch (OpeningCharacterNotFound e) {
                        throw createException(-1, input, null, null);

                    }
                    ArrayList itemList = (ArrayList) createListOfType(
                            ((ParameterizedType) type).getActualTypeArguments()[0].getClass());
                    list.add(itemList);

                    populateList(itemList, ((ParameterizedType) type).getActualTypeArguments()[0],
                            input.substring(i + 1, nextClose));
                    i += nextClose + 1;
                }
            } else if (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType() == Map.class) {
                // Map is item of list
                if (i < input.length() && input.charAt(i) == CH_OPEN_MAP) {
                    int nextClose = -1;
                    try {
                        nextClose = getEnclosingIndex(input, i);
                    } catch (OpeningCharacterNotFound e) {
                        e.printStackTrace();
                        throw createException(-1, input, null, null);
                    }

                    Map itemMap = (Map) createMapOfType(
                            ((ParameterizedType) type).getActualTypeArguments()[0].getClass(),
                            ((ParameterizedType) type).getActualTypeArguments()[1].getClass());
                    list.add(itemMap);

                    populateMap(itemMap, ((ParameterizedType) type).getActualTypeArguments()[0],
                            ((ParameterizedType) type).getActualTypeArguments()[1], input.substring(i + 1, nextClose));
                    i += nextClose + 1;
                    endOfLastItem = i;
                }
            } else {
                if (i < input.length() && (input.charAt(i) == CH_OPEN_LIST || input.charAt(i) == CH_OPEN_MAP
                        || input.charAt(i) == CH_OPEN_OBJECT)) {
                    int nextCloseObj = -1;
                    try {
                        nextCloseObj = getEnclosingIndex(input, i);
                    } catch (OpeningCharacterNotFound e) {
                        throw createException(3, input, null, e);
                    }
                    String itemValueStr = input.substring(endOfLastItem + 1, nextCloseObj);
                    endOfLastItem = nextCloseObj + 2;
                    i = nextCloseObj + 1;
                    list.add(instantiateFromStringByClass(type, itemValueStr));
                } else if (i >= input.length() || input.charAt(i) == CH_VALUE_SEPARATOR) {
                    String itemValueStr = input.substring(endOfLastItem, i);
                    endOfLastItem = i + 1;

                    if (type == String.class) {
                        String value = (String) itemValueStr;
                        list.add(value);
                    } else if (!itemValueStr.isEmpty() && !itemValueStr.isBlank()) {
                        if (type == Integer.class) {
                            list.add(Integer.parseInt(itemValueStr));
                        } else if (type == Long.class) {
                            list.add(Long.parseLong(itemValueStr));
                        } else if (type == Float.class) {
                            list.add(Float.parseFloat(itemValueStr));
                        } else if (type == Double.class) {
                            list.add(Double.parseDouble(itemValueStr));
                        } else if (type == Boolean.class) {
                            list.add(Boolean.parseBoolean(itemValueStr));
                        } else if (type == BigDecimal.class) {
                            list.add(new BigDecimal(itemValueStr));
                        } else if (type == BigInteger.class) {
                            list.add(new BigInteger(itemValueStr));
                        } else if (type == List.class) {
                            throw new RuntimeException("Essa exceção não deveria acontecer.");
                        } else if (Class.forName(type.getTypeName()).isEnum()) {
                            for (Object enumConstant : Class.forName(type.getTypeName()).getEnumConstants()) {
                                if (((Enum) enumConstant).name().equals(itemValueStr)) {
                                    list.add(enumConstant);
                                    break;
                                }
                            }
                        } else {
                            int nextCloseObj = -1;
                            try {
                                nextCloseObj = getEnclosingIndex(itemValueStr, 0);
                            } catch (OpeningCharacterNotFound e) {
                                throw createException(3, input, null, e);
                            }
                            String objInput = itemValueStr.substring(1, nextCloseObj);
                            Object newObject = Class.forName(type.getTypeName()).getDeclaredConstructor().newInstance();
                            instantiateAttributes(newObject, objInput);
                            list.add(newObject);
                        }
                    }
                }
            }

            if (i == input.length()) {
                break;
            }
        }
    }

    private static List createListOfType(Class type) {
        return new ArrayList();
    }

    private RuntimeException createException(int code, String input, String strAux, Object objAux) {
        int start = 0;
        String chunkMalformedInput = null;

        if (input != null) {
            start = input.length() - 32;
            start = start < 0 ? 0 : start;
            chunkMalformedInput = input.substring(start);
        }

        if (code == 1) {
            String message = MessageFormat.format("Missing closing character in input: {0}{1}{2}\n",
                    "'",
                    chunkMalformedInput, "'");
            return new MalFormatException(message, new Throwable("'" + strAux + "' is missing."));
        }

        if (code == 2) {
            String message = MessageFormat.format("Missing field name in input: {0}{1}{2}\n", "'",
                    chunkMalformedInput, "'");
            return new MalFormatException(message, new Throwable("field name is missing."));
        }

        if (code == 3) {
            String message = MessageFormat.format("Opening character mismatched in input: {0}{1}{2}\n",
                    "'",
                    chunkMalformedInput, "'");
            return new MalFormatException(message, new Throwable(((OpeningCharacterNotFound) objAux).getMessage()));
        }

        if (code == 4) {
            if (input != null) {
                start = input.indexOf(strAux) - 1;
                start = start < 0 ? 0 : start;
                chunkMalformedInput = input.substring(start);
                String message = MessageFormat.format("Invalid field {0}{1}{2} in input: {3}\n",
                        "'",
                        strAux, "'", chunkMalformedInput);
                return new MalFormatException(message,
                        new Throwable(MessageFormat.format("{0}{1}{2} is an invalid field name.", "'", strAux, "'")));
            } else {
                return new MalFormatException("Will never happen");
            }
        }

        if (code == 5) {
            return new InvalidObjectException(
                    "Object is not parseable. Verify if it implements 'CompactoSerializable' interface.");
        }

        return new MalFormatException("Ic!");
    }

    private static <K, V> Map<K, V> createMapOfType(Class<K> typeKey, Class<V> typeValue) {
        // private static <K, V, M> Map<K, V> createMapOfType(Class<M> type) {
        return new HashMap<K, V>();
    }

    private void stringifyMap(StringBuilder sb, Object obj, Type type)
            throws NoSuchAlgorithmException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
        sb.append(CHAR_OPEN_MAP);
        Iterator iterator = ((Map) obj).keySet().iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            Object value = ((Map) obj).get(key);
            String keyString = stringifyKey(key);

            if (value == null) {
                sb.append(key + CHAR_KEY_SEPARATOR + "null");
                if (iterator.hasNext())
                    sb.append(CHAR_CLOSE_VALUE);
                continue;
            } else if (value instanceof List || value instanceof Map) {
                sb.append(keyString);
                sb.append(CHAR_KEY_SEPARATOR);
                stringfy(sb, value, Class.forName(type.getTypeName()));
            } else if (numericalValueClasses.contains(value.getClass())) {
                sb.append(keyString);
                sb.append(CHAR_KEY_SEPARATOR);
                sb.append(value.toString());
            } else if (String.class == value.getClass()) {
                sb.append(keyString);
                sb.append(CHAR_KEY_SEPARATOR);
                sb.append(escapeCharacters(value.toString()));
            } else {// if (value instanceof CompactoSerializable) {
                sb.append(keyString);
                sb.append(CHAR_KEY_SEPARATOR);
                stringifyObject(sb, value);
            }
            if (iterator.hasNext())
                sb.append(CHAR_CLOSE_VALUE);
        }
        sb.append(CHAR_CLOSE_MAP);
    }

    private void populateMap(Map map, Type keyType, Type valueType, String input)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException, OpeningCharacterNotFound {
        int endOfLastValue = 0;
        Boolean keyFound = false;
        Boolean valueFound = false;
        String inputKey = "";
        String inputValue = "";

        for (int i = 0; i <= input.length(); i++) {
            if (!keyFound) {
                if (input.charAt(i) == CH_OPEN_LIST || input.charAt(i) == CH_OPEN_MAP
                        || input.charAt(i) == CH_OPEN_OBJECT) {
                    int nextClose = -1;
                    try {
                        nextClose = getEnclosingIndex(input, i);
                    } catch (OpeningCharacterNotFound e) {
                        e.printStackTrace();
                        throw createException(-1, input, null, null);
                    }

                    inputKey = input.substring(endOfLastValue + 1, nextClose);
                    endOfLastValue = nextClose + 2;
                    i = endOfLastValue;
                    keyFound = true;
                } else if (input.charAt(i) == CH_KEY_SEPARATOR) {
                    inputKey = input.substring(endOfLastValue, i);
                    endOfLastValue = i + 1;
                    keyFound = true;
                }
            } else if (!valueFound || i >= input.length()) {
                if (i >= input.length()) {
                    inputValue = input.substring(endOfLastValue, i);
                    endOfLastValue = i + 1;
                    valueFound = true;
                } else if (input.charAt(i) == CH_VALUE_SEPARATOR) {
                    inputValue = input.substring(endOfLastValue, i);
                    endOfLastValue = i + 1;
                    valueFound = true;
                } else if (input.charAt(i) == CH_OPEN_LIST || input.charAt(i) == CH_OPEN_MAP
                        || input.charAt(i) == CH_OPEN_OBJECT) {
                    int nextClose = -1;
                    try {
                        nextClose = getEnclosingIndex(input, i);
                    } catch (OpeningCharacterNotFound e) {
                        e.printStackTrace();
                        throw createException(-1, input, null, null);
                    }

                    inputValue = input.substring(endOfLastValue + 1, nextClose);
                    endOfLastValue = nextClose + 2;
                    i = endOfLastValue;
                    valueFound = true;
                }
            }

            if (keyFound && valueFound) {
                Object key = instantiateFromStringByClass(keyType, inputKey);
                Object value = instantiateFromStringByClass(valueType, inputValue);
                map.put(key, value);
                keyFound = false;
                valueFound = false;
            }
        }
    }

    private Object instantiateFromStringByClass(Type valueType, String input)
            throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, SecurityException,
            InstantiationException, InvocationTargetException, NoSuchMethodException, OpeningCharacterNotFound {
        if (valueType == String.class) {
            return input;
        }
        if (valueType == Integer.class) {
            return Integer.parseInt(input);
        }
        if (valueType == Long.class) {
            return Long.valueOf(input);
        }
        if (valueType == Float.class) {
            return Float.parseFloat(input);
        }
        if (valueType == Double.class) {
            return Double.parseDouble(input);
        }
        if (valueType == Boolean.class) {
            return Boolean.parseBoolean(input);
        }
        if (valueType == BigDecimal.class) {
            return new BigDecimal(input);
        }

        if (valueType instanceof ParameterizedType && ((ParameterizedType) valueType).getRawType() == List.class) {
            ArrayList list = (ArrayList) createListOfType(
                    ((ParameterizedType) valueType).getActualTypeArguments()[0].getClass());
            populateList(list, ((ParameterizedType) valueType).getActualTypeArguments()[0], input);
            return list;
        }

        if (valueType instanceof ParameterizedType && ((ParameterizedType) valueType).getRawType() == Map.class) {
            Map map = createMapOfType(
                    ((ParameterizedType) valueType).getActualTypeArguments()[0].getClass(),
                    ((ParameterizedType) valueType).getActualTypeArguments()[1].getClass());

            populateMap(map, ((ParameterizedType) valueType).getActualTypeArguments()[0],
                    ((ParameterizedType) valueType).getActualTypeArguments()[1], input);
            return map;
        }

        Object resultObject = Class.forName(valueType.getTypeName()).getDeclaredConstructor().newInstance();
        instantiateAttributes(resultObject, input);
        return resultObject;
    }

    private boolean hasRestrictModifiers(Field field) {
        return (hasModifier(field, Modifier.PRIVATE)) && hasModifier(field, Modifier.STATIC)
                && hasModifier(field, Modifier.FINAL);
    }

    private boolean hasModifier(Field field, int modifier) {
        return (field.getModifiers() & modifier) != 0;
    }

    private Map<String, Field> getAllFields(Object obj) {
        Map<String, Field> fields = new HashMap<>();

        Class<?> objSuperclass = obj.getClass();
        while (objSuperclass != Object.class) {
            Arrays.asList(objSuperclass.getDeclaredFields()).stream()
                    .filter(f -> !hasRestrictModifiers(f)).forEach(f -> {
                        fields.put(f.getName(), f);
                    });
            objSuperclass = objSuperclass.getSuperclass();
        }

        return fields;
    }
}
