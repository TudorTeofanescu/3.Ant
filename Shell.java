import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Shell {
  /**
   * Main methode.
   */
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in, "UTF-8");
    AntGrid antGrid = new AntGrid(0, 0, "");
    while (true) {
      System.out.print("ant> ");
      String command;
      try {
        command = input.next();//save the command
      } catch (Exception e) {
        break;
      }
      command = command.toLowerCase().trim();
      command = validCommand(command);//set the command to it's full name
      if (command.equals("quit") && justCommand(input, command)) {
        break;
      } else if (command.equals("new")) {
        ArrayList<String> parameters = checkInput(input, 3);
        if (parameters != null) {
          int x = Integer.parseInt(parameters.get(0));
          int y = Integer.parseInt(parameters.get(1));
          if (x <= 0 && y <= 0) {
            System.out.println("Error! Dimensions must be positive.");
          } else {
            String config = parameters.get(2);
            if (config.length() < 2) {
              System.out.println("Error! Invalid Configuration.");
            } else {
              antGrid = new AntGrid(x, y, config);
            }
          }
        }

      } else if (command.equals("help")) {
        if (justCommand(input, command)) {
          printHelp();
        }
      } else if (command.equals("ant")) {
        ArrayList<String> parameters = checkInput(input, 2);
        if (parameters != null) {
          int x = Integer.parseInt(parameters.get(0));
          int y = Integer.parseInt(parameters.get(1));
          antGrid.setAnt(new Ant(), x, y);
        }
      } else if (command.equals("unant")) {
        antGrid.clearAnts();
      } else if (command.equals("print")) {
        if (justCommand(input, command)) {
          antGrid.printGrid();
        }
      } else if (command.equals("clear")) {
        if (justCommand(input, command)) {
          antGrid.clear();
        }
      } else if (command.equals("resize")) {
        ArrayList<String> parameters = checkInput(input, 2);
        if (parameters != null) {
          int x = Integer.parseInt(parameters.get(0));
          int y = Integer.parseInt(parameters.get(1));
          antGrid.resize(x, y);
        }
      } else if (command.equals("step")) {
        String nextParam = input.nextLine();
        nextParam = nextParam.trim();
        if (nextParam.isEmpty()) {
          antGrid.performStep();
          System.out.println(antGrid.getStepCount());
        } else {
          try {
            int x = Integer.parseInt(nextParam);
            if (x > 0) {
              antGrid.performStep(x);
              System.out.println(antGrid.getStepCount());
            } else {
              antGrid.reset(-x);
            }
          } catch (Exception e) {
            System.out.println("Error! Invalid input");
          }
        }
      }
    }
  }


  /**
   * Checks if the command is valid.
   *
   * @param command contains the users command
   * @return false if the name is accepted, true if not supported
   */

  private static String validCommand(String command) {
    String[] possibleCommands = {"new", "ant", "unant", "step", "print",
        "clear", "resize", "help", "quit"};
    for (String possibleCommand : possibleCommands) { //check valid command
      for (int j = 0; j <= possibleCommand.length(); j++) {
        //command is no substring for any accepted commands
        if (command.equals(possibleCommand.substring(0, j))) {
          return possibleCommand;
        }
      }
    }
    return "";
  }

  private static void printHelp() {
    System.out.println("<---------------------------------------------------------->");
    System.out.println("NEW x y c    Starts a new game with size (x, y) and "
            + "configuration c just UPPERCASE (in RL).");
    System.out.println("ANT i j      Place ant in i-th column and j-th row. ");
    System.out.println("UNANT        Removes the ant. ");
    System.out.println("STEP [n]     Computes the next step/steps.");
    System.out.println("PRINT        Plays game field.");
    System.out.println("RESIZE x y   Changes the size of the playing field to (x, y).");
    System.out.println("QUIT         Ends the program");
    System.out.println("<---------------------------------------------------------->");
  }

  /**
   * makes sure that there is no parameter after the commands that don't need one.
   *
   * @param input   is the Scanner from witch we read
   * @param command contains the command in order to display it
   * @return true only if there is no command after
   */
  private static boolean justCommand(Scanner input, String command) {
    String extraInput = input.nextLine();
    extraInput = extraInput.trim(); // ignores the spaces at the beginning of the String
    if (extraInput.length() != 0) {
      System.out.println("Error! Command " + command + " must have 0 parameters.");
      return false;
    }
    return true;
  }

  /**
   * Check for the input after the command. Check if the 1st 2 are
   * numbers and if there are 3 values, the last should be a String.
   * This works for this commands, adapt it if first 2 should not be nr and last a string.
   * @param input the scanner from which we read
   * @return the values or empty array
   */
  private static ArrayList<String> checkInput(Scanner input, int paramNeeded) {
    String nextParam = input.nextLine();
    nextParam = nextParam.trim();
    String[] words = nextParam.split("\\W+");
    ArrayList<String> values = new ArrayList<>(); // in our case we have max 3 values after command
    if (!nextParam.isEmpty() && words.length == paramNeeded) {
      for (int i = 0; i < words.length; i++) {
        //the second argument must be written in Uppercase and be
        if (i == 2 && validSequence(words[i])) {
          values.add(words[i]);
        } else if (words[i].matches("\\d+") && i != 2) {
          values.add(words[i]);
        }
      }
    }
    if (values.size() == words.length) {
      return values;
    }
    System.out.println("Error! Invalid input.");
    return null;
  }

  private static boolean validSequence(String word) {
    // if doesn't have all letters in Uppercase or less than 13 caracters
    if (word.length() > 12) {
      return false;
    }
    // check if it consist just from letters
    for (int i = 0; i < word.length(); i++) {
      if (!(word.charAt(i) == 'R' || word.charAt(i) == 'L')) {
        return false;
      }
    }
    return true;
  }

}