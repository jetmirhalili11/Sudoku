/** By: Jetmir Halili */

import javax.swing.*;
import java.awt.*;

/** Kjo klase merret me dritaren e pare qe do ta shof shrytezuesi pra eshte klase View */
public class Open_Window{
   private JPanel openPanel;
   private JButton buttons[];
   private JFrame frame;
   
   /** The constructor initializes the field variables and creates a graphical window */
   public Open_Window(){                  
      frame= new JFrame("Sudoku Solver (JH)");
            
      openPanel();
            
      frame.getContentPane().add(openPanel);
      frame.pack();
      frame.setMinimumSize(new Dimension(frame.getWidth(),frame.getHeight()));
      frame.setLocationRelativeTo(null);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);      
            
      frame.setVisible(true);
   }
   
   /** This method sets the Layout for the entire window and adds panels and buttons to it */
   private void openPanel(){
      openPanel= new JPanel();
      openPanel.setPreferredSize(new Dimension(400,400));
      openPanel.setLayout(new BorderLayout());
      
      openPanel.add(labelsPanel(), BorderLayout.CENTER);
      openPanel.add(buttonsPanel(), BorderLayout.SOUTH);
   }
   
   /** This method creates a JPanel and puts the logo as well as the name of the game in it
       @return a Panel with a certain layout and containing the logo and the name of the game as a label */
   private JPanel labelsPanel(){      
      JPanel labelsPanel= new JPanel(new BorderLayout());
      labelsPanel.setBackground(Color.WHITE);
      
      JLabel logo= new JLabel(new ImageIcon(getClass().getResource("Images//logo.png")));
      labelsPanel.add(logo, BorderLayout.CENTER);
      
      JLabel name= new JLabel("SUDOKU",SwingConstants.CENTER);
      name.setForeground(new Color(34,54,87));
      name.setFont(new Font("Arial Rounded MT Bold",0,50));
      labelsPanel.add(name, BorderLayout.SOUTH);
      
      return labelsPanel;
   }

   /** Kjo metod krjon nje JPanel dhe vendos dy buttona ne te si dhe rregullon pamjen e tyre
       @return nje Panel me layout te caktuar si dhe qe permban dy buttonat "Game" dhe "Solver" ne te */
   private JPanel buttonsPanel(){
      JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,30,10));
      buttonsPanel.setBackground(Color.WHITE);
            
      buttons= new JButton[2];
      for(int i=0; i!=buttons.length; i++){
         buttons[i]= new JButton();
         buttons[i].setBackground(new Color(149,173,216));
         buttons[i].setForeground(new Color(34,54,87));
         buttons[i].setFont(new Font("Arial Rounded MT Bold",0,15));
         buttons[i].setFocusable(false);
         buttons[i].setPreferredSize(new Dimension(100,30));
         buttonsPanel.add(buttons[i]);
      }
      
      buttonsPanel.add(Game_Settings.defaultPanel(5000,60));   // Place an empty panel so that the buttons are not at the bottom of the window
   
      buttons[0].setText("Game");
      buttons[1].setText("Solver");
      
      return buttonsPanel;
   }
   
   /** This method returns the graphical window buttons
       @return an array with "Game" and "Solver" buttons */
   protected JButton[] getButtons(){
      return buttons;
   }

   /** This method returns JFrame so that the Panels can be changed
       @return this JFrame (graphic window) */
   protected JFrame getFrame(){
      return frame;
   }
   
   /** This method returns this Panel that contains the logo and buttons
       @return The JPanel of this graphic window */
   protected JPanel getPanel(){
      return openPanel;
   }
}