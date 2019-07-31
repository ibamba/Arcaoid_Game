import javafx.animation.*;
import javafx.scene.input.*;
import javafx.scene.shape.*;
import javafx.util.*;
import java.util.*;

public class Modele extends Thread{
    /**
       Modele en cours du jeu
       @arg vue : la vue du jeu
       @arg animation : l'animation Timer
       @arg isPaused : boolean permettant d'arreter les calculs et de les redemarrer en cas de pause ou de play
    */
    private Vue vue;
    private AnimationTimer animation;
    private boolean isPaused;
    public Modele(Vue v){
	vue = v;
	isPaused = false;
    }

    public AnimationTimer getAnim(){ return animation;}

    public boolean getStatut(){ return isPaused;}
    
    public void setStatut(boolean statut){ isPaused = statut;}

    public void chargeDebut(){
	/**
	   Charge le debut du jeu en lançant la lecture de niveau
	*/
	Terrain terrain;
	try{
	    LectureNiveau.ouvrir("Niveaux/niveau1");
	    TerrainValues v = LectureNiveau.chargerNiveau();
	    terrain = new Terrain(v.getLongueur(), v.getLargeur(), v.getBriques());		    
	    isPaused = true;
	    vue.setCanPlay(true);
	} catch(Exception e){
	    terrain = new Terrain();
	    vue.setCanPlay(false);
	    System.out.println(e);
	}
	vue.setTerrain(terrain);
    }   
    
    public void chargeNiv(int niveau){
	/**
	   Charge un niveau spécifique du jeu en lançant la lecture de ce niveau
	   et remplace le terrain de la vue par le nouveau
	   @param le niveau à charger
	*/
	Terrain terrain;
	animation.stop();
	try{
	    LectureNiveau.ouvrir("Niveaux/niveau"+niveau);
	    TerrainValues v = LectureNiveau.chargerNiveau();
	    terrain = new Terrain(v.getLongueur(), v.getLargeur(), v.getBriques());
	    vue.setScore(0);
	    vue.setMessage("Jeu en cours...\nNiveau actuel : "+niveau+"\n");
	    vue.setCanPlay(true);
	    run();
	} catch(Exception e){
	    terrain = new Terrain();
	    vue.setMessage("Erreur lors du chargement\nVeuillez relancer\n");
	    vue.setCanPlay(false);
	    System.out.println(e);
	}
	vue.setNiveau(niveau);
	vue.setTerrain(terrain);
    }
    
