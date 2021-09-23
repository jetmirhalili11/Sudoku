/** By: Jetmir Halili */

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/** This class is the main class as well as the app controller */
public class Start{
   private Open_Window open;                   // the first graphical window the user sees
   private Game_Settings settings;             // panel containing settings if the driver selects the game
   
   /** The constructor initializes the field variables
       Calls the methods that place the actions on the buttons of the first two windows */
   public Start(){
      open= new Open_Window();
      settings= new Game_Settings();
      
      addOpenButtons();
      addSettingsButtons();
   }
   
   /** Adds the action of the first window buttons (buttons to select Game or Solver) */
   private void addOpenButtons(){
      JButton[] b= open.getButtons();
      JFrame frame= open.getFrame();
      
      // "Game" Button Action
      b[0].addActionListener(                          
         e->{
            frame.getContentPane().removeAll();        // remove existing panel (first one)
            frame.getContentPane().add(settings);      // we add the panel that contains the game settings
         
            frame.repaint();
            frame.validate();
         });
         
      // "Solver" button action
      b[1].addActionListener(
         e->{
            new Game_Controll();    // Creates a Solver
         });
   }
   
   /** Adds the action of the buttons of the settings window */
   private void addSettingsButtons(){
      JButton[] b= settings.getButtons();
      JFrame frame= open.getFrame();
      
      // "Back" button action
      b[2].addActionListener(
         e->{
            frame.getContentPane().removeAll();                   // remove the existing panel (settings panel)
            frame.getContentPane().add(open.getPanel());          // we add the first panel
            
            frame.repaint();
            frame.validate();
         });
      
      // "Play" button action
      b[1].addActionListener(
         e->{
            new Game_Controll(settings.getRules(), settings.getLevel());    // Creates a new sudoku game with user-defined settings
            b[2].doClick();                     // Press the "Back" button to return to the main window
         });
      
      // "Help" button action
      b[0].addActionListener(
         e->{                                     // Displays a window with the following string
           JOptionPane.showMessageDialog(null,"                                                 ----- Help -----\n"+
                                         "X rules means that two larges diagonals mus have numbers from 1 to 9\n"+
                                         "King rules means that every position around position 'p' must be different from 'p'\n"+
                                         "For every wrong input 5 minutes are added to finishing time");
         });
   }
   
   // The main (starting) method of the application
   public static void main(String [] args){
      try{                     // set "nimbus" as UI 
         javax.swing.UIManager.setLookAndFeel(new javax.swing.plaf.nimbus.NimbusLookAndFeel());
      }
      catch(javax.swing.UnsupportedLookAndFeelException e){}  
      new Start();
   }
}