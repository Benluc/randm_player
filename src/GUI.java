import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.synth.SynthLookAndFeel;

/**

 * Diese Klasse ist zustaendig fuer die Oberflaeche des Players

 * Sie positioniert und implementiert die Bedienelemte des Players

 * @author Martin Dobbermann, Friederike Danger, Martin Tonhauser, Tim Bohnenstingel
 * <br><br> Kommentierung: Friederike Danger, Marta Czerwinski

 * @version 1.0

 */

public class GUI
{
	/* Variablen des Hauptfensters (mainWindow) */
	/** enth‰lt die X-Koordinate des Hauptfenster */
	private  int X=0;
	/** enth‰lt die Y-Koordinate des Hauptfenster */
    private  int Y=0;
	/** bildet ein zentrales Hauptfenster */
    public JFrame mainWindow = new JFrame();				
	/** bildet das linke Radiolist-Panel bzw Ohr */   
    public JDialog radioListWindow = new JDialog();			
    /** bildet das rechte Radiostation-Panel bzw Ohr */  
	public JDialog radioStationWindow = new JDialog();		
    
    
   
	/** RadiolisteVisible, RadiostationVisible regelt die Sichtbarkeit der beiden Ohren */  
    public boolean rlVisible = true, rsVisible = true;
    
    
	
	/*
	 * mainWindow Bedienelemente
	 * Es werden JButton erstellt
	 */ 
	/** ist der Schlieﬂenbutton */  
	protected JButton exit;		
	/** ist der Minimierbutton */  								
	protected JButton minimize;			
	/** ist der Maximierbutton */  						
	protected JButton maximize;									
	/** ist der Button zum oeffnen/schliessen der Radioliste */ 
	protected JButton left_open_close;	
	/** ist der Button zum oeffnen/schliessen der Radioliste */ 						
	protected JButton right_open_close;		
	/** ist der Abspielbutton */ 					
    protected JButton play;	
	/** ist der Stopbutton */ 						    		
    protected JButton stop;		
	/** ist der Weiterbutton */ 					    		
    protected JButton next;	
	/** ist der Hintergrund fuer die Bildflaeche */ 								
    protected JButton imageLabelBg;								
	
	
	/*
	 * mainWindow Bedienelemente
	 * Es werden JLabel erstellt
	 */ 
	/** ist die Flaeche auf dem das Kuenstlerbild erscheint */ 
	protected JLabel imagePanel;	
	/** bildet die Flaeche auf der die textlichen Informationen zum gespielten Titel */ 							
    protected JLabel trackInfo;								
    
  
    /*
     * Slider
     * Der Slider ist eine Art Timeline, der zeigt wo man sich zeitlich in gespielten Titel befindet.
     */
	/** ist ein Textfeld in dem die Information steckt ueber die Spieldauer des gespielten Titels*/
	public JTextField DAUER = new JTextField(30);	// Das Textfield DAUER ist auf der Oberflaeche nicht zu sehen. Aber die Timeline Klasse greift auf die Information DAUER zu.
	
	/** ist unser Slider, der zeigt wo man sich zeitlich in gespielten Titel befindet */ 
	protected JSlider Anzeige;
	
	
    /*
     * radioListWindow Bedienelemente
     */
	/** ist der Best‰tigungsbutton fuer die Erstellung einer neuen Radioliste */ 
    protected JButton  sendRl;		
	/** ist das Texteingabefeld fuer die Erstellung einer neuen Radioliste */						
    protected JTextField insertRl;		
	/** ist ein Button mit einer neuerstellten Radioliste */						
    protected JButton radiolist;
	/** ist ein Button zum Loeschen der jeweiligen Radioliste */			
    protected JButton delete_radiolist;		
	/** Enthaelt die Ueberschrift des linken Ohres */						
    protected JLabel text;	// Die Ueberschrift des linken Ohres ist konstant.	
							
    
    