    @Override
    public void run(){
	animation = new AnimationTimer() {
		long lastTime = System.nanoTime();
		
		@Override
		public void handle(long time) {
		    if(!isPaused){
			long dt = time - lastTime;
			double dx = vue.getTerrain().getBall().getXSpeed() * dt;
			double dy = vue.getTerrain().getBall().getYSpeed() * dt;
			double posX = vue.getTerrain().getBall().getCenterX();
			double posY = vue.getTerrain().getBall().getCenterY();

			double nPosX = posX + dx, nPosY = posY + dy;

			if(interceptionRaquette()){

			    //Rebonds sur la raquette
			    
			    //cas milieu raquette : angle pi/2
			    if(vue.getTerrain().getBall().getCenterX() >= ((vue.getTerrain().getRaquette().getPosXSE()+vue.getTerrain().getRaquette().getPosXNW())/2) - (vue.getTerrain().getBall().getRadius()/2) &&
			       vue.getTerrain().getBall().getCenterX() <= ((vue.getTerrain().getRaquette().getPosXSE()+vue.getTerrain().getRaquette().getPosXNW())/2) + (vue.getTerrain().getBall().getRadius()/2)){
				dy = -dy;
				vue.getTerrain().getBall().setYSpeed(-vue.getTerrain().getBall().getYSpeed());
				dx = 0;
				vue.getTerrain().getBall().setXSpeed(0);
			    }
			   
			    //cas côté droit raquette
			    else if(vue.getTerrain().getBall().getCenterX() >= (vue.getTerrain().getRaquette().getPosXSE()+vue.getTerrain().getRaquette().getPosXNW())/2){
				dy = -dy;
				vue.getTerrain().getBall().setYSpeed(-vue.getTerrain().getBall().getYSpeed());
				if(vue.getTerrain().getBall().getXSpeed()==0){
				    vue.getTerrain().getBall().setXSpeed(vue.getTerrain().getRaquette().getXSpeed()/2);
				}
				else if(nPosX < vue.getTerrain().getBall().getCenterX()){
				    dx = -dx;
				    vue.getTerrain().getBall().setXSpeed(-vue.getTerrain().getBall().getXSpeed());
				}
			    }

			    //cas côté gauche raquette
			    else{
				dy = -dy;
				vue.getTerrain().getBall().setYSpeed(-vue.getTerrain().getBall().getYSpeed());
				if(vue.getTerrain().getBall().getXSpeed()==0)
				    vue.getTerrain().getBall().setXSpeed(-vue.getTerrain().getRaquette().getXSpeed()/2);
				else if(nPosX > vue.getTerrain().getBall().getCenterX()){
				    dx = -dx;
				    vue.getTerrain().getBall().setXSpeed(-vue.getTerrain().getBall().getXSpeed());
				}
			    }
			}

			//Rebonds sur les murs haut, gauche et droite
			if (nPosX <= vue.getTerrain().getBall().getRadius() || nPosX >= vue.getTerrain().getLongueur() - vue.getTerrain().getBall().getRadius()) {
			    dx = -dx;
			    vue.getTerrain().getBall().setXSpeed(-vue.getTerrain().getBall().getXSpeed());
			}
			
			if (nPosY <= vue.getTerrain().getBall().getRadius()) {
			    dy = -dy;
			    vue.getTerrain().getBall().setYSpeed(-vue.getTerrain().getBall().getYSpeed());
			}

			//collision avec le mur du bas : défaite !
			if (nPosY >= vue.getTerrain().getLargeur() - vue.getTerrain().getBall().getRadius()) {
			    stop();
			    vue.getTerrain().getChildren().remove(0);
			    vue.setMessage("Vous avez perdu !!!\n");
			    vue.gameOver();
			}
			
			//casse de briques en cas de collision
			int casse;
			if((casse = collision()) != -1){
			    LinkedList<Brique> briques = vue.getTerrain().getListBriques();
			    if(vue.getTerrain().getBall().getCenterX()+vue.getTerrain().getBall().getRadius() >= briques.get(casse).getPosXSW() || vue.getTerrain().getBall().getCenterX()-vue.getTerrain().getBall().getRadius() <= briques.get(casse).getPosXSE()){
				dy = -dy;
				vue.getTerrain().getBall().setYSpeed(-vue.getTerrain().getBall().getYSpeed());
			    }
			    else if(vue.getTerrain().getBall().getCenterY()-vue.getTerrain().getBall().getRadius() <= briques.get(casse).getPosYSW() || vue.getTerrain().getBall().getCenterY()+vue.getTerrain().getBall().getRadius() >= briques.get(casse).getPosYNW()){
				dx = -dx;
				vue.getTerrain().getBall().setXSpeed(-vue.getTerrain().getBall().getXSpeed());
			    }
			    casseBrique(casse);
			    if(gagne()){
				stop();
				vue.getTerrain().getChildren().remove(0);
				vue.setNiveau(vue.getNiveau()+1);
				vue.setMessage("Vous avez gagné !!!\n");
				vue.victoire();
			    }
			}
			
			vue.getTerrain().getBall().setCenterX(posX + dx);
			vue.getTerrain().getBall().setCenterY(posY + dy);
			
		    }
		    lastTime = time;
		}
	    };
	animation.start();
    }
    
