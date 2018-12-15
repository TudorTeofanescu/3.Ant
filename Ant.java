public class Ant {
  // get direction of ant
  private Direction direction = new Direction();
  private Integer width;
  private Integer height;

  public Direction getOrientation() {
    return direction;
  }

  @Override
  public String toString() {
    return direction.toString();
  }

  public Integer getX() {
    return width;
  }

  public void setX(Integer x) {
    this.width = x;
  }

  public Integer getY() {
    return height;
  }

  public void setY(Integer y) {
    this.height = y;
  }
}