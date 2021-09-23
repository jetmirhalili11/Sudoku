/** By: Jetmir Halili */

import java.util.HashSet;
import java.util.HashMap;

/** The Sudoku_Model class extends the Sudoku abstract class and deals with modeling a sudoku with our rules */
public class Sudoku_Model extends Sudoku{
   private boolean[] rules;                           // rules of this sudoku (solver only)
   private int level;                                 // game level (game only)
   private HashSet<Integer> table2;                   // Positions of numbers in the window (game only)
   private HashMap<Integer,Integer> wrongNumbers;     // Wrongly given inputs as well as their positions (game only)
   private int filledCells;                           // Number of filled cells (game only)
   private HashSet<Integer>[][] smallNumbers;         // Sudoku Small Numbers (Flags) (game only)
   
   /** The solver constructor initializes the field variable (rules) */
   public Sudoku_Model(){
      rules= new boolean[3];
      rules[0] = true;
   }
   
   /** The game constructor initializes the field variables and calls the necessary methods
       @param rules are the rules that the user chooses (normal, x, king rules)
       @param level is the level that the user chooses (easy or normal or hard) */
   public Sudoku_Model(boolean[] rules, int level){
      this.rules= rules;
      this.level= level;
      smallNumbers();
      table2= new HashSet<>();
      wrongNumbers= new HashMap<>();
      initializes(rules,level);
   }
  
   /** Deals with setting the value "x" in the position "row" "column" in this sudok based on the rules set by the user
       @param x is the value to be set in sudoku
       @param row is the position row where the value 'x' will be set
       @param column is the position column where the value 'x' will be set
       @return true if this value can be set to this position otherwise returns false */
   public boolean putNumber(int x, int row, int column){
      if(x==0 || (!(rules[0] && !checkNormalRules(x,row,column)) && !(rules[1] && !checkXRules(x,row,column)) && !(rules[2] && !checkKingRules(x,row,column)))){
         getTable()[row][column]= x;
         return true;
      }
      return false;
   }
   
   /** Deals with the establishment or removal of the new rule
       @param enable is true if we are setting a rule, while false if we are removing it
       @param row is the position row where the value 'x' will be set
       @return true if this value can be set to this position otherwise returns false */
   public boolean putRule(boolean enable, int ruleIndex){
      if(!enable)
         rules[ruleIndex]= false;
      else{
         int[][] table = getTable();
         for(int i=0; i!=9; i++)
            for(int j=0; j!=9; j++)
               if(table[i][j]!=0){
                  if(ruleIndex==0 && !checkNormalRules(table[i][j],i,j))
                     return false;
                  else if(ruleIndex==1 && !checkXRules(table[i][j],i,j))
                     return false;
                  else if(ruleIndex==2 && !checkKingRules(table[i][j],i,j))
                     return false;
               }
         rules[ruleIndex]= true;
      }
      return true;
   }
   
   /** It deals with controlling the setting of the value 'x' based on the normal rules of sudoku
       @param x is the value to be set in sudoku
       @param row is the position row where the value 'x' will be set
       @param column is the position column where the value 'x' will be set
       @return true if this value can be set to this position otherwise returns false */
   private boolean checkNormalRules(int x, int row, int column){
      int[][] a= getTable();
      for(int i=0; i!=9; i++){
         if(i!=row && a[i][column]==x){
            return false;
         }
      }
         
      for(int i=0; i!=9; i++){
         if(i!=column && a[row][i]==x){
            return false;
         }
      }
      
      int tempoR= (row/3)*3;
      int tempoC= (column/3)*3;
      for(int i=0; i!=3; i++){
         for(int j=0; j!=3; j++){
            if((tempoR+i!=row || tempoC+j !=column) && a[tempoR+i][tempoC+j]==x){
               return false;
            }
         }
      }
      
      return true;
   }
   
