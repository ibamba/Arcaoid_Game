import javafx.application.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

public class Ball extends Circle{
    private double xPos, yPos;
    private double xSpeed, ySpeed;

    public Ball(double xNWRaquette, double yNWRaquette, double xSERaquette, double vxRaquette, double vyRaquette){
	/**
	   Constructeur de la balle. 
	   En debut de jeu, la balle est placée au milieu de la raquette et sa vitesse est
	   la moitie de celle de la raquette
	   @param les coordonnees du coin nord ouest de la raquette sur le terrain
	   @param la coordonnee x sud est de la raquette
	   @param les coordonnees de la vitesse de la raquette
	*/
	super(10);
	xPos = (xNWRaquette+xSERaquette)/2;
	yPos = yNWRaquette-10;
	setCenterX(xPos);
	setCenterY(yPos);
	xSpeed = vxRaquette/2;
	ySpeed = vyRaquette/2;
	setFill(Color.BLACK);
    }

    public double getXSpeed(){return xSpeed;}
    public double getYSpeed(){return ySpeed;}

    public void setXSpeed(double x){xSpeed = x;}
    public void setYSpeed(double y){ySpeed = y;}
}
