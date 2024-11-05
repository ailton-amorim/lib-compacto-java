package test.model;

import java.util.List;
import java.util.Map;

import org.compacto.parser.model.CompactoSerializable;

public class ListContainer implements CompactoSerializable {
    private List<Map<Integer, List<ListOfEnumObject>>> object;

    public List<Map<Integer,List<ListOfEnumObject>>> getObject() {
        return this.object;
    }

    public void setObject(List<Map<Integer,List<ListOfEnumObject>>> object) {
        this.object = object;
    }
}
