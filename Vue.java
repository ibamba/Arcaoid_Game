import java.util.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.input.*;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import javafx.beans.value.*;
import javafx.scene.shape.Rectangle;

public class Vue extends Application{
    /**
       La vue, classe principale du projet :
       @arg canvas : Le style de la fenetre principale est un BorderPane
       @arg terrain : le terrain
       @arg controler : le controler
       @arg score : le score du joueur
       @arg textScore : le texte qui affiche le score dans la scène
       @arg notification : zone bas de la fenetre qui affiche les messages adressés a l'utilisateur
       @arg canPlay : un boolean qui confirme si la lecture de fichier s'est bien déroulé
       @arg niveau : le niveau actuel du joueur
    */
    private BorderPane canvas;
    private Terrain terrain;
    private Controler controler;
    private int score;
    private Text textScore;
    private Label notification;
    private boolean canPlay;
    private int niveau;
    
    public Vue(){
	controler = new Controler(this);
	canvas = new BorderPane();
	niveau = 1;
	score = 0;
	controler.toChargeDebut();
    }

    public BorderPane getCanvas(){ return canvas;}
    
    public Terrain getTerrain(){ return terrain;}
    
    public void setTerrain(Terrain newTerrain){
	terrain = newTerrain;
	canvas.setAlignment(terrain, Pos.TOP_LEFT);
	canvas.setCenter(terrain);
    }

    public void setScore(int s){
	score = s;
	textScore.setText("Score = "+s);
    }

    public int getScore(){ return score;}

    public void setMessage(String s){
	notification.setText(s);
    }
    
    public void setCanPlay(boolean b){
	canPlay = b;
    }

    public int getNiveau(){ return niveau;}

    public void setNiveau(int niv){ niveau=niv;}
    
    public static void main(String arg[]) {
	launch();
    }
    
