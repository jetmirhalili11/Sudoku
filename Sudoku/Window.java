/** By: Jetmir Halili */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;

/** The Window class deals with the game or solver GUI so it is the View class */
public class Window extends JPanel{
   private int a[][];                                 // matrix with sudoku values
   private JButton button[];       
   private JPanel f;              
   private JFrame frame;       
   private JCheckBox box[];                           // buttons for setting and removing rules (solver only)
   private int selected= -1;                          // saves the selection position from the user
   private boolean help=false, isSolver, smallMode;   // help indicates if we are in helpMode (solver only), isSolver tells if it is a game or solver
   private Image h;                                   // photo help (solver only)
   private HashSet<Integer>[][] smallNumbers;         // Sudoku Small Numbers (Flags) (game only)
   private HashSet<Integer> numbers;                  // Positions of numbers in the window (game only)
   private HashMap<Integer, Integer> wrongNumbers;    // Wrongly given inputs as well as their positions (game only)
   private Timer timer;                               // game completion time (game only)
   private JLabel l, time;                            // l is the label that contains the message or information for the user, "time" contains the time since the game started (only in the game)
   private double scaleX, scaleY;                     // save the window magnification on the X and Y axis
   private double width, height;                      // panel sizes 'f' when changing it
   private static final int fWidth=610, fHeight=430;  // static values of window width and height, any window calculation is done these values should be used as a proportional ratio
   
   /** The game constructor initializes all field variables and displays the graphical window
       @param table is a two dimensional matrix that contains all sudoku numbers
       @param smallNumbers contains Flags for each position
       @param numbers contains the positions of the numbers that are in the sudoku window
       @param wrongNumbers contains erroneously given iputs as well as their positions */
   public Window(int table[][], HashSet<Integer>[][] smallNumbers, HashSet<Integer> numbers, HashMap<Integer, Integer> wrongNumbers){     
      this(table,false);
            
      smallMode= false;
      this.smallNumbers= smallNumbers;
      this.numbers= numbers;
      this.wrongNumbers= wrongNumbers;
      timer();
      resize();
   }
   
   /** The solver constructor initializes all field variables and displays the graphical window
       @param table is a two dimensional matrix where the values of the sudoku that will be solved will be placed */
   public Window(int[][] table){
      this(table, true);
            
      h=getImage("Images//help.png");
      checkBoxDeal();      
      resize();
   }
   
   /** This constructor is private because it is called only by the above two constructors
       initializes variables and calls methods that are common to both the game and the solver
       @param table 2 dimensional matrix with sudoku values
       @param isSolver true if the window is called by the solver constructor while false by the game constructor */
   private Window(int[][] table, boolean isSolver){
      try{                     // set "metal" as UI
         UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
      }
      catch(UnsupportedLookAndFeelException e){} 
      
      scaleX= scaleY= 1;
      this.isSolver= isSolver;
      
      // This part takes the dimensions of the whole destop and determines the size of the window based on it
      Dimension d= Toolkit.getDefaultToolkit().getScreenSize();
      width = d.getWidth()/2;
      height= width*fHeight/fWidth;
      
      a= table;
      frame = new JFrame("Sudoku Solver (JH)");
      frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);      
   
      f= new JPanel();
      f.setLayout(null);
      f.setPreferredSize(new Dimension((int)width,(int)height));
      buttonDeal();
      labelDeal();
      f.add(this);
      f.setBackground(new Color(255,255,255));
      
      frame.add(f);
      frame.pack();
      frame.setLocationRelativeTo(null);
      setResize();
            
