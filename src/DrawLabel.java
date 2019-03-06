import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

/**
 * Diese Klasse dient zum Laden des GUI-Hintergrundes
 * @author Marta Czerwinski <br><br> Kommentierung: Tim Bohnenstingl
 * @version 1.0
 */
public class DrawLabel extends JLabel{
	
	/** gepuffertes Bild */
	public BufferedImage img;
	
	/**
	 * Konstruktor
	 * 
	 * Legt das Bildformat fest und schreibt die Bildinformation in die Instanzvariable.
	 * 
	 * @param imageSrc enth�lt den Pfad zur Bilddatei
	 * @param width Breite des Bildes
	 * @param height H�he des Bildes
	 */
	public DrawLabel(String imageSrc, int width, int height){
		setLocation(0,0);
		setSize(width, height);
		
		try {
		    img = ImageIO.read(new File(imageSrc));
			} 
		catch (IOException e) {
			new Error("Fehler, konnte Bild unter folgendem Pfad nicht finden: " + imageSrc);
		}
	}
	
	/**
	 * F�r die grafische Darstellung der Komponente zust�ndig
	 * @param g enth�lt Information die f�r des Rendering von Bedeutung sind
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		  g.drawImage(img,0,0,this);
	}
}
