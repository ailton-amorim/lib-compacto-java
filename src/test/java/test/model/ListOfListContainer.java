package test.model;

import org.compacto.parser.model.CompactoSerializable;

public class ListOfListContainer implements CompactoSerializable {
    private ListOfList obj;

    public ListOfListContainer() {}

    public ListOfListContainer(ListOfList obj) {
        this.obj = obj;
    }

    public void setObj(ListOfList obj) {
        this.obj = obj;
    }

    public ListOfList getObj() {
        return obj;
    }
}
