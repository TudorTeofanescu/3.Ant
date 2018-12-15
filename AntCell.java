public class AntCell implements Cell {
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String COLOR_0 = "\u001B[47m";
  public static final String COLOR_1 = "\u001B[37;40m";
  public static final String COLOR_2 = "\u001B[42m";
  public static final String COLOR_3 = "\u001B[41m";
  public static final String COLOR_4 = "\u001B[37;44m";
  public static final String COLOR_5 = "\u001B[43m";
  public static final String COLOR_6 = "\u001B[46m";
  public static final String COLOR_7 = "\u001B[45m";
  public static final String COLOR_8 = "\u001B[36;41m";
  public static final String COLOR_9 = "\u001B[31;44m";
  public static final String COLOR_10 = "\u001B[34;43m";
  public static final String COLOR_11 = "\u001B[32;45m";
  private String[] color = {COLOR_0, COLOR_1, COLOR_2, COLOR_3, COLOR_4, COLOR_5, COLOR_6,
      COLOR_7, COLOR_8, COLOR_9, COLOR_10, COLOR_11};
  private Integer currentColor;

  public AntCell() {
    currentColor = 0;
  }

  @Override
  public Integer getState() {
    return currentColor;
  }

  public void setCurrentColor(int x) {
    currentColor = x % 12;
  }

  public String getColor() {
    return color[currentColor];
  }

  @Override
  public String toString() {
    String c = "" + currentColor;
    if (currentColor == 10) {
      c = "A";
    } else if (currentColor == 11) {
      c = "B";
    }
    return color[currentColor] + c + ANSI_RESET;
  }


}
