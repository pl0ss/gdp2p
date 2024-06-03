package studiplayer.ui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
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
import studiplayer.audio.AudioFile;
import studiplayer.audio.PlayList;
import studiplayer.audio.SortCriterion;

public class Player extends Application {
	
	public static final String DEFAULT_PLAYLIST = "playList.cert.m3u";
	private static final String PLAYLIST_DIRECTORY = "";
	private static final String INITIAL_PLAY_TIME_LABEL = "00:00";
	private static final String NO_CURRENT_SONG = " - ";
    private static final ObservableList<String> sortierKriterien = FXCollections.observableArrayList("Standard", "Autor", "Titel", "Album", "Dauer");
	
	private PlayList playList;
	private boolean useCertPlayList = false;
	
	
	//* UI Elements
	private Button playButton;
	private Button pauseButton;
	private Button stopButton;
	private Button nextButton;
	private Label playListLabel;
	private Label playTimeLabel;
	private Label currentSongLabel;
	private ChoiceBox<String> sortChoiceBox;
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
			
				sortChoiceBox = new ChoiceBox(sortierKriterien);
				sortChoiceBox.setPrefWidth(200);
				sortierung.getChildren().add(sortChoiceBox);
				
				VBox ButtonBox = new VBox();
				ButtonBox.setPadding(new Insets(0, 0, 0, 20));
					filterButton = new Button("Anzeigen");
					ButtonBox.getChildren().add(filterButton);
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
				playListLabel = new Label();
				playListLabel.setPadding(new Insets(5, 5, 5, 5));
				infoGrid.add(playListLabel, 1, 0);
				
				Label currentSongLabel1 = new Label("Aktuell: ");
				currentSongLabel1.setPadding(new Insets(0, 5, 5, 5));
				infoGrid.add(currentSongLabel1, 0, 1);
				currentSongLabel = new Label();
				currentSongLabel.setPadding(new Insets(0, 5, 5, 5));
				infoGrid.add(currentSongLabel, 1, 1);
				
				Label playTimeLabel1 = new Label("Abspielzeit: ");
				playTimeLabel1.setPadding(new Insets(0, 5, 5, 5));
				infoGrid.add(playTimeLabel1, 0, 2);
				playTimeLabel = new Label();
				playTimeLabel.setPadding(new Insets(0, 5, 5, 5));
				infoGrid.add(playTimeLabel, 1, 2);
				
			
			HBox buttons = new HBox();
			buttons.setPadding(new Insets(5, 5, 5, 5));
			buttons.setAlignment(Pos.CENTER);
			sectionBottom.getChildren().add(buttons);
				playButton = createButton("play.jpg");
				buttons.getChildren().add(playButton);
				pauseButton = createButton("pause.jpg");
				buttons.getChildren().add(pauseButton);
				stopButton = createButton("stop.jpg");
				buttons.getChildren().add(stopButton);
				nextButton = createButton("next.jpg");
				buttons.getChildren().add(nextButton);
		
		
		
		paneMain.setTop(sectionFilter);
		paneMain.setCenter(sectionPlaylist);
		paneMain.setBottom(sectionBottom);

		
		Scene scene = new Scene(paneMain, 600, 400);
		stage.setScene(scene);
		stage.show();
		
		// Falls das Abspielen nicht direkt beginnen soll
		setButtonStates(true, false, false, false); // ToDo: passt das so?
		
		update_playListLabel();
		updateSongInfo(playList.currentAudioFile());
		
		
		
		searchTextField.setOnAction(e -> {
			playlistSearch(playlist_tabelle, true);
		});
		searchTextField.setOnKeyReleased(e -> {
			playlistSearch(playlist_tabelle, true);
		});
		
		sortChoiceBox.setOnAction(e -> {
			playlistFilter(playlist_tabelle, true);
		});
		
		filterButton.setOnAction(e -> {
			playlistSearchFilter(playlist_tabelle);
		});
		
		
		playButton.setOnAction(e -> {
			playCurrentSong();
		});
		
		pauseButton.setOnAction(e -> {
			pauseCurrentSong();
		});
		
		stopButton.setOnAction(e -> {
			stopCurrentSong();
		});
		
		nextButton.setOnAction(e -> {
			nextSong();
		});
		
