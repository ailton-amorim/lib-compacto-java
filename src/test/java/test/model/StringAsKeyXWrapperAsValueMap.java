package test.model;

import java.util.HashMap;
import java.util.Map;

import org.compacto.parser.model.CompactoSerializable;

public class StringAsKeyXWrapperAsValueMap implements CompactoSerializable {
    private Map<String, XWrapper> mapa = new HashMap<>();

    public Map<String,XWrapper> getMapa() {
        return this.mapa;
    }

    public void setMapa(Map<String,XWrapper> mapa) {
        this.mapa = mapa;
    }
}
