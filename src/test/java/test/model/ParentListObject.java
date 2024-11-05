package test.model;

import java.util.List;

public class ParentListObject extends GrandListObject {
    private Integer parentValue = 1999;
    private List<Integer> parentList;

    public List<Integer> getParentList() {
        return this.parentList;
    }

    public void setParentList(List<Integer> parentList) {
        this.parentList = parentList;
    }

    public Integer getParentValue() {
        return this.parentValue;
    }

    public void setParentValue(Integer parentValue) {
        this.parentValue = parentValue;
    }
}