    public void deplaceRaquette(KeyCode c){
	/**
	   deplace une raquette selon le bouton pressé : LEFT ou RIGHT
	   s'il n'y a pas de pause
	*/
	if(!isPaused){
	    double dx = vue.getTerrain().getRaquette().getXSpeed() / 20E-9;
	    double posXNW = vue.getTerrain().getRaquette().getPosXNW();
	    double posXSE = vue.getTerrain().getRaquette().getPosXSE();
	    
	    switch (c) {
	    case RIGHT:
		if(posXSE+dx <= vue.getTerrain().getLongueur()){
		    vue.getTerrain().getRaquette().setPosXNW(dx);
		    vue.getTerrain().getRaquette().setPosXSE(dx);
		}
		else if(posXSE < vue.getTerrain().getLongueur()){
		    vue.getTerrain().getRaquette().setPosXNW(vue.getTerrain().getLongueur()-posXSE);
		    vue.getTerrain().getRaquette().setPosXSE(vue.getTerrain().getLongueur()-posXSE);
		}
		break;
	    case LEFT:
		if(posXNW-dx >= 0){
		    vue.getTerrain().getRaquette().setPosXNW(-dx);
		    vue.getTerrain().getRaquette().setPosXSE(-dx);
		}
		else if(posXNW > 0){
		    vue.getTerrain().getRaquette().setPosXNW(-posXNW);
		    vue.getTerrain().getRaquette().setPosXSE(-posXNW);
		}
		break;
	    }
	}
    }

    public int collision(){
	/**
	   Vérifie si la balle est en collision avec une des briques
	   @retun l'indice de la brique dans la liste
	          ou -1 s'il n'y a pas de collision
	*/
	LinkedList<Brique> briques = vue.getTerrain().getListBriques();
	for(int i=0; i<briques.size(); i++){
	    if(vue.getTerrain().getBall().getCenterX()+vue.getTerrain().getBall().getRadius() >= briques.get(i).getPosXSW() &&
	       vue.getTerrain().getBall().getCenterX()-vue.getTerrain().getBall().getRadius() <= briques.get(i).getPosXSE() &&
	       vue.getTerrain().getBall().getCenterY()-vue.getTerrain().getBall().getRadius() <= briques.get(i).getPosYSW() &&
	       vue.getTerrain().getBall().getCenterY()+vue.getTerrain().getBall().getRadius() >= briques.get(i).getPosYNW())
		return i;
	}
	return -1;
    }

    public void casseBrique(int i){
	/**
	   Casse une brique après une collision entre celle-ci et la balle :
	   supprime la brique du terrain et de la liste et augmente le score du joueur
	   @param l'indice de la brique dans la liste
	*/
	vue.setScore(vue.getScore()+vue.getTerrain().getListBriques().get(i).getPoints());
	vue.getTerrain().getChildren().remove(2+i);
	vue.getTerrain().getListBriques().remove(i);
    }

    public boolean interceptionRaquette(){
	/**
	   Vérifie si la balle entre en collision avec la raquette
	   @return true si interception il y a
	           false sinon
	*/
	if(vue.getTerrain().getBall().getCenterX()+vue.getTerrain().getBall().getRadius() >= vue.getTerrain().getRaquette().getPosXSW() &&
	   vue.getTerrain().getBall().getCenterX()-vue.getTerrain().getBall().getRadius() <= vue.getTerrain().getRaquette().getPosXSE() &&
	   vue.getTerrain().getBall().getCenterY()-vue.getTerrain().getBall().getRadius() <= vue.getTerrain().getRaquette().getPosYSW() &&
	   vue.getTerrain().getBall().getCenterY()+vue.getTerrain().getBall().getRadius() >= vue.getTerrain().getRaquette().getPosYNW())
	    return true;
	return false;
    }
	
    public boolean gagne(){
	/**
	   Vérifie si le joueur a gagné : a cassé toutes les briques du terran
	   @return true si victoire y a false sinon
	*/
	return (vue.getTerrain().getListBriques().size()==0) ? true : false;
    }
    
}	    
