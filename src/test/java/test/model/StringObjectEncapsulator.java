package test.model;

import org.compacto.parser.model.CompactoSerializable;

public class StringObjectEncapsulator implements CompactoSerializable {
    private String object;

    public StringObjectEncapsulator() {
    }

    public StringObjectEncapsulator(String object) {
        this.object = object;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "StringObjectEncapsulator [object=" + object + "]";
    }
}
