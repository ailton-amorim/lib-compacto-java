package test.model;

import java.util.HashMap;
import java.util.Map;

import org.compacto.parser.model.CompactoSerializable;

public class IntegerStringMap implements CompactoSerializable {
    private Map<Integer, String> mapaDeEstados = new HashMap<>();

    public Map<Integer,String> getMapaDeEstados() {
        return this.mapaDeEstados;
    }

    public void setMapaDeEstados(Map<Integer,String> mapaDeEstados) {
        this.mapaDeEstados = mapaDeEstados;
    }
    
}
