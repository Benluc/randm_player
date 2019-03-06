
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import de.umass.lastfm.Artist;
import de.umass.lastfm.Authenticator;
import de.umass.lastfm.Image;
import de.umass.lastfm.Playlist;
import de.umass.lastfm.Radio;
import de.umass.lastfm.Session;
import de.umass.lastfm.Tag;
import de.umass.lastfm.Track;
import de.umass.lastfm.User;

/**
 * Diese Klasse ist das Herz der Anwendung
 * Sie enthält unteranderem Funktionen zum starten und stoppen des Players.<br><br>
 * 
 * Hinweis: Diese Klasse importiert die umass-Klassenbibliothek (JAVA-Schnittstelle zu last.fm) und die javazoom-Klassenbibliothek (stellt einfache Methoden zum abspielen von Audiodaten bereit)
 * 
 * @author Martin Dobbermann <br><br> Kommentierung: Martin Dobbermann
 * @version 1.0
 */
public class MainPlayer implements Runnable, ActionListener {
	
	/* Playervariablen */
	/** Benutzername */
	private String user;
	/** Benutzerpasswort */
	private String pw;
	/** Applikationspasswort wird von last.fm angefordert um die Anwendung zuweisen zu können */
	private final String apiKey = "afefcb5301bf18e69ebaafbbdf195e6e"; 	
	/** enthält die aktuellte last.fm-Sitzung */
	private Session session;
	/** stellt einen Zugang zu den last.fm-Radiodiensten her */
	private Radio radio = null;		
	/** stellt Methoden bereit um Musikdaten von last.fm abrufen zu können */
	private Playlist playlist = null;
	/** URL zur mp3-File */
	private URL url = null;
	/** Stream mit Lesezugriff */
	private InputStream fin = null;
	/** Gepuffertet Stream mit Lesezugriff */
	private BufferedInputStream bin = null;
	/** einfache Methoden zum abspielen von Audiodateien bereit */
	private Player mp3 = null;
	/** stellt Methoden bereit um Radiolisten zu laden und zuspeichern */
	private RadioList rl = new RadioList("");
	/** bestimmt die visuelle Darstellung des Players */
	private GUI gui = new GUI();	
	/** bestimmt ob Musik abgespielt werden soll */
	private boolean threadStart = false;
	/** 
	 * easter egg 
	 * 
	 * Darstellung und Transformation von Bildern 
	 */
	private Processing hase; 											
	/** enthält die Laufzeit des aktuellen Titels */
	private int aktuelleDuration;
	/** enthält Methoden für das für einen Timer*/
	private Timeline timeline;
	/** enthält Informationen über den aktuell angewählten Radiolisten-Button */
	private JButton clickedRadiolist = new JButton();	 				
	/** enthält Informationen über den aktuell angewählten Radiostation-Button */
	private JButton selected_RadioStation;
	
	
	
	/** 
	 * Konstruktor 
	 * 
	 *  Er startet die Sitzung mit Last.fm und den Thread Player.
	 *  Zusätzlich weist er die Aktionsüberwachung den Bedienelementen zu und lädt schließlich die Radiolisten aus der Textdatei über die Methode "loadRadiolists()"
	 *
	 * @param user Benutzername
	 * @param pw Benutzerpasswort  
	 */
	public MainPlayer(String user, String pw) {
		
		this.user = user;
		this.pw = pw;
		
		/*
		 * Setzen der Sitzungsvariable.
		 * Falls dies nicht möglich ist neuer Loginversuch.
		 */
		
		session = Authenticator.getMobileSession(this.user, this.pw, apiKey, "2fd87ff7f35bf0b32076cad862503ecc");
		if(session == null) {
			gui.mainWindow.setVisible(false);
			gui.mainWindow.dispose();
			gui.radioListWindow.dispose();
			gui.radioStationWindow.dispose();
			new Error("Benutzername oder Passwort falsch oder keine Internetverbindung");
			new Login();
		}
		new Thread( this , "Player").start(); // hier wird der Thread gestartet, der für das abspielen der Musik verantwortlich ist
		
		/*
		 * Zuweisung der Aktionsüberwachung auf die jeweiligen Bedienelemente
		 */
		// mainWindow
		gui.exit.addActionListener(this);
		gui.minimize.addActionListener(this);
		gui.maximize.addActionListener(this);
		gui.left_open_close.addActionListener(this);
		gui.right_open_close.addActionListener(this);
		gui.play.addActionListener(this);
		gui.stop.addActionListener(this);
		gui.next.addActionListener(this);
		// radioListWindow
		gui.sendRl.addActionListener(this);
		gui.insertRl.addActionListener(this);
		// radioStationWindow
		gui.sendRs.addActionListener(this);
		gui.insertRs.addActionListener(this);
		gui.typBox.addActionListener(this);
	
		loadRadiolists(); // laden der Radiolisten aus der Textdatei
	}
	
