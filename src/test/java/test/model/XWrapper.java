package test.model;

import org.compacto.parser.model.CompactoSerializable;

public class XWrapper implements CompactoSerializable {
    private Integer x = 0;

    public XWrapper() {
    }

    public XWrapper(int x) {
        this.x = x;
    }

    public Integer getX() {
        return this.x;
    }

    public void setX(Integer x) {
        this.x = x;
    }
}
