package test.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.compacto.parser.model.CompactoSerializable;

public class ListOfMapOfIntegerAndListOfXWrapper implements CompactoSerializable {
    List<Map<Integer, List<XWrapper>>> lista = new ArrayList<>();

    public List<Map<Integer,List<XWrapper>>> getLista() {
        return this.lista;
    }

    public void setLista(List<Map<Integer,List<XWrapper>>> lista) {
        this.lista = lista;
    }

}