    /*
     * radioStationWindow Bedienelemente
     */
	/** ist ein Stringarray, das Genre, User und Artist beinhaltet */
    private String[] typAuswahl = {"Genre","User","Artist"};
    /** ist eine Auswahlbox mit Strings zur Bestimmung des Radiostationtyp */
    protected JComboBox<String> typBox;									
	/** ist ein Best‰tigungsbutton fuer die Erstellung einer neuen Radiotation */
    protected JButton sendRs;									
	/**  ist ein Texteingabefeld zur Erstellung einer neuen Radiostation*/
	protected JTextField insertRs; 	
	/**  ist ein Button mit einer neuhinzugefuegten Radiostation */							
	protected JButton radiostation;
	/**  ist ein Button zum Loeschen der jeweiligen Radiostation */
	protected JButton delete_radiostation;
	/**  ist eine Flaeche auf der der Name der ausgew‰hlten Radioliste als ueberschrift fuer das rechte Ohr angezeigt wird */
	protected JLabel selected_RadioList;						
	
	
	/** GUI Konstruktor 
	 * 
	 * L‰dt aus unserer XML-Datei den von uns erstellten Style mit den entsprechenden png-Dateien.
	 * Innerhalb des GUI Methode werden 3 weitere Methoden aufgerufen (initMainWindow(),initRadioListWindow() und initRadioStationWindow()).
	 */
    public GUI ()
    {
    	/*
    	 * Initialisierung des individuellen LookAndFeel
    	 */
    	try {
    			SynthLookAndFeel synth = new SynthLookAndFeel();
    			Class<GUI> aClass = GUI.class;
    			InputStream stream = aClass.getResourceAsStream("\\newFile.dtd");

    			if (stream == null) 
    			{
    				System.err.println("Keine Konfigurationsdatei vorhanden!");
    				System.exit(-1);                
    			}
    			// Datei fuer die Darstellung laden
    			synth.load(stream, aClass); 		
    			// Darstellung festlegen
    			UIManager.setLookAndFeel(synth);	
    	    } 
    	
    	catch (ParseException pe) 
    	{
    	    System.err.println("Fehlerhafte Konfigurationsdatei!");
    	    pe.printStackTrace();
    	    System.exit(-2);
    	} 
    	
    	catch (UnsupportedLookAndFeelException ulfe) 
    	{
    	    System.err.println("Ihre Javaversion wird nicht unterstuetzt, bitte aktualisieren Sie!");
    	    System.exit(-3);
    	}
    	
    	/*
    	 * Methoden zur Initialisierung der drei Fenster
    	 */
    	initMainWindow();
    	initRadioListWindow();
    	initRadioStationWindow();
    }
    
    
    
    
    /**
     * Laden des last.fm-Icons fuer die Taskleiste.
     * 
     * @param str Pfad zu Bilddatei
     * @return last.fm Logo
     */
    public BufferedImage getIconImage(String str) 
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
    
    
    

