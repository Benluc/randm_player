import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;

/**
 * Diese Klasse dient zum Ausgeben von Fehlermeldungen.
 * Sie ersetzt die JOptionPane Ausgabe und verwendet zudem
 * das selbe Design wie der Player
 * @author Michael Weidner, Friederike Danger <br><br> Kommentierung: Michael Weidner
 * 
 * 
 * @version 1.0
 */
	public class Error implements ActionListener {
        
		/** Bestätigunsbutton */
		private JButton ok;				
		/** Text der eigentlichen Fehlermeldung */
		private JLabel fehlermeldung;	//
		/** Überschrift der Fehlermeldung */
		private JLabel FEHLER;		
		/** Frame des Fehlermeldungsfensters */
		JFrame errorFrame;			
        
		
		
		/**
		 * Konstruktor
		 * 
		 * Methode bekommt Text der Fehlermeldung übergeben 
		 * und übergibt sie doLayout()
		 * @param string Text der Fehlermeldung
		 */
        
        	public Error(String string) {
        		doLayout(string);			
            }
  
        	
    		/**
    		 * Erstellt das Fenster für die Fehlermeldung. 
    		 * Bekommt Text der Meldung übergeben und setzt
    		 * den Frame zusammen
    		 * @param zeile Text der Fehlermeldung
    		 */
        	
        	private void doLayout(String zeile) {
           	
                errorFrame = new JFrame(); 					//neues Frame
                
              //Fehlermeldung
                fehlermeldung = new JLabel();				
                	fehlermeldung.setSize(200,50);
                	fehlermeldung.setLocation(20,40);     
                	fehlermeldung.setText("<html><body>"+zeile+"</body></html>");	//Über HTML passt sich der Text automatisch an
                	fehlermeldung.setFont(new Font("Myriad Pro", Font.PLAIN, 13));
   
                //Überschrift des Frames	
                FEHLER = new JLabel();
                	FEHLER.setText("Fehler!");
                	FEHLER.setSize(70,25);
                	FEHLER.setLocation(10,4);
                	FEHLER.setFont(new Font("Myriad Pro", Font.PLAIN, 15));
                	FEHLER.setForeground(Color.BLACK);
                
                //Bestätigunsbutton
                ok = new JButton("ok");
                	ok.setName("sendButton");
                	ok.addActionListener(this);
                	ok.setSize(70,25);
                	ok.setLocation(80,100);
                
                //Zusammenstellen des Frames	
                errorFrame.setBounds(233, 300, 233, 155);
                errorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                errorFrame.setUndecorated(true);
                errorFrame.setAlwaysOnTop(true);  			
                errorFrame.getContentPane().add(fehlermeldung);
                errorFrame.getContentPane().add(ok);
                errorFrame.getContentPane().add(FEHLER);
                errorFrame.getContentPane().add(new DrawLabel("src/images/Error/Fehler.png",233,155));
                errorFrame.setLocationRelativeTo(null);
                errorFrame.setShape( new RoundRectangle2D.Double(0, 0, 233, 155, 25, 25) );
                errorFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                errorFrame.setVisible( true );
                
                /*
        		 * KeyListener für ok Button. 
        		 * Enter drücken erfüllt denselben Zweck wie den Button anzuklicken
        		 */
                ok.addKeyListener (new KeyAdapter() {
					public void keyPressed(KeyEvent e) {
         	             int key = e.getKeyCode();
         	             if (key == KeyEvent.VK_ENTER) {
         	            	errorFrame.dispose();
         	                }
         	             }
         	           }
         	        );
            }
            
        	
        	/**
    		 * Diese Methode wird ausgeführt sobald ein ActionEvent eintritt
    		 * In diesem Fall das anklicken des ok Buttons
    		 * @param event
    		 */
			public void actionPerformed(ActionEvent event) {
            	
        		String command = event.getActionCommand();
        		
        		try {
        			if (command.equals("ok")) {
        				errorFrame.dispose();   //Fehlermeldung wird duch Klicken auf "ok" wieder ausgeblendet		
        				}
    				} 
        		catch (Exception e) {	
				}		
            }
            
			/**
    		 * Actionlistener für den ok Button
    		 * @param loginListener
    		 */
            public void addLoginListener(ActionListener loginListener) {
                ok.addActionListener(loginListener);
            }
            /**
             * Standart Getter-Methode
             */
            public JFrame getFrame() {
                return errorFrame;
            }
            /**
             * Standart Getter-Methode
             */
            public JButton getok() {
                return ok;
            }

        }