
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.synth.SynthLookAndFeel;
import java.awt.event.KeyEvent;

/**
 * Die Login Klasse wird beim Starten des Players zuerst ausgeführt.
 * Sie erstellt ein Anmeldefenster und fordert den User auf sich mit Benutzername
 * und Kennwort von lastfm anzumelden. Die Anmeldedaten werden der MainPlayer Klasse übergeben
 * und das Fenster geschlossen. Bei einer fehlerhaften Anmeldung wird Login erneut ausgeführt.
 * @author Martin Tonhauser, Michael Weidner, Marta Czerwinski <br><br> Kommentierung: Michael Weidner
 * @version 1.0
 */
        public class Login implements ActionListener {

        	/**exit Exit Button */
        	protected JButton exit;	
        	/**minimize Minimieren Button */
        	protected JButton minimize;			
        	/**maximize Maximieren Button */
        	protected JButton maximize;			
        	/**loginWindow Frames des Login Fensters */
        	protected JFrame loginWindow = new JFrame();
        	/**Benutzername Eingabefeld für Benutzername */
        	protected JTextField Benutzername = new JTextField(30);		
        	/**Kennwort Eingabefeld für Passwort */
        	protected JPasswordField Kennwort = new JPasswordField(30);		
        	/**KennwortHin Hintergrund für JPasswordField */
        	protected JTextField KennwortHin = new JTextField(30);			
        	/**einloggen Einlogg Button */
        	protected JButton einloggen = new JButton();	
        	/**login,pw String login = Benutzername, pw = Passwort */
        	protected String login, pw;
        	/**Benutzer Anzeige von "Benutzername" */
        	protected JLabel Benutzer = new JLabel();		
        	/**Passwort Anzeige von "Kennwort" */
        	protected JLabel Passwort = new JLabel();		
        	/**Info Anzeige von einem Infotext*/
        	protected JLabel Info = new JLabel();			
           
            
        	
        	/**
        	 * Konstruktor
        	 * 
        	 * Login() greif auf die Konfigurationsdatei zurück und lädt sich
        	 * aus der xml Datei das Design. Anschließend wird die Methode initloginWindow()
        	 * zum erstellen des Anmeldefensters aufgerufen
        	 */
            public Login() {
            	try {
            	    SynthLookAndFeel synth = new SynthLookAndFeel();
            	     Class<GUI> aClass = GUI.class;
            	    InputStream stream = aClass.getResourceAsStream("\\newFile.dtd");
            	    

            	    if (stream == null) {
            	        System.err.println("Keine Konfigurationsdatei vorhanden!");
            	        
            	        System.exit(-1);                
            	    }

            	    synth.load(stream, aClass); 		// Datei für die Darstellung laden

            	    UIManager.setLookAndFeel(synth);	// Darstellung festlegen
            	   
            	} catch (ParseException pe) {
            	    System.err.println("Fehlerhafte Konfigurationsdatei!");
            	    pe.printStackTrace();
            	    System.exit(-2);
            	} catch (UnsupportedLookAndFeelException ulfe) {
            	    System.err.println("Ihre Javaversion wird nicht unterstützt, bitte aktualisieren Sie!");
            	    new Error("Ihre Javaversion wird nicht unterstützt, bitte aktualisieren Sie!");
            	    System.exit(-3);
            	}

            	 initloginWindow();	//Methode zum Aufbau des Anmeldefensters
                
            }

            
            /**
    		 * Diese Methode wird ausgeführt sobald ein ActionEvent eintritt
    		 * In diesem Fall wird durch das Klicken auf den Login Button
    		 * der eingegebene Benutzername sowie Kennwort der MainPlayer Klasse
    		 * übergeben. Anschließend das Anmeldefenster geschlossen.
    		 * Minimieren und Schließen des Fensters ist ebenfalls möglich
    		 * @param event enthält die ActionEvent-Informationen
    		 */
			public void actionPerformed(ActionEvent event) {
            	
        		String command = event.getActionCommand();
        		
        		try {
        			//einloggen
        			if (command.equals("Login")) {
        				login = Benutzername.getText();
        				pw = Kennwort.getText();
        				
        				new MainPlayer(login,pw);
        				loginWindow.dispose();	//Fenster schließen
        				    				
        				
        			}
        			//minimieren
        			else if (command.equals("minimize")) {
        				loginWindow.setState(Frame.ICONIFIED);
        			}		
        			// schließen
        			else if (command.equals("exit")) {
        				loginWindow.dispose();
        				System.exit(0); 
        			} }
        			catch (Exception e) {
					// TODO: handle exception
				}		
        		
            }
            
			/**
    		 * ActionListener für Einlog-Button
    		 * @param loginListener enthält die ActionListener-Informationen
    		 */
            public void addLoginListener(ActionListener loginListener) {
                einloggen.addActionListener(loginListener);
            }

			/**
    		 * KeyListener für Kennwort-Eingabefeld
    		 * @param keyListener enthält die KeyListener-Informationen
    		 */
            public void addKeyListener(KeyListener keyListener) {
                Kennwort.addKeyListener(keyListener);
            }

			/**
    		 * getUsername Methode
    		 * @return Benutzername
    		 */
            public JTextField getUsername() {
                return Benutzername;
            }
			/**
    		 * getPassword Methode
    		 * @return Kennwort
    		 */
            public JTextField getPassword() {
                return Kennwort;
            }
			/**
    		 * getFrame Methode
    		 * @return loginWindow
    		 */
            public JFrame getFrame() {
                //return frame;
            	return loginWindow;
            }
			/**
    		 * getSendButton Methode
    		 * @return einloggen
    		 */
            public JButton getSendButton() {
                //return sendButton;
            	return einloggen;
            }
            
            
            /**
             * Zum Aufbau des Login-Fensters
             */
           public void initloginWindow(){
        	     
        	   /*
        	    * Elemente des Login Fensters
        	    */
        	   Benutzername = new JTextField();			//Textfeld für Benutzername
        	   Kennwort = new JPasswordField();			//Textfeld für Kennwort
        	   einloggen = new JButton("Login");		//Einlog Button
        	   Benutzer = new JLabel("Benutzername: ");	//Text: "Benutzername: "
        	   Passwort = new JLabel("Kennwort: ");		//Text: "Kennwort: "
        	   Info = new JLabel();						//Infotext
        	   KennwortHin = new JTextField();			//Hintergrund für PasswordField, damit es aussieht wie das Textfeld für Benutzername
        	   
        	   Benutzer.setSize(140, 35);
        	   Benutzer.setLocation(150,180);
        	   Benutzer.setFont(new Font("Myriad Pro", Font.LAYOUT_LEFT_TO_RIGHT, 15));
        	  
        	   Passwort.setSize(140, 35);
        	   Passwort.setLocation(150,230);
        	   Passwort.setFont(new Font("Myriad Pro", Font.LAYOUT_LEFT_TO_RIGHT, 15));
        	   
        	   Info.setSize(300,35);
        	   Info.setLocation(150,330);
        	   Info.setFont(new Font("Myriad Pro", Font.PLAIN, 15));
        	   Info.setText("<html><body>Bitte melde dich mit deinem lastfm Account an.</body></html>");
        	   
        	   Benutzername.setSize(200, 30);
        	   Benutzername.setLocation(260,180);
        	   Benutzername.setName("comboBox");
        	   Benutzername.setText("party-scrobbler");									//ACHTUNG! ersetzen durch "" vor veröffentlichung 
        	   Benutzername.setFont(new Font("Myriad Pro", Font.LAYOUT_LEFT_TO_RIGHT, 16));
        	   Benutzername.addFocusListener(new java.awt.event.FocusAdapter() {
           		@Override
				public void focusGained(java.awt.event.FocusEvent evt) {	//sobald das Textfeld den Focus hat wir der aktuelle Inhalt entfernt
           			Benutzername.setText("");}});	
        	    
        	   KennwortHin.setName("comboBox");
        	   KennwortHin.setLocation(260, 230);
        	   KennwortHin.setSize(200, 30);
        	   KennwortHin.setFocusable(false);
        	   KennwortHin.setEnabled(false);
        	   
        	   Kennwort.setName("comboBox");
        	   Kennwort.setLocation(270, 233);
        	   Kennwort.setSize(180, 30);
        	   Kennwort.setText("hanswurst");												//ACHTUNG! ersetzen durch "" vor veröffentlichung
        	   Kennwort.setFont(new Font("Myriad Pro", Font.LAYOUT_LEFT_TO_RIGHT, 16));
        	   Kennwort.addFocusListener(new java.awt.event.FocusAdapter() {
              		@Override
					public void focusGained(java.awt.event.FocusEvent evt) {	
              			Kennwort.setText("");}});		
        	   
        	   einloggen.setText("Login");
        	   einloggen.setSize(100, 25);
        	   einloggen.setName("login_Button");
        	   einloggen.setLocation(280, 280);
        	   einloggen.addActionListener(this);
        	   einloggen.setFont(new Font("Myriad Pro", Font.PLAIN, 14));
      	   
        	   
        	   /*
        	    * Key Listener für Kennwortfeld.
        	    * Durch drücken von Enter wird der Login ausgeführt
        	    */
        	   Kennwort.addKeyListener (new KeyAdapter() {
        	         @Override
					public void keyPressed(KeyEvent e) {
        	             int key = e.getKeyCode();
        	             if (key == KeyEvent.VK_ENTER) {
            				login = Benutzername.getText();
            				pw = Kennwort.getText();          				
            				new MainPlayer(login,pw);
            				loginWindow.dispose();
        	                }
        	             }
        	           }
        	        );
        	   
        	   /*
        	    * Key Listener für Benutzernamefeld.
        	    * Durch drücken von Enter wird der Focus 
        	    * auf das Passwortfeld gelegt
        	    */
        	   Benutzername.addKeyListener (new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
      	             int key = e.getKeyCode();
      	             if (key == KeyEvent.VK_ENTER) {
      	            	Kennwort.setFocusable(true);
      	            	Benutzername.setNextFocusableComponent(Kennwort);
      	            	Benutzername.transferFocus(); 

      	                }
      	             }
      	           }
      	        );

        	   
        	   /*
        	    * Exit und Minimieren Buttons
        	    */
               exit = new JButton();						// exit Button
          	   minimize = new JButton();					// minimiern Button
          	   maximize = new JButton();					// maximieren wird angezeigt, hat aber keine Funktion
               exit.setName("exit_button"); 			     
               maximize.setName("maximize_button");		
               minimize.setName("minim_button");		
               exit.setSize(30, 30);
               maximize.setSize(30, 30);
               minimize.setSize(30, 30);
               exit.setLocation(564,6);
               maximize.setLocation(530,6);   
               minimize.setLocation(496,6);         
               exit.setActionCommand("exit");
               exit.addActionListener(this);
               minimize.setActionCommand("minimize");
               minimize.addActionListener(this);
        	   
               /*
        	    * Zusammenstellen des Login Fensters
        	    */
        	   loginWindow.setUndecorated(true);
        	   loginWindow.setSize( 600, 600 );
        	   loginWindow.getContentPane().add(Benutzername);
               loginWindow.getContentPane().add(Kennwort);
               loginWindow.getContentPane().add(einloggen);
               loginWindow.getContentPane().add(exit);
               loginWindow.getContentPane().add(maximize);
               loginWindow.getContentPane().add(minimize);
               loginWindow.getContentPane().add(Benutzer);
               loginWindow.getContentPane().add(Passwort);
               loginWindow.getContentPane().add(Info);
               loginWindow.getContentPane().add(KennwortHin);
               loginWindow.setIconImage(getIconImage("apple-touch-icon.png"));
        	   loginWindow.getContentPane().add(new DrawLabel("src/images/randm_main/randm_main.png",600,600));   
        	   loginWindow.setLocationRelativeTo(null);              
        	   loginWindow.setShape( new RoundRectangle2D.Double(0, 0, 600, 600, 28, 28) );
        	   loginWindow.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        	   loginWindow.setVisible( true );
            	
            }
          
           /**
    	    * Diese Methode dient zum laden des Icon-Bildes.
    	    * Bei der Zusammenstellung des Login Fensters wird
    	    * diese Methode aufgerufen und der Pfad des Icos mit übergeben
    	    * @param str Speicherpfad 
    	    * @return icon 
    	    */
           public  BufferedImage getIconImage(String str) 
       	{
       		InputStream is = this.getClass().getResourceAsStream(str);
       		BufferedImage icon = null;
       		try 
       		{
       			icon = ImageIO.read(is);
       		} 
       		catch (IOException e) 
       		{
       			e.printStackTrace();
       		}
       		return icon;
    

        }
           
     }