/** By: Jetmir Halili */

/** The Sudoku class implements the Sudoku_Game interface and deals with modeling a sudoku solver */
public abstract class Sudoku implements Sudoku_Game{
   private int[][] table;  // table is the matrix that will contain the sudoku values
   
   /** The constructor initializes the field variable, ie creates a new sudoku using the "create" method */
   public Sudoku(){
      table = create();
   }
   
   /** Creates a new sudoku
       @return a 9x9 two-dimensional array */
   public int[][] create(){
      return new int[9][9];
   }
   
   /** Returns as a result the matrix that contains all the values of this sudoku
       @return two dimensional matrix with the values of this sudoku */
   public int[][] getTable(){
      return table;
   }
   
   /** Returns this sudoku to the initial state, ie initializes all values to zero (Sudoku_Game method) */
   public void clear(){
      for(int i=0; i<table.length; i++){
         for(int j=0; j<table[i].length; j++){
            table[i][j]=0;
         }
      }
   }
   
   /** It deals with placing the value "x" in the position "row" "column" in this sudoku
     * It is used solving the sudoku and is abstract because to solving sudoku depends on your rules.
       @param x is the value to be set in sudoku
       @param row is the position row where the value 'x' will be set
       @param column is the position column where the value 'x' will be set
       @return true if this value can be set to this position otherwise returns false */
   public abstract boolean putNumber(int x, int row, int column);
}