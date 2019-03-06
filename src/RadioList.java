import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Vector;
import javax.swing.JOptionPane;


/**
 *  Vector fuer Radioliste mit Name, Anzahl und Speicherort
 *  @author Michael Weidner, Tim Bohnenstingl <br><br> Kommentierung: Tim Bohnenstingl, Martin Tonhauser
 *  @version 1.0
 */
public class RadioList 
{
	/** Erstellen einer Textdatei */
	private File file;																			
	/** Zweckmaessige Variable um Texte zu speichern */
	private FileWriter writer;																	
	/** Name der Radioliste */
	public String listName = "";																
	/** Vector für aktuelle Radiostationen */
	public Vector<RadioStation> RadioStationContent = new Vector<RadioStation>();				
	/** Vector für verfuegbare Radiolisten */
	public Vector<RadioList> RadioListContent = new Vector<RadioList>();						
	
	
/** 
 * Konstruktor 
 * @param name Name der Radioliste
**/
	public RadioList(String name) 
	{	
		this.listName = name;
	}
	
	
/** 
 * Radiolisten aus angelegter Textdatei laden
 */
public void loadRadioList () 
	{	
		RadioListContent.removeAllElements(); 																		// leert Liste der Radiolisten									
		try 
		{	
			BufferedReader br = new BufferedReader(new FileReader("NeueRadioListe.txt"));   						// liest Radiolisten-Textdatei aus
		    String zeile;
		    try 
		    {	
		    	while ((zeile = br.readLine()) != null) 															// liest Textdatei zeilenweise, falls Zeile nicht leer ist			
		    	{		      
		        	String[] parts = zeile.split(";");	
		        	addRadioList(parts[0].substring(0, parts[0].length()));        	
		    	}
		    }  
		    catch (IOException e) 
		    {
		    	e.printStackTrace();
			}
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}		
	}
	

/**
 * Radiostationen aus Textdatei laden
 * @param listenname Name der RadioListe
 */
public void loadRadioStation(String listenname)
	{	
		RadioStationContent.clear();																				// loeschen der aktuellen Liste 												
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader("NeueRadioListe.txt"));   
		    String zeile;
		    try 
		    {
		    	while ((zeile = br.readLine()) != null) 	
		    	{	
		    		if (zeile.startsWith(listenname))																// sucht sich zugehörige Zeile für die Radioliste										
		        	{	
		        		String[] parts = zeile.split(";");															// gibt an, wie die Informationen aus der Datei getrennt werden
		        		for ( int j=0; j < parts.length; j++ ) 
		        		{
		        			if (parts[j].startsWith("Genre"))
		        			{
		        				addRadioStation("Genre", parts[j].substring(5, parts[j].length()), true);		        			
		        			}
		        			if (parts[j].startsWith("User"))
		        			{
		        				addRadioStation("User", parts[j].substring(4, parts[j].length()), true);	
		        			}
		        			if (parts[j].startsWith("Artist"))
		        			{
		        				addRadioStation("Artist", parts[j].substring(6, parts[j].length()), true);	
		        			}
		        		}	
		        	}
		    	}	
		    }
		    catch (IOException e) 
		    {
		        e.printStackTrace();
		    }
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
	}
	

/**
 *  Speichern einer Radioliste in Textdatei
 *  @param ListName Name unter dem die Radioliste gespeichert wird
 */
public void saveRadioList (String ListName) 												
	{	
		if (ListName.isEmpty())
		{
			ListName = "newList";
		}
		if(!existRadioListName(ListName)){
			file = new File("NeueRadioListe.txt");				
			try 
		    {		
			writer = new FileWriter(file ,true);  
	    	writer.write(ListName);
	    	writer.write(System.getProperty("line.separator"));
	    	writer.flush();
	    	writer.close();
	    } 
	    catch (IOException e) 
	    {
	    	e.printStackTrace();
	    }
		}		
	}
	

/**
 * Speichern einer Radiostation in Textdatei
 * @param StationName Name der Radiostation
 * @param StationTyp Typ (Genre/User) der Radiostation
 * @param ListName Name der Liste, in der die Radiostation gespeichert wird
 */
