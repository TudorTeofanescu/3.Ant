public class Direction {
  private static final String ANTUP = "^";
  private static final String ANTRIGHT = ">";
  private static final String ANTDOWN = "v";
  private static final String ANTLEFT = "<";
  private String[] directions = {ANTUP, ANTRIGHT, ANTDOWN, ANTLEFT}; //clockwise
  private Integer direction;

  public Direction() {
    this.direction = 3;
  }

  public Integer getDirection() {
    return direction;
  }

  /**
   * Sets direction between 0-4 clockwise.
   * @param x the new position.
   */
  public void setDirection(Integer x) {
    while (x < 0) {
      x += 4;
    }
    direction = x % 4;
  }

  @Override
  public String toString() {
    return directions[direction];
  }
}

