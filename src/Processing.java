import java.util.ArrayList;
import java.util.Collection;

import processing.core.PApplet;
import processing.core.PImage;

import de.umass.lastfm.Artist;
import de.umass.lastfm.Image;
import de.umass.lastfm.ImageSize;
import de.umass.lastfm.Track;

/** 
 * Klasse zur Darstellung des Artistbildes, dass in der Mitte unserer Oberfläche angezeigt wird
 * @author Tim Bohnenstingl, Martin Tonhauser <br><br> Kommentierung: Tim Bohnenstingl
 * @version 1.0 
**/
public class Processing extends PApplet {																	
	
/** das erste Bild wird gesondert behandelt */	
boolean first = true; 																						 
/** von Processing zur Verfügung gestellte Variable zur Speicherung eines Bildes */
public PImage img;
/** zweite PImage-Variable, vorlaeufig leer */
public PImage PImage = null;
/** Zur Speicherung mehrerer Bilder wird ein Array angelegt */
public ArrayList<PImage> PImageList = new ArrayList<PImage>();
/** zur Speicherung der von lastfm zur Verfügung gestellten URL des Artistbildes */
public String urlat = null;
/** Stoppt,wenn draw "false" **/
public boolean stop = true;																					
/** Ersatzbild, falls kein Bild geladen werden kann **/
public String image = "http://userserve-ak.last.fm/serve/_/58769217/MF+DOOM+DOOMvsMASS1.jpg";				


/** 
 * Konstruktor
 * @param image Artistbild
**/
public Processing (String image) {
	this.image = image;
}


/** 
 * Laed benoetigte Bilddateien von lastm und speichert diese in einer Processingvariable (PImage)
 * 
 * @param tracks Collection, die die uebergebenen Tracks der Radiostationen enthaelt
 * @param apiKey wird benoetigt, um auf Informationen von lastfm zu zugreifen
**/
public Processing (Collection<Track> tracks, String apiKey) {
	
	int i = 0; 																								// Zaehler für den ersten Track
	
	for (Track track : tracks) {
		if(Artist.getImages(track.getArtist(), apiKey).getPageResults()!=null){								// Checkt, ob Bild vorhanden
		i++;
		if(i==1){

			int zaehler = 0;
			for(Image img : Artist.getImages(track.getArtist(), apiKey).getPageResults()){
				
				if(zaehler==1){																				// Holt EIN Bild von lastfm (theoretisch erweiterbar)
				return;
				}
				
				urlat = img.getImageURL(ImageSize.ORIGINAL);												// Laed das Bild ueber eine URL von LastFm
				
		        try {
		        	PImage = loadImage(urlat);																// Speichert das Bild (ueber URL) in das Bild-Objekt
		        	 } catch (Exception e) {
					System.out.println("Hier stimmt was nicht!");
				}
		        try {
		    	   PImage.resize(380, 0);																	// Aendert die Groesse passend zur Labelgroesse
		       } catch (Exception e) {
		    	   System.out.println("Fehlermeldung: Fehler beim laden des Bildes seitens LastFm"+e);
		     }
		        PImageList.add(PImage);																		// Wenn alles ok: Bild wird zu den gespeicherten Bildern hinzugefügt
		        zaehler++;
			}
		}	
		}else{
			System.out.println("Fehlermeldung: Fehler beim laden des Bildes seitens LastFm");
			return;
		}
	}	
}


/**
 * Wird einmal ausgeführt, legt Einstellungen fest, die später von draw() uebernommen werden
 */
public void setup() {																						
																											
	if(PImage != null){
		img = PImage;
	}else {
		img = loadImage(image);																				// Wenn kein Bild geladen werden konnte, wird Ersatzbild geladen
	}
	
  size(img.width, img.height);																				// Groesse des Bildes wird festgelegt
  frameRate(30);																							// Gibt an, wie oft das Bild aktualisiert wird
  smooth();																									// Verbessert optisch die Qualität des Bildes
}
 

/**
 * Wird fuer jedes neue Bild ausgeführt, um dieses zu "zeichnen"
 * Theoretisch um math. Formeln erweiterbar, um Effekte ueber die Bilder laufen zu lassen
 */
public void draw() {																						

  if(stop){
  if (first) {
	
    image(img, 0, 0);
    first = false;
    
  }
  }else{
	  noLoop();
  }
 }
}