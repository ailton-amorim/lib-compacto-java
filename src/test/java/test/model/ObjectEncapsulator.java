package test.model;

import org.compacto.parser.model.CompactoSerializable;

public class ObjectEncapsulator implements CompactoSerializable {
    Object object;

    public Object getObject() {
        return this.object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
