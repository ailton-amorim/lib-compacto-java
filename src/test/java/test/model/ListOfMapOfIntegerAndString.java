package test.model;

import java.util.List;
import java.util.Map;

import org.compacto.parser.model.CompactoSerializable;

public class ListOfMapOfIntegerAndString implements CompactoSerializable {
    private List<Map<Integer, String>> lista;

    public List<Map<Integer,String>> getLista() {
        return this.lista;
    }

    public void setLista(List<Map<Integer,String>> lista) {
        this.lista = lista;
    }
}
