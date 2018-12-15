import java.util.*;

public class AntGrid implements Grid {

  private final ArrayList<Integer> configuration = new ArrayList<>(); // 0 = Left; 1=Right
  private LinkedList<LinkedList<AntCell>> playField = new LinkedList<>();
  private Map<AntCell, Ant> ants = new HashMap<>();
  private Integer width = 0;
  private Integer height = 0;
  private Stack<AntCell> colorBack = new Stack<>();
  private Stack<Integer> directionBack = new Stack<>();
  private Integer stepCount = 0;

  /**
   * Contructor that creates the Grid.
   * @param x width
   * @param y height
   * @param configuration R,L sequence
   */
  public AntGrid(int x, int y, String configuration) {
    this.width = x;
    this.height = y;
    for (int i = 0; i < height; i++) {
      LinkedList<AntCell> spalte = new LinkedList<>();
      for (int j = 0; j < width; j++) {
        spalte.add(new AntCell());
      }
      playField.add(spalte);
    }
    for (int i = 0; i < configuration.length(); i++) {
      if (configuration.charAt(i) == 'L') {
        this.configuration.add(0);
      } else {
        this.configuration.add(1);
      }
    }
  }

  @Override
  public void setAnt(Ant object, int col, int row) {
    if (col < width && row < height && col >= 0 && row >= 0) {
      object.setX(col);
      object.setY(row);
      ants.clear();
      AntCell antCell = playField.get(row).get(col);
      ants.put(antCell, object);
    } else {
      System.out.println("Error! Ant must be on the current grid.");
    }
  }

  @Override
  public Map<AntCell, Ant> getAnts() {
    return ants;
  }

  @Override
  public void clearAnts() {
    ants = new HashMap<>();
  }

  @Override
  public void performStep() {
    for (Map.Entry<AntCell, Ant> e : ants.entrySet()) {
      AntCell cell = e.getKey();
      Ant ant = e.getValue();

      stepCount++;
      colorBack.push(cell);
      directionBack.push(ant.getOrientation().getDirection());
      int color = cell.getState();
      //set it to the next color that also has a configuration allocated
      cell.setCurrentColor((color + 1) % configuration.size());
      int dir = ant.getOrientation().getDirection();
      //place the ant to the new position
      switch (dir) {
        case 0:
          int y = ant.getY() - 1;
          if (y < 0) {
            y = height - 1;
          }
          setAnt(ant, ant.getX(), y);
          break;
        case 1:
          setAnt(ant, (ant.getX() + 1) % width, ant.getY());
          break;
        case 2:
          setAnt(ant, ant.getX(), (ant.getY() + 1) % height);
          break;
        default:
          int x = ant.getX() - 1;
          if (x < 0) {
            x = width - 1;
          }
          setAnt(ant, x, ant.getY());
          break;
      }
      //update ant direction
      for (AntCell antCell : ants.keySet()) {
        cell = antCell;
      }
      color = cell.getState();
      if (configuration.get(color) == 1) {
        ant.getOrientation().setDirection(ant.getOrientation().getDirection() + 1); //face right
      } else {
        ant.getOrientation().setDirection(ant.getOrientation().getDirection() - 1); //face left
      }
    }
  }

  @Override
  public void performStep(int number) {
    if (number > 0) {
      for (int i = 0; i < number; i++) {
        this.performStep();
      }
    }
  }

  @Override
  public void reset(int number) {
    int n = number;
    if (number > stepCount) {
      n = stepCount;
      stepCount = 0;
    } else {
      stepCount -= number;
    }
    AntCell cell = null;
    Ant ant = null;
    for (Ant a : ants.values()) {
      ant = a;
    }
    for (int i = 0; i < n; i++) {
      if (!colorBack.empty()) {
        cell = colorBack.pop();
        int x = cell.getState() - 1;
        if (x < 0) {
          x = configuration.size() - 1;
        }
        ant.getOrientation().setDirection(directionBack.pop());
        cell.setCurrentColor(x % configuration.size());
      }
    }
    ants.clear();
    ants.put(cell, ant);
    System.out.println(this.getStepCount());
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public List<AntCell> getColumn(int i) {
    List<AntCell> antCellsColum = new LinkedList<>();
    for (LinkedList<AntCell> listElem : playField) {
      antCellsColum.add(listElem.get(i));
    }
    return antCellsColum;
  }

  @Override
  public List<AntCell> getRow(int j) {
    return playField.get(j);
  }

  @Override
  public void resize(int cols, int rows) {
    cols = cols - width;
    rows = rows - height;

    if (cols >= 0) {
      for (LinkedList<AntCell> row : playField) {
        int c = cols;
        while (c > 1) {
          row.add(new AntCell());
          row.addFirst(new AntCell());
          c -= 2;
        }
        if (c == 1) {
          row.add(new AntCell());
        }
      }
    } else { // <0, deleting colums
      for (LinkedList<AntCell> row : playField) {
        int c = -cols;
        while (c > 1) {
          row.removeLast();
          row.remove();
          c -= 2;
        }
        if (c == 1) {
          row.removeLast();
        }
      }
    }
    width = width + cols;

    if (rows >= 0) {
      int r = rows;
      while (r > 0) {
        LinkedList<AntCell> listFirst = new LinkedList<>();
        LinkedList<AntCell> listLast = new LinkedList<>();
        for (int i = 0; i < playField.get(0).size(); i++) {
          listFirst.add(new AntCell());
          listLast.add(new AntCell());
        }
        playField.add(listLast);
        r--;
        if (r > 0) {
          playField.addFirst(listFirst);
          r--;
        }
      }
    } else { // <0 delete rows
      int r = -rows;
      while (r > 0) {
        playField.removeLast();
        playField.remove();
        r -= 2;
        if (r == 1) {
          playField.removeLast();
          r--;
        }
      }
    }
    height = height + rows;
  }

  @Override
  public void clear() {
    while (!colorBack.empty()) {
      colorBack.pop();
    }
    stepCount = 0;
    this.clearAnts();
    playField = new LinkedList<>();
    for (int i = 0; i < width; i++) {
      LinkedList<AntCell> spalte = new LinkedList<>();
      for (int j = 0; j < height; j++) {
        AntCell z = new AntCell();
        spalte.add(z);  // Neue Element hinzufügen!
      }
      playField.add(spalte); // Neue Spalte hinzufügen!
    }
  }

  @Override
  public int getStepCount() {
    return stepCount;
  }

  /**
   * Prints the Grid.
   */
  public void printGrid() {
    AntCell onlyAntCell = new AntCell();
    for (AntCell antCell : ants.keySet()) {
      onlyAntCell = antCell;
    }
    Collection<Ant> ant = ants.values();
    Ant aaa = new Ant();
    for (Ant a : ant) {
      aaa = a;
    }
    for (LinkedList<AntCell> colum : playField) {
      for (AntCell row : colum) {
        if (row.equals(onlyAntCell)) {
          System.out.print(row.getColor() + aaa.getOrientation() + "\u001B[0m");
        } else {
          System.out.print(row);
        }
      }
      System.out.println();
    }
  }

}
