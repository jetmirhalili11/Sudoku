/** By: Jetmir Halili */

import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.event.*;
import java.util.Calendar;

/** The Game_Controll class is the game or solver control class */
public class Game_Controll{
   private Window view;                   // view part of the game or solver
   private Sudoku_Model model;            // the modeling part of the game or solver
   private boolean isSolver;              // this boolean defines whether it is game or solver
   private Thread solvingThread;          // this Thread is activated when solving a sudoku
   
   /** The solver constructor initializes the field variables and calls the action methods of buttons, mouse, checkboxes and keyboard */
   public Game_Controll(){
      isSolver= true;
      
      model = new Sudoku_Model();      
      view = new Window(model.getTable());
      
      addCheckBoxActions();
      addButtonsAction();
      addMouseAction();
      addKeyboardAction();
      addCloseWindow();
   }
      
   /** The game constructor initializes field variables and calls the button, mouse, and keyboard action methods
       @param rules are the rules that the user sets in the settings window
       @param level is the level that the user chooses (easy, medium, hard) */
   public Game_Controll(boolean[] rules, int level){            
      isSolver= false;
      
      model = new Sudoku_Model(rules, level);      
      view = new Window(model.getTable(), model.getSmallNumbers(), model.getTable2(), model.getWrongNumbers());
      
      addButtonsAction();
      addMouseAction();
      addKeyboardAction();
   }

