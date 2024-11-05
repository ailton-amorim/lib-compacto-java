package test.model;

import java.util.Map;

import org.compacto.parser.model.CompactoSerializable;

public class MapAsKeyMapAsValueMap implements CompactoSerializable {
    private Map<Integer, Map<String, StringObjectEncapsulator>> mapa;

    public Map<Integer, Map<String, StringObjectEncapsulator>> getMapa() {
        return this.mapa;
    }

    public void setMapa(Map<Integer, Map<String, StringObjectEncapsulator>> mapa) {
        this.mapa = mapa;
    }

}