    /**
     * Initialisieren des Hauptfensters
     * 
     * Die oben erstellten Variablen werden hier in das Hauptfenster initialisiert.
     * Buttons werden erzeugt. 
     * 
     */
    public void initMainWindow()
    {						
         mainWindow.setLayout(null); 
         exit = new JButton();
     	 minimize = new JButton();						
     	 maximize = new JButton();						
     	 left_open_close = new JButton();				
     	 right_open_close = new JButton();	
         
         play = new JButton();
         stop = new JButton();
         next = new JButton();
         imagePanel = new JLabel();
         imageLabelBg = new JButton();
         trackInfo = new JLabel();
         Anzeige = new JSlider();
          
         /*
          * setName Name verbindet Button mit XML-Datei
          */
         exit.setName("exit_button"); 			
         maximize.setName("maximize_button");		
         minimize.setName("minim_button");		
         left_open_close.setName("left_button");		
         right_open_close.setName("right_button");  	 
         play.setName("play");					
         stop.setName("stop");					
         next.setName("next");							
         imageLabelBg.setName("ImageLabelBg");
         
         /*
          * setActionCommand leitet die Befehle an den ActionListener weiter.
          */
         exit.setActionCommand("exit");
         minimize.setActionCommand("minimize");
         left_open_close.setActionCommand("left_open_close");
         right_open_close.setActionCommand("right_open_close");
         play.setActionCommand("play");
         stop.setActionCommand("stop");
         next.setActionCommand("next");
         
          /*
           * setSize bestimmt die Breite und Hoehe der Buttons.
           */
          exit.setSize(30, 30);
          maximize.setSize(30, 30);
          minimize.setSize(30, 30);
          left_open_close.setSize(24, 134);
          right_open_close.setSize(24, 134);
          play.setSize(34, 39);
          stop.setSize(34, 39);
          next.setSize(49, 39);
          imageLabelBg.setSize(400,399);
          trackInfo.setSize(580,30);
         
          /*
           * setSize bestimmt die genaue Position der Buttons.
           */
          exit.setLocation(564,6);
          maximize.setLocation(530,6);   
          minimize.setLocation(496,6);   
          left_open_close.setLocation(0, 272);
          right_open_close.setLocation(576, 272);
          play.setLocation(258, 552);
          stop.setLocation(258, 552);
          next.setLocation(308, 552);
          trackInfo.setLocation(20,500);
          imageLabelBg.setLocation(100,92);
          imagePanel.setLocation(110, 102);
          
          Anzeige.setLocation(110, 460);
          Anzeige.setSize(380, 23);
          Anzeige.setValue(0);
  		  Anzeige.setEnabled(false);
          
          stop.setVisible(false);
          next.setEnabled(false);
          
          mainWindow.setIconImage(getIconImage("apple-touch-icon.png"));
          // Favicon einsetzen
  
          mainWindow.setUndecorated(true);
          mainWindow.setSize( 600, 600 );
          
          /*
           * getContentPane fuellt das MainWindow.
           */
          mainWindow.getContentPane().add(Anzeige);
          mainWindow.getContentPane().add(DAUER);
          mainWindow.getContentPane().add(imageLabelBg);
          mainWindow.getContentPane().add(imagePanel);
          mainWindow.getContentPane().add(left_open_close);
          mainWindow.getContentPane().add(exit);
          mainWindow.getContentPane().add(maximize);
          mainWindow.getContentPane().add(minimize);
          
          mainWindow.getContentPane().add(right_open_close);
          mainWindow.getContentPane().add(next);
          mainWindow.getContentPane().add(play);
          
          mainWindow.getContentPane().add(stop);
          mainWindow.getContentPane().add(trackInfo);
          mainWindow.getContentPane().setComponentZOrder(Anzeige, 0);
          
          /*
           * Klasse DrawLabel wird aufgerufen. Pfad und Groeﬂe werden bestimmt.
           */	
         mainWindow.getContentPane().add(new DrawLabel("src/images/randm_main/randm_main.png",600,600));
        
		
         System.out.println(mainWindow.getContentPane().getComponentZOrder(imageLabelBg)+"<---imageLabelBg--------");
         System.out.println(mainWindow.getContentPane().getComponentZOrder(Anzeige)+"<--Anzeige---------");
   
   
          mainWindow.setLocationRelativeTo(null);
          
          mainWindow.setShape( new RoundRectangle2D.Double(0, 0, 600, 600, 28, 28) );
          mainWindow.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
          mainWindow.setVisible( true );
          
          /*
           * addMouseListener reagiert auf Actionen der Mouse
           */
          mainWindow.addMouseListener(new MouseAdapter()
          {
            @Override
			public void mousePressed(MouseEvent e)
                  {
            		System.out.println("press");
            		X=e.getX();
            		Y=e.getY();
                  }
              
            @Override
			public void mouseReleased(MouseEvent e)
            	{
            	  mainWindow.setCursor(Cursor.DEFAULT_CURSOR);
             	}
          });
          
          /*
           * addMouseMotionListener faengt die Mousebewegungen ab und reagiert
           */
          mainWindow.addMouseMotionListener(new MouseMotionAdapter()
          {
            @Override
			public void mouseDragged(MouseEvent e)
                  {
            	  mainWindow.setLocation(mainWindow.getLocation().x+(e.getX()-X),mainWindow.getLocation().y+(e.getY()-Y));
                  radioStationWindow.setLocation(radioStationWindow.getLocation().x+(e.getX()-X),radioStationWindow.getLocation().y+(e.getY()-Y));
                  radioListWindow.setLocation(radioListWindow.getLocation().x+(e.getX()-X),radioListWindow.getLocation().y+(e.getY()-Y));
                  mainWindow.setCursor(Cursor.MOVE_CURSOR);
                  }  
          });
          
          
          mainWindow.addWindowListener(
      		new WindowAdapter() 
      		{
      			@Override
				public void windowIconified(WindowEvent event)
      			{
      				if(rlVisible)
      				{
      					radioListWindow.setVisible(false);
      					radioListWindow.repaint();
      				}
      				if(rsVisible)
      				{
      					radioStationWindow.setVisible(false);
      					radioStationWindow.repaint();
      				}
      			}
      			
      			@Override
				public void windowDeiconified(WindowEvent event)
      			{
      				if(rlVisible)
      				{
      					radioListWindow.setVisible(true);
      					radioListWindow.repaint();
      				}
      				if(rsVisible)
      				{
      					radioStationWindow.setVisible(true);
      					radioStationWindow.repaint();
      				}
      			}
			});
      	
      }
    
    
    
    
    
