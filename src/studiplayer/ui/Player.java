package studiplayer.ui;

import java.io.File;
import java.net.URL;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import studiplayer.audio.PlayList;

public class Player extends Application {
	
	public static final String DEFAULT_PLAYLIST = "playList.cert.m3u";
	private static final String PLAYLIST_DIRECTORY = "";
	private static final String INITIAL_PLAY_TIME_LABEL = "00:00";
	private static final String NO_CURRENT_SONG = " - ";
	
	private PlayList playList;
	public boolean useCertPlayList = false;
	
	
	//* UI Elements
	private Button playButton;
	private Button pauseButton;
	private Button stopButton;
	private Button nextButton;
	private Label playListLabel;
	private Label playTimeLabel;
	private Label currentSongLabel;
	private ChoiceBox sortChoiceBox;
	private TextField searchTextField;
	private Button filterButton;
	

	public Player() {

	}
	
	
	public void setUseCertPlayList(boolean value) {
		useCertPlayList = value;
	}
	
	public void setPlayList(String pathname) {
		
	}
	
	
	public void start(Stage stage) {
		stage.setTitle("APA Player");
		BorderPane paneMain = new BorderPane();
		
		playList = askForPlayList(stage); // an dieser Stelle, da ich die stage übergeben muss und die playlist für "playlist_tabelle" gefüllt sein muss

		

		VBox sectionFilterInner = new VBox();
		TitledPane sectionFilter = new TitledPane("Filter", sectionFilterInner);

			FlowPane suchtext = new FlowPane();
			sectionFilterInner.getChildren().add(suchtext);
				Label suchtextLabel = new Label("Suchtext");
				suchtextLabel.setPrefWidth(70);
				suchtext.getChildren().add(suchtextLabel);
				
				searchTextField = new TextField();
				searchTextField.setPrefWidth(200);
				suchtext.getChildren().add(searchTextField);

			FlowPane sortierung = new FlowPane();
			sortierung.setPadding(new Insets(5, 0, 0, 0));
			sectionFilterInner.getChildren().add(sortierung);
				Label sortierungLabel = new Label("Sortierung");
				sortierungLabel.setPrefWidth(70);
				sortierung.getChildren().add(sortierungLabel);
			
				sortChoiceBox = new ChoiceBox();
				sortChoiceBox.setPrefWidth(200);
				sortierung.getChildren().add(sortChoiceBox);
				
				VBox ButtonBox = new VBox();
				ButtonBox.setPadding(new Insets(0, 0, 0, 20));
					Button btnAnzeigen = new Button("Anzeigen");
					ButtonBox.getChildren().add(btnAnzeigen);
					sortierung.getChildren().add(ButtonBox);
				
		
		VBox sectionPlaylist = new VBox();
			SongTable playlist_tabelle = new SongTable(playList);
			VBox.setVgrow(playlist_tabelle, Priority.ALWAYS); // sodass sich die tabellenhöhe dynamisch anpasst 
			sectionPlaylist.getChildren().add(playlist_tabelle);
		
		VBox sectionBottom = new VBox();
				
			GridPane infoGrid = new GridPane();
			sectionBottom.getChildren().add(infoGrid);
			
				Label playListLabel1 = new Label("Playlist: ");
				playListLabel1.setPadding(new Insets(5, 5, 5, 5));
				infoGrid.add(playListLabel1, 0, 0);
				Label playListLabel = new Label("/path/"); // ToDo: muss ich das noch anpassen?
				playListLabel.setPadding(new Insets(5, 5, 5, 5));
				infoGrid.add(playListLabel, 1, 0);
				
				Label currentSongLabel1 = new Label("Aktuell: ");
				currentSongLabel1.setPadding(new Insets(0, 5, 5, 5));
				infoGrid.add(currentSongLabel1, 0, 1);
				Label currentSongLabel = new Label(NO_CURRENT_SONG); // ToDo: muss ich das noch anpassen?
				currentSongLabel.setPadding(new Insets(0, 5, 5, 5));
				infoGrid.add(currentSongLabel, 1, 1);
				
				Label playTimeLabel1 = new Label("Abspielzeit: ");
				playTimeLabel1.setPadding(new Insets(0, 5, 5, 5));
				infoGrid.add(playTimeLabel1, 0, 2);
				Label playTimeLabel= new Label(INITIAL_PLAY_TIME_LABEL); // ToDo: muss ich das noch anpassen?
				playTimeLabel.setPadding(new Insets(0, 5, 5, 5));
				infoGrid.add(playTimeLabel, 1, 2);
				
			
			HBox buttons = new HBox();
			buttons.setPadding(new Insets(5, 5, 5, 5));
			buttons.setAlignment(Pos.CENTER);
			sectionBottom.getChildren().add(buttons);
				Button playButton = createButton("play.jpg");
				buttons.getChildren().add(playButton);
				Button pauseButton = createButton("pause.jpg");
				buttons.getChildren().add(pauseButton);
				Button stopButton = createButton("stop.jpg");
				buttons.getChildren().add(stopButton);
				Button nextButton = createButton("next.jpg");
				buttons.getChildren().add(nextButton);
		
		

		
		paneMain.setTop(sectionFilter);
		paneMain.setCenter(sectionPlaylist);
		paneMain.setBottom(sectionBottom);

		
		Scene scene = new Scene(paneMain, 600, 400);
		stage.setScene(scene);
		stage.show();
	}
	
