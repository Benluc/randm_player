import java.util.Vector;

/**
 * 
 * Klasse um Informationen über jeweilige RadioStation zu speichern
 * @author Marta Czerwinski, Michael Weidner, Friederike Danger, Martin Tonhauser, Martin Dobbermann, Tim Bohnenstingl <br><br> Kommentierung: Martin Tonhauser
 * @version 1.0
 */
public class RadioStation 											
{	
	/** Variable um zwischen Genre oder User zu wählen */
	protected String typ = "";
	/** Name der Radiostation */
	protected String name = "";	
	/** Station aktiv/inaktiv setzen */																				
	protected Boolean status = null; 								
	
/**
 * Konstruktor
 * @param typ Genre oder User
 * @param name Name der Radiostation
 * @param active Station ist aktiv/inaktiv
 */
public RadioStation(String typ, String name, Boolean active) 		
	{
		this.typ = typ;
		this.name = name;
		this.status = active;
	}
}