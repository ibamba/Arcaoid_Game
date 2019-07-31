import javafx.application.*;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Brique extends Rectangle{
    private final double xNW, yNW;
    private final double xSE, ySE;
    private final int points;
    
    public Brique(double x1, double y1, double x2, double y2, Color c, int p){
	/**
	   Constructeur des briques. 
	   @param coordonnees des coin nord ouest et sud est du rectangle
	   @param c : la couleur de fond du rectangle
	   @param p : le nombre de points que rapporte ce rectangle lorsqu'il est cassé par la balle
	*/
	super(x1, y1, x2-x1, y2-y1);
	xNW = x1;
	yNW = y1;
	xSE = x2;
	ySE = y2;
	setFill(c);
	points = p;
    }

    public double getPosXNW(){ return xNW;}

    public double getPosYNW(){ return yNW;}

    public double getPosXSW(){ return xNW;}

    public double getPosYSW(){ return ySE;}

    public double getPosXNE(){ return xSE;}
    
    public double getPosYNE(){ return yNW;}

    public double getPosXSE(){ return xSE;}

    public double getPosYSE(){ return ySE;}

    public int getPoints(){ return points;}
    
}