	/** 
     * Ruft das Loginfenster auf
     *
     * @param args Kommandozeilenparameter
     */
	public static void main(String[] args) {
		new Login();
	}
	
	/**
	 * Steuerung der getätigten Aktionen auf der GUI-Oberfläche
	 * 
	 * @param event enthält die ActionEvent-Informationen
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
	
		String command = event.getActionCommand(); // enthält den aktuellen ActionCommand
		try {
			// Player starten	
			if (command.equals("play")) {
			
				if(rl.RadioStationContent.size() == 0){
					new Error("Es muss mindestens eine Radiosation existieren");
				}
				else{
				startStream(random(rl.RadioStationContent.size())); // übergibt Vectorgroesse an die Methode random()
				gui.play.setVisible(false);
				gui.stop.setVisible(true);
				gui.next.setEnabled(true);
				}
			}
			// Player stoppen
			else if (command.equals("stop")) {
				stopStream();
				gui.stop.setVisible(false);
				gui.play.setVisible(true);
				gui.next.setEnabled(false);
			}
			// Song Ueberspringen
			else if (command.equals("next")) {
				stopStream();
				startStream(random(rl.RadioStationContent.size()));
			}					
			// Radiostation hinzufuegen
			else if (command.equals("sendRs")) {

				boolean doesexist = false;
				
				try
				{		
					//Gibt Fehlermeldung aus falls keine Bezeichnung fuer eine Station eingegeben ist	
					if (gui.insertRs.getText().isEmpty())
					{	
						new Error("Bitte zuerst Bezeichnung eingeben!");
						return;
					}
					if(rl.RadioStationContent.size()>=10){
						new Error("Fehlermeldung es sind nciht mehr als 10 RadioStationen por Liste möglich");
					}else{
						if(clickedRadiolist.getText()!=""){	
							//ruft checkType Mehode auf. Sollte Name nicht existieren wird false übergeben	
							doesexist = checkType(gui.typBox.getSelectedItem().toString(), gui.insertRs.getText());
							if (!doesexist)
							{	
								//Fehlernachricht falls Station nicht gefunden werden kann
								new Error(gui.typBox.getSelectedItem().toString() + " - " + gui.insertRs.getText()+ "- konnte nicht gefunden werden");
								return;
							}				
							for ( int j=0; j < rl.RadioStationContent.size(); j++ )
							{
								
								if (rl.RadioStationContent.elementAt(j).name.equals(gui.insertRs.getText()))
								{
									if (rl.RadioStationContent.elementAt(j).typ.equals(gui.typBox.getSelectedItem().toString()))
									{
										new Error("Bezeichnung bereits in der Playlist");	
										return;
									}
								}			
							}
							addRadioStation(gui.insertRs.getText()); // Radiostation visuell darstellen
							rl.saveRadioStation(gui.insertRs.getText(), gui.typBox.getSelectedItem().toString(), clickedRadiolist.getText());
							rl.addRadioStation(gui.typBox.getSelectedItem().toString(), gui.insertRs.getText(), true); // Radiostation als Dateninformation hinterlegen
						}else{
							new Error("Es muss eine Radioliste ausgewählt sein");
						}
					}
					
					} 
					catch (Exception e) 
					{
					System.out.println("Exception bei Hinzufuegen"+e);
					new Error(gui.typBox.getSelectedItem().toString() + " - " + gui.insertRs.getText()+ "- konnte nicht gefunden werden");
					}		
			} 
			// Radiostation löschen
			else if (command.startsWith("delete_rs_")) 
			{						
				int saveY = 0;

				for(Component b : gui.radioStationWindow.getContentPane().getComponents())
				{
					if(b.getName()!= null)
					{
						if(b.getName().equals(command))
						{
							saveY = b.getY();
							gui.radioStationWindow.getContentPane().remove(b);
							gui.radioStationWindow.getContentPane().remove((JButton) event.getSource()); // entfernt den delete Button
						}
					}
				}
				// alle Elemente des radioStationWindow, die unter dem entfernten Element standen rücken hoch
				for(Component b :gui.radioStationWindow.getContentPane().getComponents())
				{
					if(b.getName()!= null)
					{
						if(b.getY()>saveY)
						{
							b.setLocation(b.getX(), b.getY()-35);
						}
					}
				}
				rl.deleteRadioStation(clickedRadiolist.getText(),command.substring(10)); // Der Name der zu löschenden Radiostation wird hier bestimmt, das Prefix "delete_rs_" wird dafür entfernt.
				rl.loadRadioStation(clickedRadiolist.getText());  // Vector aktualisieren
				
				gui.radioStationWindow.getContentPane().repaint();

			}			
			// Radioliste hinzufuegen
			else if (command.equals("sendRl")){
				
				//Gibt Fehlermeldung aus falls keine Bezeichnung fuer eine Station eingegeben ist	
				if (gui.insertRl.getText().isEmpty())
				{	
					new Error("Bitte zuerst Bezeichnung eingeben!");
					return;
				}			
				
				if(rl.existRadioListName(gui.insertRl.getText())){
					new Error("Die Radioliste konnte nicht erstellt werden, da die Bezeichung schon vergeben ist");
				}else{
					if(rl.RadioListContent.size()>=10){
						new Error("Ees sind nciht mehr als 10 RadioListen möglich");
					}else{
					addRadioList(gui.insertRl.getText());
					rl.saveRadioList(gui.insertRl.getText());
					rl.loadRadioList();
					}
				}
			}
			// Radioliste aufrufen/öffnen
			else if (command.startsWith("rl_name_"))
			{
				// Radiolistselektierung
				if(!clickedRadiolist.equals(event.getSource()))
				{
					clickedRadiolist.setSelected(false); // die Selektierung vom vorherigen Element entfernen
					clickedRadiolist =  (JButton) event.getSource();
					clickedRadiolist.setSelected(true); // das neue Element selektieren
					
				}
				
				gui.selected_RadioList.setText(clickedRadiolist.getText());
				gui.radioStationWindow.setVisible(true);
				gui.rsVisible = true;
				gui.radioStationWindow.repaint();
				loadRadioStations();

			}
			 //Radioliste speichern
			else if (command.equals("Radioliste speichern")) 
			{	
				rl.saveRadioList(gui.insertRl.getText());
			}
			// Radioliste entfernen
			else if (command.startsWith("delete_rl_"))
			{
				int saveY = 0;

				for(Component b :gui.radioListWindow.getContentPane().getComponents())
				{
					
					if(b.getName()!= null)
					{
						if(b.getName().equals(command))
						{
							if(b.getName().equals(clickedRadiolist.getName())){
								clickedRadiolist = new JButton();
								removeAllRadioStation();
							}
							saveY = b.getY();
							gui.radioListWindow.getContentPane().remove(b);
							gui.radioListWindow.getContentPane().remove((JButton) event.getSource());
							
						}
					}
				}
				// alle Elemente des radioListWindow, die unter dem entfernten Element standen rücken hoch
				for(Component b :gui.radioListWindow.getContentPane().getComponents())
				{
					if(b.getName()!= null)
					{
						if(b.getY()>saveY)
						{
							b.setLocation(b.getX(), b.getY()-35);
						}
					}
				}
				
				rl.deleteRadioList(command.substring(10)); // Der Name der zu löschenden Radioliste wird hier bestimmt, das Prefix "delete_rl_" wird dafür entfernt.
				rl.loadRadioList(); // das Ganze einmal aktualisieren
				
				gui.radioListWindow.getContentPane().repaint();
			}
			// Player minimieren
			else if (command.equals("minimize")) {
					gui.mainWindow.setState(Frame.ICONIFIED);
					
			}
			// Player schließen
			else if (command.equals("exit")) {
				gui.mainWindow.dispose();
				System.exit(0);
			}
			// linkes Ohr einklappen/aufklappen
			else if (command.equals("left_open_close")) {
				if(gui.rlVisible)
				{
					gui.radioListWindow.setVisible(false);
					gui.rlVisible = false;
				}else{
					gui.radioListWindow.setVisible(true);
					gui.rlVisible = true;
				}
			}
			// rechtes Ohr einklappen/aufklappen
			else if (command.equals("right_open_close")) {
				if(gui.rsVisible)
				{
					gui.radioStationWindow.setVisible(false);
					gui.rsVisible = false;
				}else{
					gui.radioStationWindow.setVisible(true);
					gui.rsVisible = true;
				}
			}

		} 
		catch (NullPointerException e) {
				System.err.println(e.toString());
		} 
		catch (Exception e) {
			System.err.println(e.toString());
		} 
	}
	
	/**
	 * Startet Stream, bezieht URL von last.fm
	 * und stellt das Künstlerbild zur gespielten Musik dar
	 * 
	 * @param index Enthält die Position für die Radiostation
	 */
	public void startStream(int index) {
		
		// Intialisierung der playlist
		if(rl.RadioStationContent.elementAt(index).typ.equals("User")){
			radio = Radio.tune(Radio.RadioStation.library(rl.RadioStationContent.elementAt(index).name), session);
		}
		else if (rl.RadioStationContent.elementAt(index).typ.equals("Genre")) {
			radio = Radio.tune(Radio.RadioStation.tagged(rl.RadioStationContent.elementAt(index).name), session);	
		}
		else if (rl.RadioStationContent.elementAt(index).typ.equals("Artist")) {
			radio = Radio.tune(Radio.RadioStation.similarArtists(rl.RadioStationContent.elementAt(index).name), session);	
		} 
		playlist = radio.getPlaylist();
		

		// Gespielte Radiostation anzeigen
		for(Component b : gui.radioStationWindow.getContentPane().getComponents()){
			if(b.getName()!= null)
			{
				if(b.getName().equals("delete_rs_"+rl.RadioStationContent.elementAt(index).name))
				{
					if(selected_RadioStation!=null){
						selected_RadioStation.setSelected(false);
					}
					selected_RadioStation = (JButton) b;
					selected_RadioStation.setSelected(true);
				}
			}
		}
		
		hase = new Processing(playlist.getTracks(),apiKey);
		
		int i = 0;
		// enthält 6 Tracks, wir wollen aber nur den ersten
        for (Track track : playlist.getTracks()) {
			
			i++;
			if(i==1){
				int counter = 0;
				for(Image img : Artist.getImages(track.getArtist(), apiKey).getPageResults()){
					counter++;
					if(counter==1){
						
						try {
							url = new URL(track.getLocation());				
			        		fin = url.openStream();
			        		bin = new BufferedInputStream(fin);
			        		mp3 = new Player(bin);  		
			   			} 

						
						catch (Exception e) {
							System.err.println(e.toString());
							stopStream();
							startStream(random(rl.RadioStationContent.size()));
							
						}
						threadStart = true;

					    gui.imagePanel.add(hase);
					    gui.imagePanel.setSize(380,360);
					    gui.imagePanel.setLocation(110, 102);
					    gui.mainWindow.getContentPane().setComponentZOrder(gui.imagePanel, 2);
					    gui.mainWindow.getContentPane().repaint();
				        gui.imagePanel.setVisible(true);	
					}
				} 
				
				  	//Track Info in der Gui anzeigen
			        gui.trackInfo.setFont(new Font("Myriad Pro",Font.PLAIN,18));
			        gui.trackInfo.setText( track.getArtist() +" | " + track.getName());
			        gui.trackInfo.setHorizontalAlignment( SwingConstants.CENTER);
			 
			        // Laufzeit des Tracks bestimmen
			        aktuelleDuration = track.getDuration();
     
			        //initialisieren des Bildes
			        hase.init(); 
			}
		}	
	}
	/**
	 * Hier wird die eigentlich mp3-Datei abgespielt
	 */
	@Override
	public void run() {
		while(true)
		{
		// dem Thread ein Päuschen gönnen	
		try {
			Thread.sleep(50);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(threadStart){							
			try { 
				
				timeline = new Timeline(aktuelleDuration, gui);
				mp3.play();

			} 	
			catch (JavaLayerException e) {
				e.printStackTrace();
				new Error ("Datei beschädigt. Eventuell den Player neustarten");
			}
			// falls nichts anderes gewünscht spiele jetzt einfach den nächsten Titel
			if(threadStart) {
				hase.stop = false;
				gui.imagePanel.remove(hase);
				startStream(random(rl.RadioStationContent.size()));
			}
		}
		}
	}
	
	/**
	 *  Gibt ein Zufallszahl aus 
	 *  
	 *  @param zufallsGroesse Zahlenbereich
	 *  @return enthält eine zufällig generierte Zahl, die später ein Position im Vector darstellt
	 */
	public static int random(int zufallsGroesse) {
		Random r = new Random();
		int i = Math.abs(r.nextInt(zufallsGroesse));
		return i;
	}

	/**
	 *  Stoppt aktuelle Liste und Song 
	 */
	public void stopStream() {
			hase.stop = false;
			gui.imagePanel.remove(hase);
			
			// stoppt das Processing zum nächsten Durchlauf
			threadStart = false;
			// stoppt den Durationtimer
			timeline.stopTimer();
			mp3.close();
			
	}
	
	/**
	 * Stellt hinzugefügte Radiostation visuell dar
	 * @param Filler Enthält Namen der Radiostation
	 */
	public void addRadioStation(String Filler){
		
		
		gui.radiostation = new JButton(Filler);
		gui.delete_radiostation = new JButton();
		
		gui.delete_radiostation.setName("delete_rs_");
		gui.delete_radiostation.setActionCommand("delete_rs_"+Filler); 
		gui.delete_radiostation.addActionListener(this);
		gui.delete_radiostation.setSize(25,25);
		gui.delete_radiostation.setLocation(gui.typBox.getX()+165, gui.typBox.getY());
		
		
		gui.radiostation.setName("delete_rs_"+Filler);
		gui.radiostation.setActionCommand("rs_name_"+Filler); 
		gui.radiostation.addActionListener(this);
		gui.radiostation.setRolloverEnabled(false);
		
		gui.radiostation.setSize(158, 25);
		// der neue Radiostations-Button rutscht auf die aktuelle Position der Eingabesteuerung
		gui.radiostation.setLocation(gui.typBox.getX(), gui.typBox.getY());
		
		// die Eingabesteuerung rutscht ein Stück nach unten
		gui.sendRs.setLocation(gui.sendRs.getX(),gui.sendRs.getY()+35);
		gui.insertRs.setLocation(gui.insertRs.getX(), gui.insertRs.getY()+35);	
		gui.typBox.setLocation(gui.typBox.getX(),gui.typBox.getY()+35);
		
		
		gui.radioStationWindow.getContentPane().add(gui.radiostation);
		gui.radioStationWindow.getContentPane().add(gui.delete_radiostation);
		gui.radioStationWindow.getContentPane().setComponentZOrder(gui.radiostation, 2);
		gui.radioStationWindow.getContentPane().setComponentZOrder(gui.delete_radiostation, 2);
		

		gui.radioStationWindow.getContentPane().repaint();
		
	}
	/**
	 * Stellt hinzugefügte Radioliste visuell dar
	 * @param Filler Enthält Namen der Radioliste
	 */
	public void addRadioList(String Filler){
		
		
		gui.radiolist = new JButton(Filler);
		gui.delete_radiolist = new JButton();
		
		gui.delete_radiolist.setName("delete_rl_");
		gui.delete_radiolist.setActionCommand("delete_rl_"+Filler);
		gui.delete_radiolist.addActionListener(this);
		gui.delete_radiolist.setSize(25,25);
		gui.delete_radiolist.setLocation(gui.sendRl.getX(), gui.sendRl.getY());
		
		gui.radiolist.setName("delete_rl_"+Filler);
		gui.radiolist.setActionCommand("rl_name_"+Filler); 
		gui.radiolist.addActionListener(this);
		gui.radiolist.setSize(158, 25);
		// der neue Radioslisten-Button rutscht auf die aktuelle Position der Eingabesteuerung
		gui.radiolist.setLocation(gui.sendRl.getX()-165, gui.sendRl.getY());
		
		
		// die Eingabesteuerung rutscht ein Stück nach unten
		gui.sendRl.setLocation(gui.sendRl.getX(),gui.sendRl.getY()+35);
		gui.insertRl.setLocation(gui.insertRl.getX(), gui.insertRl.getY()+35);	
		
		gui.radioListWindow.getContentPane().add(gui.radiolist);
		gui.radioListWindow.getContentPane().add(gui.delete_radiolist);
		gui.radioListWindow.getContentPane().setComponentZOrder(gui.radiolist, 2);
		gui.radioListWindow.getContentPane().setComponentZOrder(gui.delete_radiolist, 2);

		gui.radioListWindow.getContentPane().repaint();
		
	}
	/**
	 * Stellt Radiostationen als Dateninformation bereit
	 */
	public void loadRadioStations()
	{	
		
		if(clickedRadiolist!=null)
		{
	
	        // nun die Radiostationen zur gewählten Radioliste laden
			rl.loadRadioStation(clickedRadiolist.getText());
			
			// vohrer einmal die RadioStationen visuell entfernen
			removeAllRadioStation();
		
			for ( int j=0; j < rl.RadioStationContent.size(); j++ )
			{		
				addRadioStation(rl.RadioStationContent.elementAt(j).name);
				
			}	
		}	
	}
	/**
	 * Die Radioliste aus der Textdatei laden
	 */
	public void loadRadiolists(){

		rl.loadRadioList();
		for ( int j=0; j < rl.RadioListContent.size(); j++ ) 
		{
			addRadioList(rl.RadioListContent.elementAt(j).listName);
		}
	}
	

	
	/**
	 * Überprüft Daten mit lastfm
	 * @param type Enthält den Typ der Radiostation
	 * @param name Name der Radiostation
	 * @return boolean
	 */
	public boolean checkType(String type, String name){	

		boolean exist = true;

		if (type.equals("User")){		
				if(User.getInfo(name, apiKey) == null){
					exist = false;
					return exist;							
				}
		}	
		else if (type.equals("Artist")){
				if(Artist.getInfo(name, apiKey) == null){
					exist = false;
					return exist;
				}
		}	
		else if (type.equals("Genre")){
				if(Tag.getInfo(name, apiKey) == null){
				exist = false;
				return exist;
			}
		}
		return exist;
	}
	/**
	 * entfernt alle Radiostationen visuell
	 */
	public void removeAllRadioStation(){
		for(Component b : gui.radioStationWindow.getContentPane().getComponents()){
			if(b.getName()!= null)
			{
				if(b.getName().startsWith("delete_rs_"))
				{
					gui.radioStationWindow.getContentPane().remove(b);
				}
			}
		}
		gui.typBox.setLocation(9, 60);
        gui.insertRs.setLocation(9, 95);
        gui.sendRs.setLocation(175, 95);
        gui.radioStationWindow.repaint();
	}
	
	
}

