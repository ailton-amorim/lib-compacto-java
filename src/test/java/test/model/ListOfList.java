package test.model;

import java.util.List;

import org.compacto.parser.model.CompactoSerializable;

public class ListOfList implements CompactoSerializable {
    List<List<String>> list;

    public List<List<String>> getList() {
        return list;
    }
}
