import javafx.application.*;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;

public class Raquette extends Rectangle{
    private double xNW, yNW;
    private double xSE, ySE;
    private final double xSpeed, ySpeed;
    
    public Raquette(double longueurTerrain, double largeurTerrain, double sx, double sy){
	/**
	   Constructeur de la raquette. 
	   En debut de jeu, la raquette est placée en bas et au milieu du terrain
	   @param longueurTerrain : la longueur du terrain
	   @param largeurTerrain : la largeur du terrain
	   @param les coordonnees x et y de sa vitesse
	*/
	super((longueurTerrain/2)-25, largeurTerrain-10, 50, 10);
	xNW = (longueurTerrain/2)-25;
	yNW = largeurTerrain-10;
	xSE = 50+xNW;
	ySE = largeurTerrain-10;
	xSpeed = sx;
	ySpeed = sy;
	setFill(Color.BLACK);
    }

    public double getPosXNW(){ return xNW;}

    public double getPosYNW(){ return yNW;}

    public double getPosXSW(){ return xNW;}

    public double getPosYSW(){ return ySE;}

    public double getPosXNE(){ return xSE;}
    
    public double getPosYNE(){ return yNW;}

    public double getPosXSE(){ return xSE;}

    public double getPosYSE(){ return ySE;}

    public void setPosXNW(double x){xNW += x; setX(xNW);}

    public void setPosXSE(double x){xSE += x;}

    public double getXSpeed(){return xSpeed;}

    public double getYSpeed(){return ySpeed;}
    
}
