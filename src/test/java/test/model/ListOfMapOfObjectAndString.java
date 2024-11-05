package test.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.compacto.parser.model.CompactoSerializable;

public class ListOfMapOfObjectAndString implements CompactoSerializable {
    List<Map<XWrapper, String>> lista = new ArrayList<>();

    public List<Map<XWrapper,String>> getLista() {
        return this.lista;
    }

    public void setLista(List<Map<XWrapper,String>> lista) {
        this.lista = lista;
    }
}