    @Override
    public void start(Stage stage) {
	
	MenuItem  quitte = new MenuItem("Quitter");
	quitte.setOnAction(e -> System.exit (0));
	MenuItem pause = new MenuItem("Pause");
	pause.setOnAction(e -> controler.toPausePlay());
	MenuItem play = new MenuItem("Play");
	play.setOnAction(e -> controler.toPausePlay());
	MenuItem redemarre = new MenuItem("Redemarrer");
	redemarre.setOnAction(e ->  changeNiveau(niveau));
	Menu jeu = new Menu("Jeu");
	jeu.getItems().addAll(pause, play, redemarre, quitte);
	MenuBar menu = new MenuBar();
	menu.getMenus().add(jeu);

	Button [] listButton = new Button[7];
	listButton[0] = new Button("Niveau "+1);
	listButton[0].setOnAction(e ->  changeNiveau(1));
	listButton[1] = new Button("Niveau "+2);
	listButton[1].setOnAction(e ->  changeNiveau(2));
	listButton[2] = new Button("Niveau "+3);
	listButton[2].setOnAction(e ->  changeNiveau(3));
	listButton[3] = new Button("Niveau "+4);
	listButton[3].setOnAction(e ->  changeNiveau(4));
	listButton[4] = new Button("Niveau "+5);
	listButton[4].setOnAction(e ->  changeNiveau(5));
	listButton[5] = new Button("Niveau "+6);
	listButton[5].setOnAction(e ->  changeNiveau(6));
	listButton[6] = new Button("Niveau "+7);
	listButton[6].setOnAction(e ->  changeNiveau(7));
	
	VBox vbox = new VBox();
	vbox.setPadding(new Insets(0));
	vbox.setSpacing(5);
	Text title = new Text("Niveaux");
	title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
	vbox.getChildren().add(title);
	for(int i=0; i<listButton.length; i++){
	    vbox.setMargin(listButton[i], new Insets(0, 0, 0, 8));
	    vbox.getChildren().add(listButton[i]);
	}

	textScore = new Text("Score = "+score);
	textScore.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 12));
	StackPane scoreBoard = new StackPane();
	scoreBoard.setMaxWidth(90);
	scoreBoard.setMaxHeight(30);
	scoreBoard.setPrefSize(90, 30);
	scoreBoard.setStyle("-fx-background-color: white;"+
		       "-fx-border-style: solid inside;"+
		       "-fx-border-color: red;"+
		       "-fx-border-width: 2;");
	
	scoreBoard.getChildren().add(textScore);	    

	notification = new Label("");

	canvas.setTop(menu);
	canvas.setLeft(vbox);
	canvas.setBottom(notification);
	canvas.setAlignment(terrain, Pos.TOP_LEFT);
	canvas.setCenter(terrain);
	canvas.setRight(scoreBoard);
	canvas.setAlignment(scoreBoard, Pos.TOP_RIGHT);
	Scene scene = new Scene(canvas, 700, 800);
	stage.setScene(scene);
	stage.setTitle("Arcanoid");
	stage.show();
	scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
		@Override
		public void handle(KeyEvent event) {
		    KeyCode code;
		    if((code = event.getCode()) == KeyCode.LEFT || code == KeyCode.RIGHT)
			controler.actionRaquette(code);
		    else{
			switch (event.getCode()) {
			case ESCAPE : System.exit(0);
			    break;
			case P : controler.toPausePlay();
			    break;
			case S : controler.toDemarre();
			    break;
			case R : changeNiveau(niveau);
			    break;
			}
		    }
		}
	    });
	if(canPlay){
	    setMessage("Debut du jeu : niveau 1\nAppuyer sur S pour démarrer\n");
	    controler.joue();
	}
	else
	    setMessage("Erreur lors du chargement\nVeuillez relancer\n");
    }

    public void changeNiveau(int n){
	/**
	   Change le niveau du jeu
	   @param le niveau à découvert
	*/
	controler.toPause();
	Alert conf = new Alert(AlertType.CONFIRMATION);
	conf.setTitle("CONFIRMATION");
	conf.setHeaderText(null);
	conf.setContentText("Voulez-vous vraiment changer de niveau ?");
	ButtonType oui = new ButtonType("Oui");
	ButtonType non = new ButtonType("Non");
	conf.getButtonTypes().setAll(oui, non);
	conf.resultProperty().addListener((ObservableValue<? extends ButtonType> observableValue, ButtonType oldValue, ButtonType newValue) -> { 
		if (newValue == oui) {  
		    controler.toChargeNiv(n);		    
		}
		else{
		    controler.toDemarre();
		}
	    }); 
	conf.show();
    }
    
    public void gameOver(){
	/**
	   Averti le joueur de sa défaite et lui propose de recommencer
	*/
	Alert conf = new Alert(AlertType.CONFIRMATION);
	conf.setTitle("GAME OVER");
	conf.setHeaderText(null);
	conf.setContentText("Vous avez perdu. SCORE : "+score+
			     "\nVoulez-vous recommencer ?");
	ButtonType oui = new ButtonType("Oui");
	ButtonType non = new ButtonType("Non");
	conf.getButtonTypes().setAll(oui, non);
	conf.resultProperty().addListener((ObservableValue<? extends ButtonType> observableValue, ButtonType oldValue, ButtonType newValue) -> { 
		if (newValue == oui) {  
		    controler.toChargeNiv(niveau);		    
		}
		else{
		    Alert info = new Alert(AlertType.INFORMATION);
		    info.setTitle("INFORMATION");
		    info.setHeaderText(null);
		    info.setContentText("Fin de jeu.\nAurevoir !!!");
		    info.showAndWait();
		    System.exit(0);
		}
	    }); 
	conf.show();
    }

    public void victoire(){
	/**
	   Averti le joueur de sa victoire et lui propose de passer au niveau supérieur
	   ou de recommencer ou de quitter
	*/
	Alert conf = new Alert(AlertType.CONFIRMATION);
	conf.setTitle("VICTOIRE");
	conf.setHeaderText(null);
	conf.setContentText("Vous avez gagné. SCORE : "+score+
			     "\nVoulez-vous passez au niveau suivant ?");
	ButtonType suiv = new ButtonType("Suivant");
	ButtonType recom = new ButtonType("Recommencer");
	ButtonType back = new ButtonType("Quitter");
	conf.getButtonTypes().setAll(suiv, recom, back);
	conf.resultProperty().addListener((ObservableValue<? extends ButtonType> observableValue, ButtonType oldValue, ButtonType newValue) -> { 
		if (newValue == suiv) {
		    controler.toChargeNiv(niveau++);		    
		}
		else if(newValue == recom) {
		    controler.toChargeNiv(niveau);
		}else{
		    System.exit(0);
		}
	    }); 
	conf.show();
    }

}
