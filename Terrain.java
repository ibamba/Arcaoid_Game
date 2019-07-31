import java.util.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class Terrain extends Pane {
    private final double longueur, largeur;
    private Ball ball;
    private Raquette raquette;
    private LinkedList<Brique> listBriques;

    public Terrain(){
	/**
	   Constructeur par defaut du terrain :
	   ce constructeur est utilise en cas d'échec dans la lecture de fichier
	*/
	longueur = 500;
	largeur = 600;
	setMaxWidth(longueur);
	setMaxHeight(largeur);
	setPrefSize(longueur, largeur);
	raquette = new Raquette(longueur, largeur, 400E-9, 240E-9);
	getChildren().addAll(raquette);
	setStyle("-fx-background-color: white;"+
		 "-fx-border-style: solid inside;"+
		 "-fx-border-color: grey;"+
		 "-fx-border-width: 2;");
    }	
    
    public Terrain(double lo, double la, LinkedList<Brique> b){
	/**
	   Constructeur du terrain
	   @param lo : longueur du terrain
	   @param la : largeur du terrain
	   @param b : liste des briques du terrain
	*/
	longueur = lo;
	largeur = la;
	setMaxWidth(longueur);
	setMaxHeight(largeur);
	setPrefSize(longueur, largeur);
	raquette = new Raquette(longueur, largeur, 400E-9, 240E-9);
	ball = new Ball(raquette.getPosXNW(), raquette.getPosYNW(), raquette.getPosXSE(), raquette.getXSpeed(), raquette.getYSpeed());
	getChildren().addAll(ball, raquette);
	listBriques = b;
	for(int i=0; i<listBriques.size(); i++)
	    getChildren().add(listBriques.get(i));
	setStyle("-fx-background-color: white;"+
		 "-fx-border-style: solid inside;"+
		 "-fx-border-color: grey;"+
		 "-fx-border-width: 2;");
    }

    public Raquette getRaquette(){ return raquette;}

    public Ball getBall(){ return ball;}
    
    public double getLongueur(){ return longueur;}

    public double getLargeur(){ return largeur;}
    
    public LinkedList<Brique> getListBriques(){ return listBriques;}
    
}
