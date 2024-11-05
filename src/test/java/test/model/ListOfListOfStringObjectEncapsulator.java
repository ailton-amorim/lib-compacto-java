package test.model;

import java.util.List;

import org.compacto.parser.model.CompactoSerializable;

public class ListOfListOfStringObjectEncapsulator implements CompactoSerializable {
    private List<List<StringObjectEncapsulator>> list;

    public List<List<StringObjectEncapsulator>> getList() {
        return list;
    }

    public void setList(List<List<StringObjectEncapsulator>> list) {
        this.list = list;
    }
}
