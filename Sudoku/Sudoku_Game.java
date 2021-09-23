/** By: Jetmir Halili */

/** This interface deals with the methods that a sudoku game should have */
public interface Sudoku_Game{
   
   /** Creates a new sudoku
       @return a two-dimensional array of size 9x9 */
   public int[][] create();

   /** Returns this sudoku to the initial state, ie initializes all values to zero */
   public void clear();

   /** This method only initializes the values that the user will see at the beginning of the game
       @param rules are the rules of the game (normal, x, king rules)
       @param level is the level of the game (easy, medium, hard) */
   public void initializes(boolean[] rules, int level);

   /** Checks a user input if the given value is correct and updates the filledCells variable
       @param x is the value that is checked if it is correct
       @param row is the value row that we will check
       @param column is the value column that we will check
       @return true if the input is correct otherwise it returns false */
   public boolean checkInput(int x, int row, int column);
      
   /** Deals with the use of the game
        @return true if the user has filled in all the cells, otherwise returns false */
   public boolean hasEnd();
   
}