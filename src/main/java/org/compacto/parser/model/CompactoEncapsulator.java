package org.compacto.parser.model;

public class CompactoEncapsulator<T> implements CompactoSerializable {
    private Object object;

    public CompactoEncapsulator() {}

    public Object getObject() {
        return this.object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

}
