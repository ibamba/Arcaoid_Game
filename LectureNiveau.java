import java.io.*;
import java.util.*;
import java.lang.Exception.*;
import javafx.scene.paint.Color;

public class LectureNiveau{

    /**
       classe de lecture de niveau
       Lit les valeurs de la longueur, de la largeur et les coordonnées de briques du terrain
       puis les passe au modele.
    */
    
    private static Scanner fR; //Pour la lecture

    public static void ouvrir(String nomDuFichier)
	throws FileNotFoundException{
	/**
	   Ouvre un fichier en mode ecriture.
	   @param le chemin du fichier
	*/
	fR = new Scanner(new File(nomDuFichier));
    }
    
    public static TerrainValues chargerNiveau()
	throws Exception{
	/**
	   Lit les dimensions dans le fichier texte ouvert
	   @return La longueur et la largeur du terrain et la liste des
	           briques stockés dans un objet spécial
	*/
	
	LinkedList<Brique> briques = new LinkedList<>();
	double longueur, largeur;
	
	if(fR.hasNextLine()){
	    String dimensions = fR.nextLine();
	    String dim[] = dimensions.split("x");
	    try{
		largeur = Double.parseDouble(dim[0]);
	    } catch(NumberFormatException e){
		throw new NumberFormatException();
	    }
	    try{
		longueur = Double.parseDouble(dim[1]);
	    } catch(NumberFormatException e){
		throw new NumberFormatException();
	    }
	    
	    while(fR.hasNextLine()){

		double x1,y1,x2,y2;
		String brique = fR.nextLine();
		String valeurs[] = brique.split("&");
		if(valeurs.length != 3)
		    throw new Exception("Mauvais format de fichier");
		String nw[]  = valeurs[0].split(",");
		if(nw.length != 2)
		    throw new Exception("Mauvais format de fichier");
		String xNW = nw[0].substring(1);
		String yNW = nw[1].substring(1, nw[1].length()-2);
		try{
		    x1 = Double.parseDouble(xNW);
		} catch(NumberFormatException e){
		    throw new NumberFormatException();
		}
		try{
		    y1 = Double.parseDouble(yNW);
		} catch(NumberFormatException e){
		    throw new NumberFormatException();
		}
		String se[]  = valeurs[1].split(",");
		if(se.length != 2)
		    throw new Exception("Mauvais format de fichier");
		String xSE = se[0].substring(2);
		String ySE = se[1].substring(1, se[1].length()-2);
		try{
		    x2 = Double.parseDouble(xSE);
		} catch(NumberFormatException e){
		    throw new NumberFormatException();
		}
		try{
		    y2 = Double.parseDouble(ySE);
		} catch(NumberFormatException e){
		    throw new NumberFormatException();
		}
		if(x2<x1 || y2<y1 || x1<0 || x2<0 || x1>longueur || x2>longueur || y1>largeur || y2>largeur || y1<0 || y2<0)
		    throw new IllegalArgumentException("Coordonnées de brique invalides");
		
		Color c;
		try{
		    c = Color.valueOf(valeurs[2].substring(1));
		} catch(IllegalArgumentException e){
		    throw new IllegalArgumentException("Couleur invalide");
		}
		briques.add(new Brique(x1, y1, x2, y2, c, 1));
	    }
	    return new TerrainValues(longueur, largeur, briques);
	}
	else{
	    throw new Exception("Fichier vide !!!");
	}
    }
    
    public static void fermer() throws IOException{
	fR.close();
    }
    
}