    /**
     * Initialisieren des Radiolisten-Fensters bzw linkes Ohr
     * 
     * Die oben erstellten Variablen werden hier in das linke Ohr initialisiert.
     * 
     */
    public void initRadioListWindow()
    {  	
    		radioListWindow.setLayout(null);
          
            insertRl = new JTextField("Neue RadioListe..."); 

            insertRl.addFocusListener(new java.awt.event.FocusAdapter() 
            {
            //leert TextField beim Anklicken
            	@Override
				public void focusGained(java.awt.event.FocusEvent evt) 
            	{
        			insertRl.setText("");
        		}
            });		
            
            sendRl = new JButton();
            text = new JLabel("Radiolisten");
            
            radioListWindow.setUndecorated(true);
            radioListWindow.setSize(210, 521);
            
            insertRl.setSize(158, 25);
            insertRl.setLocation(9, 60);
            insertRl.setName("RadioListInput");
            
            sendRl.setSize(25, 25);
            sendRl.setLocation(175, 60);
            sendRl.setActionCommand("sendRl");
            sendRl.setName("sendRl");
            
            text.setLocation(0,9);
            text.setSize(200, 30);
            text.setFont(new Font("Myriad Pro",Font.PLAIN,20));
            text.setForeground(Color.black);
            text.setHorizontalAlignment(SwingConstants.CENTER);
            
            radioListWindow.getContentPane().add(insertRl);
            radioListWindow.getContentPane().add(sendRl);
            radioListWindow.getContentPane().add(text);
      
            DrawLabel testdraw = new DrawLabel("src/images/left_ear/rl_left_ear.png",210,521);	
            radioListWindow.getContentPane().add(testdraw);
            System.out.println("zindex"+radioListWindow.getContentPane().getComponentZOrder(testdraw));

            System.out.println(radioListWindow.getBackground());
       
            radioListWindow.setLocation(mainWindow.getX()-214, mainWindow.getY()+79);
            
            radioListWindow.setShape(new RoundRectangle2D.Double(0, 0, 210, 521, 28, 28));
            radioListWindow.setVisible(rlVisible);
    }
     
    
    
    /**
     * Initialisieren des Radiostationen-Fensters bzw rechtes Ohr
     * 
     * Die oben erstellten Variablen werden hier in das rechtes Ohr initialisiert.
     * 
     */
    public void initRadioStationWindow()
    {
        radioStationWindow.setLayout(null);
        System.out.println(radioStationWindow.getLayout());
       
        typBox = new JComboBox<String>(typAuswahl);
    	// Auswahlfeld
        typBox.setName("comboBox");
        typBox.setLocation(9, 60);
    	typBox.setSize(72, 25);
    	
    	insertRs = new JTextField("Neue Radiostation..."); 
    	// Texteingabefeld fuer die Erstellung einer neuen Radioliste
    	
    	// leert TextField beim Anklicken
    	insertRs.addFocusListener(new java.awt.event.FocusAdapter() 
    	{
    		@Override
			public void focusGained(java.awt.event.FocusEvent evt) 
    		{
    			insertRs.setText("");
    		}
    	});
    	
        sendRs = new JButton();
        selected_RadioList = new JLabel("Radiostationen");
        
        insertRs.setSize(158, 25);
        insertRs.setLocation(9, 95);
        insertRs.setName("RadioStationInput");
        
        sendRs.setSize(25, 25);
        sendRs.setLocation(175, 95);
        sendRs.setActionCommand("sendRs");
        sendRs.setName("sendRs");
    
        selected_RadioList.setLocation(0,9);
        selected_RadioList.setSize(200, 30);
        selected_RadioList.setFont(new Font("Myriad Pro",Font.PLAIN,20));
        selected_RadioList.setForeground(Color.black);
        selected_RadioList.setHorizontalAlignment(SwingConstants.CENTER);
			        
        radioStationWindow.setUndecorated(true);
        radioStationWindow.setSize( 210, 521 );
       
        radioStationWindow.getContentPane().add(insertRs);
        radioStationWindow.getContentPane().add(sendRs);
        radioStationWindow.getContentPane().add(typBox);
        radioStationWindow.getContentPane().add(selected_RadioList);
      
        radioStationWindow.getContentPane().add(new DrawLabel("src/images/right_ear/rs_right_ear.png",210,521));
        radioStationWindow.getContentPane().setBackground(Color.BLUE);
        
        System.out.println(radioStationWindow.getBackground());
    
        // Positionierung nach dem Haupftfenster
        radioStationWindow.setLocation(mainWindow.getX()+604,mainWindow.getY()+79);
        
        radioStationWindow.setShape(new RoundRectangle2D.Double(0, 0, 210, 521, 28, 28) );
       
        radioStationWindow.setVisible(rsVisible);
    }
    
    
    /** 
     * Ruft die GUI auf
     *
     * @param args Kommandozeilenparameter
     */
  public static void main(String[] args)
  {
	  new GUI();   
  }

}
