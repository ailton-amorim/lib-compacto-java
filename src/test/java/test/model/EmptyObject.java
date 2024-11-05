package test.model;

import org.compacto.parser.model.CompactoSerializable;

/**
 * EmptyObject
 */

public class EmptyObject implements CompactoSerializable {
    public boolean equals(Object obj) {
        return obj != null && obj instanceof EmptyObject;
    }
}