package org.compacto.parser;

import java.text.MessageFormat;
import org.compacto.parser.exceptions.OpeningCharacterNotFound;

public abstract class BaseCompactoParser implements AbstractCompactoParser {
    // indicador de fim de valor
    protected static final String CHAR_CLOSE_VALUE = ",";
    // separa nome e valor de um atributo
    protected static final String CHAR_EQUALITY = "=";
    // abre uma lista
    protected static final String CHAR_OPEN_LIST = "[";
    // fecha uma lista
    protected static final String CHAR_CLOSE_LIST = "]";
    // indica início de objeto
    protected static final String CHAR_OPEN_OBJECT = "{";
    // indica fim de objeto ou mapa
    protected static final String CHAR_CLOSE_OBJECT = "}";
    protected static final String CHAR_OPEN_MAP = "(";
    protected static final String CHAR_CLOSE_MAP = ")";
    // separa chave e valor em um mapa
    protected static final String CHAR_KEY_SEPARATOR = "#";

    protected char CH_OPEN_OBJECT = '{';
    protected char CH_CLOSE_OBJECT = '}';
    protected char CH_VALUE_SEPARATOR = ',';
    protected char CH_EQUALITY = '=';
    protected char CH_OPEN_LIST = '[';
    protected char CH_CLOSE_LIST = ']';
    protected char CH_KEY_SEPARATOR = '#';
    protected char CH_OPEN_MAP = '(';
    protected char CH_CLOSE_MAP = ')';

    /**
     * Returns the index of the closing character based on the opening character at
     * the given start index in the input string.
     *
     * @param input the input string
     * @param start the starting index of the opening character
     * @return the index of the closing character, or -1 if not found
     */
    public Integer getEnclosingIndex(String input, int start) throws OpeningCharacterNotFound {
        if (input.charAt(start) == CH_OPEN_OBJECT) {
            return getEnclosingIndexObject(input, start);
        } else if (input.charAt(start) == CH_OPEN_LIST) {
            return getEnclosingIndexList(input, start);
        } else if (input.charAt(start) == CH_OPEN_MAP) {
            return getEnclosingIndexMap(input, start);
        }
        throw new OpeningCharacterNotFound(
                MessageFormat.format("{0}{1}{2} is not an opening character.", "'", "" + input.charAt(start), "'"));
    }

    protected Integer getEnclosingIndexObject(String input, int start) {
        return start + getEnclosingIndex(input.substring(start), CH_OPEN_OBJECT, CH_CLOSE_OBJECT);
    }

    protected Integer getEnclosingIndexList(String input, int start) {
        return start + getEnclosingIndex(input.substring(start), CH_OPEN_LIST, CH_CLOSE_LIST);
    }

    protected Integer getEnclosingIndexMap(String input, int start) {
        return start + getEnclosingIndex(input.substring(start), CH_OPEN_MAP, CH_CLOSE_MAP);
    }

    /**
     * A function to get the index of the closing character based on the open and
     * close characters.
     *
     * @param input the input string
     * @param open  the opening character
     * @param close the closing character
     * @return the index of the closing character, or -1 if not found
     */
    private int getEnclosingIndex(String input, char open, char close) {
        int openCount = 0;
        int closeCount = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == open) {
                if ((i - 1) >= 0) {
                    if (input.charAt(i - 1) != '\\') {
                        openCount++;
                    }
                } else {
                    openCount++;
                }
            } else if (input.charAt(i) == close) {
                if ((i - 1) >= 0) {
                    if (input.charAt(i - 1) != '\\') {
                        closeCount++;
                    }
                } else {
                    closeCount++;
                }
            }
            if (openCount == closeCount) {
                return i;
            }
        }

        throw new RuntimeException(input);// createException(1, input, "" + close, null);
    }
}
