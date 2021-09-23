/** By: Jetmir Halili */

import java.util.HashSet;

/** This class represents the BackTracking algorithm
    All methods are static */
public class BackTracking{
   
   /** This method solves a sudoku
       @param model is the model that sets the rules of sudoku to be solved
       @return true if sudoku with these values can be solved, otherwise it returns false */
   public static boolean solve(Sudoku_Model model){
      int[][] a = model.getTable();
      HashSet<Integer> input= saveInput(a);
      HashSet<Integer>[][] b = new HashSet[9][9];
      
      int r;
      for(int i=0; i<9; i++){
         for(int j=0; j<9; j++){
            if(!input.contains(i*10+j)){           // if the current position is not part of the input
               if(i<0 || j<0){                     // if this sudoku can not be solved
                  clear(a,input);
                  return false;
               }
               
               if(b[i][j]==null)
                  b[i][j]= new HashSet<Integer>();
            
               do{
                  if(b[i][j].size()==9){          // if no number can be set to this position
                     a[i][j]=0;
                     b[i][j].clear();
                     int tIJ= nextIJ(i,j, input);
                     i= tIJ/10;
                     j= tIJ%10;
                     break;
                  }
                  r = random();  
               }while(!b[i][j].add(r) || !model.putNumber(r,i,j));     // As long as the number 'r' cannot be placed in position 'i', 'j'
            }
         }
      }
      return true;
   }
   
   /** Calculate a random number
       @return a random integer in the range [1,9] */
   private static int random(){
      return ((int)(Math.random()*9))+1;
   }
   
   /** Returns as a result the next sudoku position during the algorithm when we go back
       @param i row of current position
       @param j column of current position
       @param input hashSet containing the player's input positions
       @return the sum of the column of the next position with the output of the number 10 with the row of the next position (row * 10 + column) */
   private static int nextIJ(int i, int j, HashSet<Integer> input){
      do{
         j-=2;
         if(j<-1){
            i--;
            j=7;
         }
      }while(input.contains(i*10+j));
      return i*10+j;
   }
   
   /** This method saves the positions of the inputs provided by the user so that they do not change during the algorithm
       @param a matrix that holds all the sudoku
       @return HashSet containing the input positions */
   private static HashSet<Integer> saveInput(int[][] a){
      HashSet<Integer> rez= new HashSet<>();
      for(int i=0; i!=9; i++)
         for(int j=0; j!=9; j++)
            if(a[i][j]!=0)
               rez.add(i*10+j);
               
      return rez;
   }
   
   /** This method serves to remove all numbers set during the algorithm if this sudoku can not be solved
       @param is it a two dimensional array with sudoku values
       @param input is the HashSet that contains the input positions of the user, ie the positions of the values that should not be left */
   private static void clear(int[][] a, HashSet<Integer> input){
      for(int i=0; i!=9; i++)
         for(int j=0; j!=9; j++)
            if(!input.contains(i*10+j))
               a[i][j]= 0;
   } 
}