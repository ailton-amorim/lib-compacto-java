package test.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.compacto.parser.model.CompactoSerializable;

public class ListOfMapOfIntegerAndListOfString implements CompactoSerializable {
    List<Map<Integer, List<String>>> lista = new ArrayList<>();

    public List<Map<Integer,List<String>>> getLista() {
        return this.lista;
    }

    public void setLista(List<Map<Integer,List<String>>> lista) {
        this.lista = lista;
    }
}
