package test.model;

import java.util.Map;

import org.compacto.parser.model.CompactoSerializable;

public class LongAsKeyObjectAsValueMap implements CompactoSerializable {
    private Map<Long, EmptyObject> mapa;

    public LongAsKeyObjectAsValueMap() {
    }

    public LongAsKeyObjectAsValueMap(Map<Long, EmptyObject> mapa) {
        this.mapa = mapa;
    }

    public Map<Long, EmptyObject> getMapa() {
        return this.mapa;
    }

    public void setMapa(Map<Long, EmptyObject> mapa) {
        this.mapa = mapa;
    }
}
