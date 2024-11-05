package test.model;

public class IntegerPoint {
    private static final long serialVersionUID = -74505709L;

    private Integer x = 0, y = 0;

    public IntegerPoint() {
    }

    public IntegerPoint(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return this.x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return this.y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