      frame.setVisible(true);
   }
   
   /** This method takes the window size during the change and calls the resize() method with the new values */
   private void setResize(){
      frame.setMinimumSize(new Dimension(frame.getWidth(),frame.getHeight()));
      f.addComponentListener(
         new ComponentAdapter(){
            public void componentResized(ComponentEvent e){
               Component c = (Component) e.getSource();
               width= c.getWidth();
               height= c.getHeight();        
               resize();
            }
         }
         );
   }
   
   /** This method deals with setting and restoring the size of all objects to be exploited by the user
       such as buttons, boxboxes, texts, etc. */
   private void resize(){
      double size= width*fHeight/fWidth <= height ? width : height;
      int size2= width*fHeight/fWidth <= height ? fWidth : fHeight;
      
      Font font1 = new Font("Arial Rounded MT Bold",0,(int)(20*size/size2));
      Font font2 = new Font("Arial Rounded MT Bold",0,(int)(35*size/size2));
      
      int w=(int)(width/10);
      int h=(int)(height/9.5);
      
      for(int i=0; i!=4; i++){     // button dimensions
         for(int j=0; j!=3; j++){
            button[i*3+j].setBounds((int)(400*width/fWidth+j*w+5*width/fWidth*j),(int)(105*height/fHeight-h*(i-1)-5*height/fHeight*i),w,h);
         }
      }
      button[12].setBounds((int)(400*width/fWidth),(int)(337*height/fHeight),(int)(3*w+10*width/fWidth),h);
      for(int i=1; i!=button.length; i++){  // setting the new font to buttons
         button[i].setFont(font2);
      }
      button[0].setFont(font1);
      button[2].setFont(font1);
   
      l.setBounds((int)(100*width/fWidth),(int)(390*height/fHeight),(int)(fHeight*width/fWidth),(int)(35*height/fHeight));
      l.setFont(font1);
         
      if(isSolver){
         for(int i=0; i!=box.length; i++){  // setting the size and font of checkBoxes
            box[i].setBounds((int)(400*width/fWidth),(int)(220*height/fHeight+30*i*height/fHeight),(int)(200*width/fWidth),(int)(30*height/fHeight));
            box[i].setFont(font1);
         }
      }
      else{          // setting the size of the text containing the time
         time.setBounds((int)(400*width/fWidth),(int)(220*height/fHeight+30*1*height/fHeight),(int)(200*width/fWidth),(int)(30*height/fHeight));
         time.setFont(font2);
      }
      
      // calculating the panel magnification with sudoku values as well as its size
      scaleX= width/fWidth;
      scaleY= height/fHeight;
      this.setBounds(0,0,(int)(scaleX*384),(int)(scaleY*384));
   }

   /** This method initializes the value of the "selected" variable based on the input parameters
       @param x is the x coordinate of the position that the user has pressed with the mouse
       @param y is the x coordinate of the position that the user has pressed with the mouse */
   protected void setSelected(int x, int y){
      for(int i=0; i!=9; i++){
         for(int j=0; j!=9; j++){              // with both for loops we ask if the point (x, y) is inside any of the sudoku positions
            if(x>(j*42+3+j/3)*scaleX && x<(j*42+3+j/3+40)*scaleX && y>(i*42+3+i/3)*scaleY && y<(i*42+3+i/3+40)*scaleY){
               selected= i*10+j;
               setMessage("Click the number to put in this position");
               repaint();
            }
         }
      }
   }
   
   /** This method directly sets the value of the variable "selected" (row * 10 + column)
       @param i is the row of the position we want to select
       @param j is the position column we want to select */
   protected void setDirectSelected(int i, int j){
      selected = i*10+j;
   }
   
   /** This method returns the selected variable as a result
       @return variable containing the selected position (row * 10 + column) */
   protected int getSelected(){
      return selected;
   }
      
   /** This method deals with the appearance of buttons, such as colors, borders, text, etc. */
   private void buttonDeal(){
      int w=(int)(width/10);
      int h=(int)(height/9.5);
      
      button= new JButton[13];
      for(int i=0; i!=4; i++){
         for(int j=0; j!=3; j++){
            button[i*3+j] = new JButton((i-1)*3+j+1+"");
         }
      }
      button[1].setText("0");
      button[0].setText("Clear");
      button[2].setText("Start");
      button[12]= new JButton("Help");
      if(!isSolver)
         button[12].setText("N");
         
      for(int i=0; i!=button.length; i++){
         button[i].setFocusable(false);
         button[i].setBackground(new Color(0,81,232));
         button[i].setBorder(null);
         button[i].setForeground(new Color(255,255,255));
         f.add(button[i]);
      }
      
      // If it is a game then remove "Clear" and "Start" buttons
      if(!isSolver){
         f.remove(button[0]);
         f.remove(button[2]);
      }
   }
   
   /** This method deals with the appearance of the message label, such as colors, borders, text, etc. */
   public void labelDeal(){
      l= new JLabel("Sudoku Solver (JH)");
      l.setHorizontalAlignment(SwingConstants.CENTER);
      l.setForeground(Color.BLACK);
      l.setOpaque(true);
      l.setBackground(Color.RED);
      f.add(l);
   }  
   
   /** This method deals with the appearance of checkBoxes (that contain rules), such as colors, borders, text, etc. */
   private void checkBoxDeal(){
      box= new JCheckBox[3];
      for(int i=0; i!=box.length; i++){
         box[i]= new JCheckBox();
         box[i].setEnabled(true);
         box[i].setFocusable(false);
         box[i].setBackground(new Color(255,255,255));
         f.add(box[i]);
      }
      box[0].setSelected(true);
      box[0].setText("normal rules");
      box[1].setText("X rules");
      box[2].setText("king moves");
   }
   
   /** This method deals with the panel that will display the sudoku, ie its painting
       It is a method overwritten by the JPanel class
       @param g1 is the graphic pen that will be used to paint */
   public void paintComponent(Graphics g1){
      Graphics2D g= (Graphics2D)g1;
      
      g.scale(scaleX,scaleY);    // we scale (zoom) everything we will draw
      
      g.setColor(Color.BLACK);
      g.fillRect(0,0,384,384);
      if(!help){    
         g.setFont(new Font("Arial Rounded MT Bold",Font.PLAIN,25));
         for(int i=0; i!=9; i++){                        // For each square of sudoku
            for(int j=0; j!=9; j++){
               if(!isSolver && wrongNumbers.containsKey(i*10+j)){       // If in this position the user has given incorrect input
                  g.setColor(Color.RED);
               }
               else if(i*10+j==selected){                 // If this position is selected by the user then we set another color
                  g.setColor(new Color(2,184,253));
               }
               else{                                     // Otherwise we set the color white or blue depending on the position
                  if(((i/3+1)+(j/3+1))%2==1){
                     g.setColor(Color.WHITE);
                  }
                  else{
                     g.setColor(new Color(200,255,250));
                  }
               }
               g.fillRect(j*42+3+j/3,i*42+3+i/3,40,40);       // Paint a square in position i, j
               g.setColor(Color.BLACK);
               if((isSolver && a[i][j]!=0) || (!isSolver && numbers.contains(i*10+j))){      // We put the number (if any) in position i, j
                  g.drawString(""+a[i][j],j*42+3+13+j/3,i*42+3+i/3+28);
               }
               else if(!isSolver && wrongNumbers.containsKey(i*10+j)){                       // We enter the wrong number given by the user
                  g.drawString(""+wrongNumbers.get(i*10+j),j*42+3+13+j/3,i*42+3+i/3+28);
               }
               else if(!isSolver && smallNumbers[i][j].size()!=0){                           // We put small numbers (flags) 
                  paintInSquare(g,i,j,smallNumbers[i][j]);
               }
            }
         }
      }
      else{                             // If we are in helpMode then we paint the help picture
         g.drawImage(h,3,3,this);
      }
   }
   
   /** This method deals with drawing numbers within a position, small numbers
       @param g graphic pen that I will paint
       @param x is the position on the x axis where the numbers will be painted
       @param y is the position on the y axis where the numbers will be painted
       @param num are the numbers that will be painted */
   private void paintInSquare(Graphics g, int x, int y, HashSet<Integer> num){
      x= x*42+3+x/3+10;
      y= y*42+3+y/3+2;
   
      Font f= g.getFont();
      g.setFont(new Font("Arial Rounded MT Bold",0,10));
      Iterator iter= num.iterator();
      for(int i=0; i<3; i++){
         for(int j=0; j<3; j++){
            if(!iter.hasNext()){
               g.setFont(f);
               return;
            }
            g.drawString(iter.next()+"", y+j*14, x+i*14);
         }
      }
      g.setFont(f);
   }
   
   /** Activates all buttons if they are deactivated otherwise deactivates them */
   protected void setActiveButtons(){
      boolean t= !button[0].isEnabled();
      for(int i=0; i<button.length; i++){
         button[i].setEnabled(t);
      }
      for(int i=0; i<box.length; i++){
         box[i].setEnabled(t);
      }
      
      if(help)
         button[button.length-1].setEnabled(true);
   }
   
   /** Returns all buttons in this window as a result
       @return an array that contains all the window buttons */
   protected JButton[] getButtons(){
      return button;
   }
   
   /** Returns all checkboxes in this window as a result
       @return an array that contains all the window checkboxes */
   protected JCheckBox[] getCheckBoxs(){
      return box;
   }
   
   /** Returns the graphical window (JFrame) as a result
        @return the application window (JFrame) */
   protected JFrame getFrame(){
      return frame;
   }
   
   /** Creates an Image based on the Input String
       @param s String containing the path of the photo
       @return an Image object (photo) based on path 's' */
   protected Image getImage(String s){
      ImageIcon i= new ImageIcon(getClass().getResource(s));
      return i.getImage();
   }
   
   /** Put String 's' in the message bar
       @param s String to be placed in the label */
   protected void setMessage(String s){
      l.setText(s);
   }
   
   /** Changes the current boolean help value and returns it as a result
       @return help that indicates if the user has logged in or left the help window */
   protected boolean getHelpMode(){
      help= !help;
      return help;
   }   
   
   /** This method creates a label that will contain the time and puts an action loop in it
       As well as set the action to stop this loop if the user closes this window */
   protected void timer(){
      time= new JLabel("00 : 00 : 00");
      time.setForeground(Color.BLACK);
      time.setHorizontalAlignment(SwingConstants.CENTER);
      f.add(time);
      
      timer= new javax.swing.Timer(1000,
         e->{
            addTime(0,1);             // For every second we add 1 second
         });
      timer.start();
      
      // Action if the window is closed by the user 
      frame.addWindowListener(
         new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
               timer.stop();             // We stop the infinite cycle for setting the time
               frame.dispose();          // Close this window
            }
         });
   }
   
   /** This method serves to add minutes and seconds in time
       @param m are the minutes that will be added to the time
       @param s are the seconds that will be added to the time */
   protected void addTime(int m, int s){
      String[] t= time.getText().split(" : ");
      int seconds= Integer.parseInt(t[2]);
      int minutes= Integer.parseInt(t[1]);
      int hours= Integer.parseInt(t[0]);
      seconds+= s;
      minutes+= m;
      if(seconds==60){
         seconds=0;
         minutes++;
      }
      if(minutes==60){
         minutes=0;
         hours++;
      }
         
      String secondsS= seconds<10 ? ("0"+seconds) : (""+seconds); 
      String minutesS= minutes<10 ? ("0"+minutes) : ""+minutes; 
      String hoursS= hours<10 ? ("0"+hours) : ""+hours; 
         
      time.setText(hoursS+" : "+minutesS+" : "+secondsS);
   }
   
   /** This method stops the infinite cycle of time and returns time as a result
       @return an array of 3 strings containing past hours, minutes and seconds */
   protected String[] getTime(){
      timer.stop();
      return time.getText().split(" : ");
   }
   
   /** This method is used to switch from the sudoku with normal numbers to the one with small numbers (Flags) and vice versa */
   protected void setSmallMode(){
      smallMode= !smallMode;
   }
   
   /** This method is obtained if we are in smallMode
       @return a boolean that indicates whether we are in smallMode (setting Flags) or off */
   protected boolean getSmallMode(){
      return smallMode;
   }
}