   /** This method adds action to close a graphical window
       As well as stops the solution of sudoku (if it was being solved) */  
   public void addCloseWindow(){
      view.getFrame().addWindowListener(
         new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
               solvingThread.stop();               // We stop solving sudoku (if it was being solved)
               view.getFrame().dispose();          // Close window
            }
         });
      }
   
   /** This method sets the button action (containing a number from 0 to 9)
       @param b is the Button to which the action will be placed
       @param is the number that button contains */
   private void numberAction(JButton b, int i){
      b.addActionListener(
            e->{
               int s= view.getSelected();
               if(s==-1)                    // If the user has not selected any sudoku position
                  view.setMessage("First select position then any number");
               else if(isSolver && model.putNumber(i, s/10, s%10)){   // If it is solver and not against the rules
                  view.setMessage("");
                  view.repaint();
               }
               else if(isSolver)                                      // If it is a solver but it is against the rules
                  view.setMessage("This number can't be placed here");
               else if(view.getSmallMode()){                          // If it is a game and we are in smallNumbers mode (flags mode)
                  model.putSmallNumber(i,s/10,s%10);
                  view.setMessage("");
                  view.repaint();
               }
               else if(model.getTable2().contains(s)){                // If it is a game and this position has a proper number placed in it
                  view.setMessage("You can't change that number");
               }
               else if(i==0){                                         // If it is a game and we are setting the number 0 
                  if(model.getWrongNumbers().containsKey(s))          // If this position holds an incorrect input
                     model.getWrongNumbers().remove(s);
                  view.setMessage("");
                  view.repaint();
               }
               else if(model.checkInput(i,s/10,s%10)){                // If it is a game and we are putting the right number in this position
                  if(model.getWrongNumbers().containsKey(s))          // If this position holds an incorrect input
                     model.getWrongNumbers().remove(s);
                  model.getTable2().add(s);
                  if(model.hasEnd()){                                 // If it is a game and all sudoku is completed
                     String[] time= view.getTime();
                     view.setMessage("Finished for "+Integer.parseInt(time[0])+" hours, "+Integer.parseInt(time[1])+" minutes, "+Integer.parseInt(time[2])+" seconds");
                  }
                  else
                     view.setMessage("Correct number");
                  view.repaint();
               }
               else {                                                 // If it is a game and we are putting the wrong number in this position
                  model.getWrongNumbers().put(s,i);
                  view.addTime(5,0);                   // Add 5 minutes as a penalty for the user
                  view.setMessage("Wrong number");
                  view.repaint();
               }
            });
   }
   
   /** This method sets the shares of all Window class buttons */
   private void addButtonsAction(){
      JButton[] b= view.getButtons();
      for(int i=3; i<b.length-1; i++){
         numberAction(b[i],i-2);               // button action from 0 to 9
      }
      numberAction(b[1],0);
      
      // "Clear" button action
      b[0].addActionListener(                  
         e->{
            model.clear();
            view.setDirectSelected(0,-1);
            view.setMessage("Everything is cleared");
            view.repaint();
         });
      
      // "Start" button action
      b[2].addActionListener(                
         e->{
            view.setMessage("Please wait");
            startThread();
            solvingThread.start();
         });
      
      // "Help" or "N" button action (if it is solver or game)
      b[12].addActionListener(               
         e->{
            if(isSolver){                      // If it is solver then we have the action for the "Help" button
               if(view.getHelpMode())
                  view.setMessage("You entered Help mode");
               else
                  view.setMessage("You exited Help mode");
               view.setActiveButtons();
            }
            else{                             // If it is a game then we have the action for the "N" button (smallNumbers, flags)
               if(b[12].getText().equals("N")){
                  b[12].setText("n");
                  view.setSmallMode();
               }
               else{
                  b[12].setText("N");
                  view.setSmallMode();
               }
            }            
            view.repaint();
         });
   }
   
   /** This method creates a new Thread that is used to solve sudoku
       It is created so that the application runs separately and the solution is done separately */
   private void startThread() {
         solvingThread= new Thread() {
            public void run() {
               view.setActiveButtons();
               Calendar c1= Calendar.getInstance();                 // We save the time before the start to solve sudoku
               boolean isSolved= BackTracking.solve(model);
               Calendar c2= Calendar.getInstance();                 // We save the time after we end solving sudoku
               view.setDirectSelected(0,-1);
               if(isSolved)                                         // If the sudoku is solved we indicate how much time was needed that the sudoku was solved
                  view.setMessage("Sudoku is solved for "+(c2.getTimeInMillis()-c1.getTimeInMillis())+" miliseconds");
               else
                  view.setMessage("This Sudoku can't be solved");
               view.repaint(); 
               view.setActiveButtons();
            }
         };
   }
   
   /** This method adds keyboard action so the buttons can also be used via the keyboard
       For buttons containing numbers their numbers must be printed
       The letter 'C' is pressed for the Clear button
       The letter 'S' is pressed for the Start button
       The letter 'H' is pressed for the Help button
       As well as to move from one cell to another we can use arrows */
   public void addKeyboardAction(){
      JButton[] b= view.getButtons();
      view.getFrame().addKeyListener(
         new KeyAdapter(){  
            public void keyPressed(KeyEvent e){
               int i=e.getKeyCode(); 
               if(i>=49 && i<=57){                         // if we press the numbers from 1 to 9
                  b[i-46].doClick();
               }
               else if(i==48 || i==8){                     // if we press the number 0 or the backSpace key
                  b[1].doClick();
               }
               else if(i==KeyEvent.VK_C && isSolver){      // if we press the key that contains the letter 'C'
                  b[0].doClick();
               }
               else if(i== KeyEvent.VK_S && isSolver){     // if we press the key that contains the letter 'S'
                  b[2].doClick();
               }
               else if(i== KeyEvent.VK_H && isSolver){     // if we press the key that contains the letter 'H'
                  b[12].doClick();
               }
               else if(i== KeyEvent.VK_F && !isSolver){    // if we press the key that contains the letter 'F'
                  b[12].doClick();
               }
               else if(i== KeyEvent.VK_UP){                // if we press the UP key
                  moveInSudoku(-10);   
               }
               else if(i== KeyEvent.VK_DOWN){              // if we press the DOWN key
                  moveInSudoku(10);   
               }
               else if(i== KeyEvent.VK_LEFT){              // if we press the LEFT key
                  moveInSudoku(-1);   
               }
               else if(i== KeyEvent.VK_RIGHT){             // if we press the RIGHT key
                  moveInSudoku(1);
               }
            }
         }
         );
   }
   
   /** This method is used when moving through sudoku by means of arrows
       @param x indicates where to move. If it is 10 move down, if it is -10 up, -1 to the left and 1 to the right */
   private void moveInSudoku(int x){
      int sel= view.getSelected()+x;
      if(sel%10==9){
         sel+=x;
      }
      if(sel>=0 && sel<89){
         int i= sel/10;
         int j= sel%10;
         view.setDirectSelected(i,j);   
         view.repaint();
      }
   }
   
   /** This method places the mouse action on the panel that displays Sudoku
       Selects the cell in which the user clicks */
   private void addMouseAction(){
      view.addMouseListener(
         new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
               int x=e.getX();
               int y=e.getY();
               selectInSudoku(x,y);
            }
         }
         );
   }
   
   /** This method serves to select a position in sudoku
       @param x is the row of the position we want to select
       @param y is the position column we want to select */
   private void selectInSudoku(int x, int y){
      view.setSelected(x,y);
   }
   
   /** This method puts the action in a checkBox (rules of the game)
       @param box is the checkBox that will be assigned the action
       @param is the index that shows which rule we are setting */
   private void checkBoxAction(JCheckBox box, int i){
      box.addActionListener(
         e->{
            boolean t= model.putRule(box.isSelected(), i);
            if(!box.isSelected())
               view.setMessage("This rule is removed succesfuly");
            else if(t)
               view.setMessage("This rule is added succesfuly");
            else{
               box.setSelected(false);
               view.setMessage("This rule can't be added");
            }
         });
   }
   
   /** This method adds actions to all checkBoxes (adds game rules) */
   private void addCheckBoxActions(){
      JCheckBox[] box= view.getCheckBoxs();
      for(int i=0; i!=3; i++){
         checkBoxAction(box[i],i);
      }
   }
}