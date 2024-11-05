package org.compacto.parser.model;

import java.util.ArrayList;
import java.util.List;

public class ListCompactoSerializable implements CompactoSerializable {
    public List<CompactoSerializable> list = new ArrayList<>();

    public ListCompactoSerializable(List<CompactoSerializable> theList) {
        list = theList;
    }

    public List<CompactoSerializable> getList() {
        return list;
    }

    public void setList(List<CompactoSerializable> list) {
        this.list = list;
    }
}