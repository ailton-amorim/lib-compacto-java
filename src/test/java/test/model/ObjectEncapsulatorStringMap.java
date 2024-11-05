package test.model;

import java.util.HashMap;
import java.util.Map;

import org.compacto.parser.model.CompactoSerializable;

public class ObjectEncapsulatorStringMap implements CompactoSerializable {
    private Map<XWrapper, String> mapa = new HashMap<>();

    public Map<XWrapper,String> getMapa() {
        return this.mapa;
    }

    public void setMapa(Map<XWrapper,String> mapa) {
        this.mapa = mapa;
    }
}
