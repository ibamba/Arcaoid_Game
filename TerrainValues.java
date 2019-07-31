import java.util.*;

/**
   classe permettant de stocker les informations d'un terrain lus dans un fichier texte :
   la longueur, la largeur, et les coordonnees des briques
*/

class TerrainValues{
    private double longueur;
    private double largeur;
    private LinkedList<Brique> briques;

    public TerrainValues(double lo, double la, LinkedList<Brique> b){
	longueur = lo;
	largeur = la;
	briques = b;
    }

    public double getLongueur(){ return longueur;}
    public double getLargeur(){ return largeur;}
    public LinkedList<Brique> getBriques(){ return briques;}

}
