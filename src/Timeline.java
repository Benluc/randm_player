
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

 

 
	/**Implemetierung der Zeitleiste
	 * @author Tim Bohnenstingl, Friederike Danger, Marta Czerwinski
	 * @version 1.0 <br><br> Kommentierung: Martin Tonhauser
	 */	

	public class Timeline implements ActionListener, Runnable {
	/** gibt an wie Lange ein Lied schon läuft **/
	public static int anzeigeZaehler;
	/** enthält die aktuelle GUI **/
	public GUI gui;
	/** entält die gesamte Trackdauer **/
	public int duration;
	/** Stoppuhr */
	public Timer timer;
	
	/**
	 * Timeline Konstruktor
	 * 
	 * @param duration enthält die aktuelle Trackdauer
	 * @param gui enthält das aktuelle gui-Objekt
	 */
	public Timeline(int duration, GUI gui){
		
		this.gui = gui;
		this.duration = duration;
		
		new Thread( this , "DurationTimer").start();
	
	}
 
	/**
	 * Anzeige und Skaliereung der Zeitachse auf den jeweiligen Titel
	 * @param e enthält die ActionEvent-Informationen
	 */	
		public void actionPerformed(ActionEvent e) {
													
		Timeline.anzeigeZaehler++;
			gui.Anzeige.setValue(100*anzeigeZaehler/duration);
			gui.DAUER.setText("Duration --> " + anzeigeZaehler);
	  }


	/** 
	 * Start der Anzeige und Verhalten während des Titels, GUI 
	 */	
		public void run() {
	
		
			gui.mainWindow.getContentPane().repaint();
			timer = new Timer(1000, this);
			anzeigeZaehler=0;
			timer.start();
	   
	    try {
	      Thread.sleep(duration*1000);
	         } 
	    catch (InterruptedException e)
	    {
	    }
	    
	    timer.stop();

	}
	/** der 
	 * Timer wird gestoppt 
	 */
		 public void stopTimer(){
	
		timer.stop();
	}

}
