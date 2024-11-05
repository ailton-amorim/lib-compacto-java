package org.compacto.parser;

import java.lang.reflect.Type;

import org.compacto.parser.exceptions.CompactoException;

public interface AbstractCompactoParser {
    String toCompacto(Object object, Type resolvedType) throws CompactoException;

    Object fromCompacto(String input, Type resolvedType) throws CompactoException;
}