	public PlayList askForPlayList(Stage stage) {
		if(!useCertPlayList) { // Playlist PopUp
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			File selectedFile = fileChooser.showOpenDialog(stage);
	        if (selectedFile != null) {
	            String filePath = selectedFile.getAbsolutePath();
	            // System.out.println("Selected file path: " + filePath);
	            return loadPlayList(filePath);
	        } else {
	            // System.out.println("File selection cancelled.");
	        	return loadPlayList();
	        }
		} else { // Unit Test
			return loadPlayList();
		}
	}
	
	public PlayList loadPlayList() {
		return loadPlayList(null);
	}
	public PlayList loadPlayList(String pathname) {
		if(pathname == null || pathname.trim().equals("")) {
			// DEFAULT_PLAYLIST verwenden
			playList = new PlayList("playlists/" + DEFAULT_PLAYLIST);
		} else {
			playList = new PlayList(pathname);
		}
		
		return playList;
	}
	
	private Button createButton(String iconfile) { // aus der angabe übernommen

		// listDir(); //! Debug
		
		Button button = null;
		try {
			URL url = getClass().getResource("/icons/" + iconfile); //! so gehts nur, wenn die icons noch in einem zwischen ordner sind
			// URL url = getClass().getResource("/" + iconfile); //! es geht nur so, aber auch nur wenn icons ein source folder ist
			Image icon = new Image(url.toString());
			ImageView imageView = new ImageView(icon);
			imageView.setFitHeight(20);
			imageView.setFitWidth(20);
			button = new Button("", imageView);
			button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			button.setStyle("-fx-background-color: #fff;");
		} catch (Exception e) {
			System.out.println("Image " + "icons/" + iconfile + " not found!");
			System.exit(-1);
		}

		return button;
	}
	
	
	public static void main(String[] args) {
		launch();
	}
	

	public void listDir() { //! Debug
        URL url;
		try {
			url = getClass().getResource("/");
	        System.out.println(url);
	        
	        if (url != null) {
	            File resourceFolder = new File(url.getFile());
	            listFilesAndFolders(resourceFolder);
	        } else {
	            System.out.println("Der Pfad zur Ressource wurde nicht gefunden.");
	        }
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
    public static void listFilesAndFolders(File folder) { //! Debug
        if (folder.isDirectory()) {
        	System.out.println("Ordner: " + folder.getAbsolutePath());
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    listFilesAndFolders(file);
                }
            }
        } else {
        	System.out.println("Datei: " + folder.getAbsolutePath());
        }
    }
}