		playlist_tabelle.setRowSelectionHandler(e -> {
			playSelectedSong(playlist_tabelle.getSelectionModel());
		});
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
		Button button = null;
		try {
			URL url = getClass().getResource("/icons/" + iconfile); // icons Ordner muss in src (oder einem anderen SourceFolder) liegen
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
	
	private void playlistSearchFilter(SongTable playlist_tabelle) {
		playlistSearch(playlist_tabelle, false);
		playlistFilter(playlist_tabelle, true);
		
	}
	private void playlistSearch(SongTable playlist_tabelle, boolean refresh) {
		String search = searchTextField.getText();
		playList.setSearch(search);

		if(refresh) {
			playlist_tabelle.refreshSongs();
		}
	}
	private void playlistFilter(SongTable playlist_tabelle, boolean refresh) {
		String sort_str = (String) sortChoiceBox.getValue();
		if(sort_str == null || sort_str.equals(sortierKriterien.get(0))) {
			playList.setSortCriterion(SortCriterion.DEFAULT);
		} else if(sort_str.equals(sortierKriterien.get(1))) {
			playList.setSortCriterion(SortCriterion.AUTHOR);
		} else if(sort_str.equals(sortierKriterien.get(2))) {
			playList.setSortCriterion(SortCriterion.TITLE);
		} else if(sort_str.equals(sortierKriterien.get(3))) {
			playList.setSortCriterion(SortCriterion.ALBUM);
		} else if(sort_str.equals(sortierKriterien.get(4))) {
			playList.setSortCriterion(SortCriterion.DURATION);
		}

		if(refresh) {
			playlist_tabelle.refreshSongs();
		}
	}

	private void playCurrentSong() {
		System.out.println("play");
		// ToDo
		// - aktualisiere die Informationen zum aktuellen Song und setze die Abspielzeit auf „00:00“
		// - passe die Button-Zustände (setDisable(true|false)) an, Abbildung 1 und Abbildung 4 zeigen den Zustand der Buttons wenn ein Lied abgespielt wird bzw. der Player gestoppt ist.
		// - beginne mit der Wiedergabe
		
		setButtonStates(false, true, true, true); // ToDo: passt das so? 
	}
	
	private void pauseCurrentSong() {
		System.out.println("pause");
		// ToDo
		// - falls Song abgespielt wird: pausiere die Wiedergabe
		// - falls Song pausiert ist: setze die Wiedergabe fort
		
		setButtonStates(true, false, true, true); // ToDo: passt das so?
	}

	private void stopCurrentSong() {
		System.out.println("stop");
		// ToDo
		// - unterbreche die aktuelle Wiedergabe
		// - passe die Button-Zustände an
		// - setze die Abspielzeit zurück.
		
		setButtonStates(true, false, false, true); // ToDo: passt das so?
		updateSongInfo(playList.currentAudioFile());
	}
	
	private void nextSong() {
		System.out.println("next");
		// - unterbreche die aktuelle Wiedergabe, falls aktiv
		// - springe zum nächsten Song
		// - zeige die Song-Information an und setze die Abspielzeit auf „00:00“
		// - passe die Button-Zustände an
		playList.nextSong();
		
		setButtonStates(true, true, true, true); // ToDo: passt das so?
		updateSongInfo(playList.currentAudioFile());
	}
	
	private void playSelectedSong(TableViewSelectionModel<Song> tableViewSelectionModel) { // ToDo
		System.out.println(tableViewSelectionModel);
	}
	
	private void setButtonStates(boolean playButtonState, boolean pauseButtonState, boolean stopButtonState, boolean nextButtonState) {
		playButton.setDisable(!playButtonState);
		pauseButton.setDisable(!pauseButtonState);
		stopButton.setDisable(!stopButtonState);
		nextButton.setDisable(!nextButtonState);
	}
	
	private void updateSongInfo(AudioFile af) {
		update_currentSongLabel(af);
		update_playTimeLabel(af);
	}
	private void update_currentSongLabel(AudioFile af) {
		Platform.runLater(() -> {
			if (af == null) {
				currentSongLabel.setText(NO_CURRENT_SONG);
			} else {
				String title = playList.currentAudioFile().getTitle();
				String author = playList.currentAudioFile().getAuthor();
				String album = playList.currentAudioFile().getAlbum();
				
				String text = author + " - " + title; //* leicht abgewandelt
				if(album != null) {
					text += " - " + album;
				}
				
				currentSongLabel.setText(text);
			}
		});
	}
	private void update_playTimeLabel(AudioFile af) {
		Platform.runLater(() -> {
			if (af == null) {
				playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL);
			} else {
				String duration = playList.currentAudioFile().formatDuration();
				String position = playList.currentAudioFile().formatPosition();
				
				String text = position + " - " + duration; //* leicht abgewandelt
				
				playTimeLabel.setText(text);
			}
		});	
	}
	private void update_playListLabel() {
		String text = "/path/"; // ToDo: muss noch angepasst werden
		playListLabel.setText(text);
	}
	
	public static void main(String[] args) {
		launch();
	}
}
