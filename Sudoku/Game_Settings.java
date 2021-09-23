/** By: Jetmir Halili */

import javax.swing.*;
import java.awt.*;

/** This class deals with the panel that will contain the settings of this game so it is the View class
    used only when the user wants to play sudoku (not solve) */
public class Game_Settings extends JPanel{
   private JButton buttons[];
   private Color textColor;
   private JRadioButton[] level;                     // contain 3 levels (easy, medium and hard)
   private JCheckBox[] rules;                        // contain rules (normal rules, X and king)
   
   /** The constructor initializes the field variables and adds buttons to the panel */
   public Game_Settings(){         
      textColor= new Color(34,54,87);
                     
      setPreferredSize(new Dimension(400,400));
      setLayout(new BorderLayout(0,20));
      setBackground(Color.WHITE);
      
      add(titlePanel(), BorderLayout.NORTH);
      add(defaultPanel(50,200), BorderLayout.WEST);
      add(defaultPanel(50,200), BorderLayout.EAST);
      add(optionsPanel(), BorderLayout.CENTER);
      add(buttonsPanel(), BorderLayout.SOUTH);
      
   }
   
   /** This method deals with the panel that contains the title ("Options") of the whole panel
       @return a JPanel that contains 2 labels that represent the title of the whole panel */
   private JPanel titlePanel(){
      JPanel title= new JPanel(new BorderLayout());
      title.setBackground(Color.WHITE);
      title.add(label("OPTIONS",35, SwingConstants.CENTER),BorderLayout.CENTER);
      title.add(label("~~~~~~~~~",35, SwingConstants.CENTER),BorderLayout.SOUTH);
      return title;
   }

   /** This method creates a blank panel and is used in cases where we want to place space between other panels
        @param width is the width of the panel
        @param height is the length of the panel
        @return an empty JPanel with a white background */
   protected static JPanel defaultPanel(int width, int height){
      JPanel rez= new JPanel();
      rez.setPreferredSize(new Dimension(width,height));
      rez.setBackground(Color.WHITE);
      return rez;
   }
   
   /** This method creates a label with the text, size and position specified by the input parameters
       @param text is the text that will be placed on the label
       @param size is the size of the text
       @param position is the position of the text on the label (RIGHT, CENTER, LEFT)
       @return a JPanel that contains 2 labels that represent the title of the whole panel */   
   private JLabel label(String text, int size, int position){
      JLabel title= new JLabel(text, position);
      title.setFont(new Font("Arial Rounded MT Bold",0,size));
      title.setForeground(textColor);
      return title;
   }
   
   /** This method creates a panel that contains the game options
       @return a JPanel that contains all the game options and rules and levels */
   private JPanel optionsPanel(){      
      JPanel optionsPanel= new JPanel(new GridLayout(1,2,50,0));
      optionsPanel.setBackground(Color.WHITE);
      optionsPanel.add(rulesPanel());
      optionsPanel.add(levelPanel());
      return optionsPanel;
   }   

   /** This method creates a panel that contains 3 check buttons and adjusts their appearance
       @return a JPanel that contains check buttons with normal rules, X and king rules */
   private JPanel rulesPanel(){
      JPanel rulesPanel= new JPanel(new GridLayout(4,1,0,5));
      rulesPanel.setBackground(Color.WHITE);
      rulesPanel.add(label("Rules:",25, SwingConstants.LEFT)); 
      
      rules= new JCheckBox[3];
      for(int i=0; i<rules.length; i++){
         rules[i]= new JCheckBox();
         rules[i].setForeground(textColor);
         rules[i].setFont(new Font("Arial Rounded MT Bold",0,20));
         rulesPanel.add(rules[i]);
      }
      
      rules[0].setSelected(true);
      rules[0].addActionListener(e-> rules[0].setSelected(true));      // Normal rules can not be removed
      
      rules[0].setText("Normal");
      rules[1].setText("X rule");
      rules[2].setText("King");
      
      return rulesPanel;
   }

   /** This method creates a panel that contains 3 box buttons and adjusts their appearance
       @return a JPanel that contains radio buttons with easy, medium and hard levels */
   private JPanel levelPanel(){
      JPanel levelPanel= new JPanel(new GridLayout(4,1,0,5));
      levelPanel.setBackground(Color.WHITE);
      levelPanel.add(label("Level:",25, SwingConstants.LEFT)); 
      
      ButtonGroup group = new ButtonGroup();
      level= new JRadioButton[3];
      for(int i=0; i<level.length; i++){
         level[i]= new JRadioButton();
         level[i].setForeground(textColor);
         level[i].setFont(new Font("Arial Rounded MT Bold",0,20));
         group.add(level[i]);
         levelPanel.add(level[i]);
      }
      level[0].setSelected(true);
      
      level[0].setText("Easy");
      level[1].setText("Medium");
      level[2].setText("Hard");
      
      return levelPanel;
   }
   
   /** This method creates a panel that contains the 3 window buttons and adjusts their appearance
       @return a JPanel containing the "Help", "Play" and "Back" buttons */
   private JPanel buttonsPanel(){
      JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,15,10));
      buttonsPanel.setBackground(Color.WHITE);
            
      buttons= new JButton[3];
      for(int i=0; i!=buttons.length; i++){
         buttons[i]= new JButton();
         buttons[i].setBackground(new Color(149,173,216));
         buttons[i].setForeground(new Color(34,54,87));
         buttons[i].setFont(new Font("Arial Rounded MT Bold",0,15));
         buttons[i].setFocusable(false);
         buttons[i].setPreferredSize(new Dimension(90,30));
         buttonsPanel.add(buttons[i]);
      }
      
      buttonsPanel.add(defaultPanel(5000,60));          // Place an empty panel so that the buttons are not at the bottom of the window
   
      buttons[0].setText("Help");
      buttons[1].setText("Play");
      buttons[2].setText("Back");
      
      return buttonsPanel;
   } 
   
   /** Deals with the rules of the game
       @return a boolean array containing 3 rules of the game (true if that rule is selected) */
   protected boolean[] getRules(){
      return new boolean[]{ true, rules[1].isSelected(), rules[2].isSelected() };
   }
   
   /** Deals with game levels
       @return a number that represents the selected level. Number 0 for easy, 1 for medium and 2 for hard */
   protected int getLevel(){
      for(int i=0; i<level.length; i++){
         if(level[i].isSelected()){
            return i;
         }
      }
      return -1;
   }
   
   /** Deals with 3 window buttons
       @return an array with 3 buttons "Help", "Play", "Back"*/
   protected JButton[] getButtons(){
      return buttons;
   }
}