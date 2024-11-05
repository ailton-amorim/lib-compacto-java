package test.model;

import org.compacto.parser.model.CompactoSerializable;

public class EmptyObjectWrapper implements CompactoSerializable {
    public EmptyObject object = new EmptyObject();

    public EmptyObjectWrapper() {
    }

    public EmptyObjectWrapper(EmptyObject object) {
        this.object = object;
    }

    public EmptyObject getObject() {
        return object;
    }

    public void setObject(EmptyObject object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "EmptyObjectWrapper [object=" + object + "]";
    }

}