   /** Deals with controlling the setting of the value 'x' based on the X rule (two diagonals)
       @param x is the value to be set in sudoku
       @param row is the position row where the value 'x' will be set
       @param column is the position column where the value 'x' will be set
       @return true if this value can be set to this position otherwise returns false */
   private boolean checkXRules(int x, int r, int c){
      int[][] a= getTable();
      if(r==c){
         for(int i=0; i!=9; i++){
            if(i!=r && a[i][i]==x){
               return false;
            }
         }
      }
         
      if(r+c==8){
         for(int i=0; i!=9; i++){
            if(i!=r && a[i][8-i]==x){
               return false;
            }
         }
      }
      return true;
   }
   
   /** Deals with controlling the setting of the value 'x' according to the King rules (from the game of chess)
       @param x is the value to be set in sudoku
       @param row is the position row where the value 'x' will be set
       @param column is the position column where the value 'x' will be set
       @return true if this value can be set to this position otherwise returns false */
   protected boolean checkKingRules(int x, int r, int c){
      int[][] a= getTable();
      for(int i=-1; i<2; i++){
         for(int j=-1; j<2; j++){
            int tempoR= r+i;
            int tempoC= c+j;
            if((i!=0 || j!=0) && tempoR>=0 && tempoR<9 && tempoC>=0 && tempoC<9 && a[tempoR][tempoC]==x){
               return false;
            }
         }
      }
      return true;
   }
   
   /** This method only initializes the values that the user will see at the beginning of the game
        @param rules are the rules of the game
        @param level is the level of the game */
   public void initializes(boolean[] rules, int level){
      BackTracking.solve(this);
      int[][] table= getTable();
      filledCells= 45- 8*level;
      if(rules[2])
         filledCells-= 5; 
      
      int count= 0;
      while(count<filledCells){
         int i= (int)(Math.random()*9);
         int j= (int)(Math.random()*9);
         if(!table2.contains(i*10+j)){
            table2.add(i*10+j);
            count++;
         }
      }
   }
   
   /** Checks a user input if the given value is correct and updates the filledCells variable
        @param x is the value that is checked if it is sake
        @param row is the value position row that we will check
        @param column is the value position column that we will check
        @return true if the input is correct otherwise it returns false */
   public boolean checkInput(int x, int row, int column){
      if(getTable()[row][column] == x){
         filledCells++;
         return true;
      }
      return false;
   }
   
   /** Deals with the end of the game
       @returns true if the user has filled in all the cells, otherwise returns false */
   public boolean hasEnd(){
      return filledCells==81;
   }
   
   /** Deals with the numbers that the user sees during the game
       @return a HashSet containing number positions */
   protected HashSet<Integer> getTable2(){
      return table2;
   }

   /** Deals with numbers that the user gives incorrectly as input
       @return a HashMap that contains the positions as well as the numbers in those positions */
   protected HashMap<Integer,Integer> getWrongNumbers(){
      return wrongNumbers;
   }
   
   /** This method creates the two-dimensional array that will contain smallNumbers (Flags) */
   private void smallNumbers(){
      smallNumbers= new HashSet[9][9];
      for(int i=0; i!=9; i++){
         for(int j=0; j!=9; j++){
            smallNumbers[i][j]= new HashSet<Integer>();
         }
      }
   }
   
   /** This method is used to set or remove a number from the HashSet smallNumbers (flag)
       @param x is the value to be added or removed
       @param is the number line
       @param j is the number column */
   protected void putSmallNumber(int x, int i, int j){
      if(x!=0)
         if(smallNumbers[i][j].contains(x))
            smallNumbers[i][j].remove(x);
         else
            smallNumbers[i][j].add(x);
      else
         smallNumbers[i][j].clear();
   }
   
   /** This method deals with smallNumbers (flags)
       @return a 2 dimensional array containing HashSets with smallNumbers (flags) */
   protected HashSet<Integer>[][] getSmallNumbers(){
      return smallNumbers;
   }
}