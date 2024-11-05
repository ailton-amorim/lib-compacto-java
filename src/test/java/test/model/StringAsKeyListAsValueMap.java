package test.model;

import java.util.List;
import java.util.Map;

import org.compacto.parser.model.CompactoSerializable;

public class StringAsKeyListAsValueMap implements CompactoSerializable {
    private Map<String, List<String>> mapa;

    public Map<String,List<String>> getMapa() {
        return this.mapa;
    }

    public void setMapa(Map<String,List<String>> mapa) {
        this.mapa = mapa;
    }
}
