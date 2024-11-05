package test.model;

import java.util.List;

import org.compacto.parser.model.CompactoSerializable;

public class ListOfEnumObject implements CompactoSerializable {

    private List<EstadoCivil> list;

    public List<EstadoCivil> getList() {
        return this.list;
    }

    public void setList(List<EstadoCivil> list) {
        this.list = list;
    }

}
