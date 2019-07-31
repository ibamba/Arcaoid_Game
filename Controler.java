import javafx.scene.input.*;

class Controler{
    Modele modele;
    Vue vue;

    /**
       Le controler
       @arg modele : le modele actuel du jeu
       @arg vue : la vue du jeu
    */
    public Controler(Vue v){
	vue = v;
	modele = new Modele(v);
    }

    public void toChargeDebut(){
	/**
	   Demande au modele le chargement du debut de jeu
	*/
	modele.chargeDebut();
    }
    
    public void toChargeNiv(int niveau){
	/**
	   demande au modele de charger un niveau spécifique
	*/
	modele.chargeNiv(niveau);
    }
    
    public void joue(){
	/**
	   Lance le modele donc le jeu
	*/
	modele.run();
    }

    public void actionRaquette(KeyCode c){
	/**
	   Demande au modele de deplacer la raquette
	*/
	modele.deplaceRaquette(c);
    }

    public void toPausePlay(){
	/**
	   Met le jeu en play si pause y a ou en pause sinon
	*/
	if(modele.getStatut()){
	    modele.setStatut(false);
	    vue.setMessage("Jeu en cours...\nNiveau actuel : "+vue.getNiveau()+"\n");
	}
	else{
	    modele.setStatut(true);
	    vue.setMessage("Jeu en pause...\nAppuyer sur P pour reprendre\n");
	}
    }

    public void toPause(){
	/**
	   Met le jeu en pause
	*/
	modele.setStatut(true);
	vue.setMessage("Jeu en pause...\nAppuyer sur P pour reprendre\n");
    }
    
    public void toDemarre(){
	/**
	   Met le jeu en play
	*/
	modele.setStatut(false);
	vue.setMessage("Jeu en cours...\nNiveau actuel : "+vue.getNiveau()+"\n");
    }

}