public void saveRadioStation (String StationName, String StationTyp, String ListName) 
	{	
		Vector<String> inhalt = new Vector<String>();
		String zeile;
		String neueZeile = "";
		
		if (StationName.isEmpty())																					// kontrolliert, ob ueberhaupt etwas eingegeben worden ist 																				 
		{
			System.out.println("FEHLERMELDUNG: Bitte Bezeichnung eingeben");
			JOptionPane.showMessageDialog(null, "Bitte Bezeichnung eingeben", "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
			return;
		}
		for ( int j=0; j < RadioStationContent.size(); j++ )
		{
			if (RadioStationContent.elementAt(j).name.equals(StationName))											// 	Kontrolliert, ob Station schon vorhanden ist		
			{
				if (RadioStationContent.elementAt(j).typ.equals(StationTyp))
				{
					System.out.println("FEHLERMELDUNG: Bezeichnung bereits in der Playlist");
					JOptionPane.showMessageDialog(null, "Bezeichnung bereits in der Playlist", "Fehlermeldung", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}			
		}
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader("NeueRadioListe.txt"));   	    	    
		    try 
		    {
				while ((zeile = br.readLine()) != null) 
		    	{
		        	if(zeile.startsWith(ListName))
		        	{   
		        		neueZeile = zeile + ";" + StationTyp + StationName;											// legt neue Zeile an und trennt Informationen durch ";"			 
		        		inhalt.add(neueZeile);																		// fuegt eben erstellte Zeile in die Datei ein											 
		        	}	        	
		        	else
		    		inhalt.add(zeile);  	
		    	}
		    }  
		    catch (IOException e) 
		    {
		    	e.printStackTrace();
			}
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}	
		try 
	    {	 
			file = new File("NeueRadioListe.txt");																	// erstellt neue Textdatei 									
	    	writer = new FileWriter(file ,false);  
	    	for ( int j=0;   j < inhalt.size(); j++ )																// fuellt eine Datei											
	    	{
	    		writer.write(inhalt.elementAt(j));
	    		writer.write(System.getProperty("line.separator"));
	    	}
	    	writer.flush();
	    	writer.close();
	    } 	
	    catch (IOException e) 
	    { 
	    	e.printStackTrace();
	    }
	   
	}


/** 
 * Radioliste mit Inhalt aus Textdatei entfernen
 * @param Liste Liste die geloescht werden soll
 */
public void deleteRadioList(String Liste) 
	{	
		Vector<String> inhalt = new Vector<String>();
		String zeile;
		file = new File("NeueRadioListe.txt");		
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader("NeueRadioListe.txt"));   	    	    
		    try 
		    {	
				while ((zeile = br.readLine()) != null)																// gibt an, dass geloescht werden soll, bis eine leere Zeile folgt											
		    	{	
		        	if(zeile.startsWith(Liste)){}																	// beim Listenname, beginnt die Methode zu loeschen												
		        	else
		    		inhalt.add(zeile);		        	
		    	}
		    }  
		    catch (IOException e) 
		    {
		    	e.printStackTrace();
			}
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}						
		try 
	    {	 
	    	writer = new FileWriter(file ,false);     
	    	for ( int j=0; j < inhalt.size(); j++ )
	    	{
	    		writer.write(inhalt.elementAt(j));
	    		writer.write(System.getProperty("line.separator"));
	    	}	    	
	    	writer.flush();
	    	writer.close();
	    }  
	    catch (IOException e) 
	    {
	    	e.printStackTrace();
	    }
	}
	

/**
 * einzelne Radiostationen aus Textdatei entfernen
 * @param Liste aus welcher Liste wird geloescht
 * @param Station welche Station wird geloescht
 */
public void deleteRadioStation(String Liste, String Station)
	{
		Vector<String> inhalt = new Vector<String>();
		String zeile;
		String neueZeile = "";
		file = new File("NeueRadioListe.txt");		
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader("NeueRadioListe.txt"));   	    	    
		    try 
		    {
				while ((zeile = br.readLine()) != null) 
		    	{	
		        	if(zeile.startsWith(Liste))																//sucht nach der gewuenschten Liste, in der die Station gespeichert ist												
		        	{
		        		String[] parts = zeile.split(";");
		        		for ( int j=0; j < parts.length; j++ )
		        		{	
		        			if(parts[j].contains(Station))													// sucht nach der zu loeschenden Station											
		        			{ }		        			
		        			else 				
		        				neueZeile = neueZeile + parts[j].toString()+";";
		        		}
		        		inhalt.add(neueZeile);
		        	}	        	
		        	else
		    		inhalt.add(zeile);    	
		    	}
		    }  
		    catch (IOException e) 
		    {
		    	e.printStackTrace();
			}
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}			
		try 
	    {	 
	    	writer = new FileWriter(file ,false);     
	    	for ( int j=0; j < inhalt.size(); j++ )
	    	{
	    		writer.write(inhalt.elementAt(j));
	    		writer.write(System.getProperty("line.separator"));
	    	}
	    	writer.flush();
	    	writer.close();
	    }  
	    catch (IOException e) 
	    {
	    	e.printStackTrace();
	    }
	}


/** 
 * Checkt, ob Radiolistenname schon existiert
 * @param name Name der gesuchten Liste
 */
public boolean existRadioListName(String name){
		for ( int j=0; j < RadioListContent.size(); j++ )
		{
			
			if (RadioListContent.elementAt(j).listName.equals(name))
			return true; 
		}
		return false;
	}
	

/** 
 * Neue Radiostation hinzufügen 
 * @param genre Genre der Radiostation
 * @param typ Typ der Radiostation
 * @param active Radiostation inaktiv/aktiv
 */
	public void addRadioStation (String genre, String typ,Boolean active) 
	{
		RadioStation rs = new RadioStation(genre, typ, active);
		RadioStationContent.addElement(rs);
	}
	
	
/**
 *  Neue Radioliste hinzufügen
 *  @param listenname Name der neuen Liste
 */
public void addRadioList (String listenname) 
	{
		RadioList rl = new RadioList(listenname);
		RadioListContent.addElement(rl);
	}
	

/**
 *  Einzelne Radiostation aus Vector entfernen
 *  @param index Position der Radiostation
 */
public void removeRadioStation (int index) 
	{
		RadioStationContent.removeElementAt(index);
	}
	

/**
 *  Einzelne Radioliste aus Vector entfernen
 *  @param index Position der Radioliste
 */
public void removeRadioList (int index) 
	{
		RadioListContent.removeElementAt(index);
	}
	

/**
 *  Status einer Radioliste ändern
 */
public void setRadioStatus () 
	{
	}
	

/** 
 * Vector der mit aktuell geladenen Radiostationen leeren
 */
public void ClearRadioList()
	{
		RadioStationContent.clear();
	}
}