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
import javafx.scene.control.Labeled;
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
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import studiplayer.audio.AudioFile;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.PlayList;
import studiplayer.audio.SortCriterion;

public class Player extends Application {
	
	//* Settings
	private static Boolean playtimeProgressBar = true; // Bonus
	
	//* Consts
	public static final String DEFAULT_PLAYLIST = "playlists/DefaultPlayList.m3u";
	private static final String PLAYLIST_DIRECTORY = "";
	private static final String INITIAL_PLAY_TIME_LABEL = "00:00";
	private static final String NO_CURRENT_SONG = " - ";
    private static final ObservableList<SortCriterion> sortierKriterien = FXCollections.observableArrayList(SortCriterion.values());
	
    //* Vars
	private PlayList playList = new PlayList();
	private boolean useCertPlayList = false;
	private String selectedPlaylistPath = "-";
	private boolean songIsPaused = false;
	private boolean firstTimePlaying = true; // weil sonst beim ersten next der selbe song zwei mal hintereinander kommt
	
	//* 
	private PlayerThread playerThread;
	private TimerThread timerThread;
	
	
	//* UI Elements
	private Button playButton;
	private Button pauseButton;
	private Button stopButton;
	private Button nextButton;
	private Label playListLabel;
	private Label playTimeLabel;
	private Label playTimeLabel2; // für playtimeProgressBar
	private Label playTimeLabel3; // für playtimeProgressBar
	private Label playTimeLabel4; // für playtimeProgressBar
	private Label currentSongLabel;
	private ChoiceBox<String> sortChoiceBox;
	private TextField searchTextField;
	private Button filterButton;
	private SongTable playlist_tabelle;

	public Player() {
		// ToDo: soll hier irgendwas passieren?
	}
	
	
	public void setUseCertPlayList(boolean value) {
		useCertPlayList = value;
	}
	
	public void setPlayList(String pathname) {
		
	}
	
	
	public void start(Stage stage) throws Exception { // "throws Exception" wegen UnitTest
//		playList = askForPlayList(stage); // an dieser Stelle, da ich die stage übergeben muss und die playlist für "playlist_tabelle" gefüllt sein muss
		
		stage.setTitle("APA Player");
		BorderPane paneMain = new BorderPane();
		

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
			playlist_tabelle = new SongTable(playList);
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
				
				if(!playtimeProgressBar) {
					playTimeLabel = new Label();
					playTimeLabel.setPadding(new Insets(0, 5, 5, 5));
					infoGrid.add(playTimeLabel, 1, 2);
				} else {
					HBox playTimeLabelBox = new HBox();
					playTimeLabelBox.setPadding(new Insets(0, 5, 5, 5));
					infoGrid.add(playTimeLabelBox, 1, 2);
						playTimeLabel = new Label();
//						playTimeLabelBox.getChildren().add(playTimeLabel); // wird nicht angezeigt, nur für UnitTest
						playTimeLabel2 = new Label();
						playTimeLabel2.setPrefWidth(35);
						playTimeLabel2.setAlignment(Pos.CENTER_RIGHT);
						playTimeLabelBox.getChildren().add(playTimeLabel2);
						playTimeLabel3 = new Label();
						playTimeLabel3.setPadding(new Insets(0, 5, 0, 5));
						playTimeLabelBox.getChildren().add(playTimeLabel3);
						playTimeLabel4 = new Label();
						playTimeLabelBox.getChildren().add(playTimeLabel4);
				}
				
			
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
		
		// playlist hier erst festlegen, wegen JunitTest
		playList.loadFromM3U(askForPlayGetPath(stage));
		playlist_tabelle.refreshSongs();
		
		// Falls das Abspielen nicht direkt beginnen soll
		setButtonStates(true, false, false, true);
		
		updatePlayListLabel();
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
//			return loadPlayList();
			return new PlayList();
		}
	}

	public String askForPlayGetPath(Stage stage) {
		String path = "";

		if(!useCertPlayList) { // Playlist PopUp
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			File selectedFile = fileChooser.showOpenDialog(stage);
	        if (selectedFile != null) {
	            String filePath = selectedFile.getAbsolutePath();
	            // System.out.println("Selected file path: " + filePath);
	            path = filePath;
	        } else {
	            // System.out.println("File selection cancelled.");
	        	path = DEFAULT_PLAYLIST;
	        }
		} else { // Unit Test
			path = DEFAULT_PLAYLIST;
		}

		selectedPlaylistPath = path;
		return path;
	}
	
	public PlayList loadPlayList() {
		return loadPlayList(null);
	}
	public PlayList loadPlayList(String pathname) {
		if(pathname == null || pathname.trim().equals("")) {
			// DEFAULT_PLAYLIST verwenden
			pathname = DEFAULT_PLAYLIST;
		}

		selectedPlaylistPath = pathname;
		
		playList.loadFromM3U(pathname);
		playlist_tabelle.refreshSongs();

		return new PlayList(pathname);
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
		Object value = sortChoiceBox.getValue();
		
		String sort_str = value.toString();
		
		boolean filtered = false;
		
		for (SortCriterion type : SortCriterion.values()) { // besser als jeden type mit einzener if zu überprüfen, da die Kriterien dann nur noch in "SortCriterion.java" angepasst werden müssen
			if(sort_str.equals(type.toString())) {
				playList.setSortCriterion(type);
				filtered = true;
			}
		}
		
		if(!filtered) { // falls value nicht for(...) auftaucht, dann nach default filtern (zB falls value null ist, falls das überhaupt passieren kann)
			playList.setSortCriterion(SortCriterion.DEFAULT);
		}

		if(refresh) {
			playlist_tabelle.refreshSongs();
		}
	}

	private void playCurrentSong() {
		// System.out.println("play");
		// - aktualisiere die Informationen zum aktuellen Song und setze die Abspielzeit auf „00:00“
		// - passe die Button-Zustände (setDisable(true|false)) an, Abbildung 1 und Abbildung 4 zeigen den Zustand der Buttons wenn ein Lied abgespielt wird bzw. der Player gestoppt ist.
		// - beginne mit der Wiedergabe
		
		if(firstTimePlaying) { // weil sonst beim ersten next der selbe song zwei mal hintereinander kommt
			firstTimePlaying = false;
			nextSong();
		}
		
		startThreads(false);
		
		if(songIsPaused) {
			pauseCurrentSong();
		}
		
		setButtonStates(false, true, true, true); // muss nach "pauseCurrentSong" sein
	}
	
	private void pauseCurrentSong() {
		// System.out.println("pause");
		// - falls Song abgespielt wird: pausiere die Wiedergabe
		// - falls Song pausiert ist: setze die Wiedergabe fort
		
		AudioFile current = playList.currentAudioFile();
		if(current != null) {
			current.togglePause();
		}

		songIsPaused = !songIsPaused;

		setButtonStates(false, true, true, true); // muss laut UnitTest so sein
	}

	private void stopCurrentSong() {
		// System.out.println("stop");
		// - unterbreche die aktuelle Wiedergabe
		// - passe die Button-Zustände an
		// - setze die Abspielzeit zurück.
		
		songIsPaused = false;
		
		setButtonStates(true, false, false, true); // ToDo: Skip auf true lassen?
		updateSongInfo(playList.currentAudioFile());
		
		stopThreads(false);
	}
	
	private void nextSong() {
		// System.out.println("next");
		// - unterbreche die aktuelle Wiedergabe, falls aktiv
		// - springe zum nächsten Song
		// - zeige die Song-Information an und setze die Abspielzeit auf „00:00“
		// - passe die Button-Zustände an
		playList.nextSong();
		
		songIsPaused = false;
		
		setButtonStates(false, true, true, true);
		updateSongInfo(playList.currentAudioFile());
		
		stopPlayerThread();
		startPlayerThread();
		startTimerThread(); // wenn nach "Stop" "Next" gedrückt wird
	}
	
	private void playSelectedSong(TableViewSelectionModel<Song> tableViewSelectionModel) { // wie würde die Lösung ohne "jumpToAudioFileIndex" aussehen
		int index = tableViewSelectionModel.getSelectedIndex();
		
		if(index >= 0) { // Falls eine Leere Zeile ausgewählt wird
			playList.jumpToAudioFileIndex(index);
			
			updateSongInfo(playList.currentAudioFile());
			
			// play ausführen, falls player gestoppt ist oder noch nie gestartet wurde
			if(!playButton.isDisable()) {
				playCurrentSong();
			}
			
			stopPlayerThread();
			startPlayerThread();	
		}
	}
	
	private void setButtonStates(boolean playButtonState, boolean pauseButtonState, boolean stopButtonState, boolean nextButtonState) {
		playButton.setDisable(!playButtonState);
		pauseButton.setDisable(!pauseButtonState);
		stopButton.setDisable(!stopButtonState);
		nextButton.setDisable(!nextButtonState);
	}
	
	private void updateSongInfo(AudioFile af) {
		updateCurrentSongLabel(af);
		updatePlayTimeLabel(af);
	}
	private void updateCurrentSongLabel(AudioFile af) {
		Platform.runLater(() -> { // weil der Thread sonst nicht auf die JavaFX Elemente zugreifen kann
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
	private void updatePlayTimeLabel(AudioFile af) {
		Platform.runLater(() -> { // weil der Thread sonst nicht auf die JavaFX Elemente zugreifen kann
			if (
				af == null
				|| playerThread == null // wenn der stop button gedrückt wurde (also "playerThread" terminiert wurde)
				) {
				if(!playtimeProgressBar) {
					playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL);
				} else {
					playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL); // wird nicht angezeigt, nur für UnitTest
					playTimeLabel2.setText("");
					playTimeLabel3.setText("");
					playTimeLabel4.setText("");
				}
				
			} else {
				String formatPosition = playList.currentAudioFile().formatPosition();
				String formatDuration = playList.currentAudioFile().formatDuration();
				
				if(!playtimeProgressBar) {
					String text = formatPosition + " - " + formatDuration; //* leicht abgewandelt
					playTimeLabel.setText(text);	
				} else {
					String text = formatPosition + " - " + formatDuration;
					playTimeLabel.setText(text);	
					
					long position = playList.currentAudioFile().getPosition();
					long duration = playList.currentAudioFile().getDuration();
					int progress = (int) ( ( (double) position / (double) duration) * 100 ); // in Prozent
					
					// playTimeLabel.setText("");
					playTimeLabel2.setText(formatPosition);
					playTimeLabel3.setText(getProgressBar(progress));
					playTimeLabel4.setText(formatDuration);
				}
			}
		});	
	}
	private void updatePlayListLabel() {
		playListLabel.setText(selectedPlaylistPath);
	}
	
	public String getProgressBar(int progress) {
		int breite = 20;
		String start = "<";
		String end = ">";
		String current = "⦁";
		String space = "-";
		
		String str = "";
		str += start;
		
		int pos = progress / (100/breite); 
		for(int i = 0; i < 20; i++) {
			if(i != pos) {
				str += space;
			} else {
				str += current;
			}
		}
		
		str += end;
		
		return str;
	}
	
	public static void main(String[] args) { // args in Run Configurations > Arguemnts > Program arguments
//		for(String str : args) { //* Bonus
//			// System.out.println(str);
//			
//			if(str.toLowerCase().equals("showProgressBar".toLowerCase())) {
//				playtimeProgressBar = true;
//			}
//		}
		
		launch();
	}
	
	public void stop() {
		stopThreads(false);
		try {
			super.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public class PlayerThread extends Thread {
		private boolean stopped;
		
		public void terminate() {
			stopped = true;
			playerThread = null;

			AudioFile current = playList.currentAudioFile();
			if(current != null) {
				current.stop();
			}
			
		}
		
		public void run() {
            while (!stopped) {
//    			System.out.println("PlayerThread");
    			
    			try {
					AudioFile current = playList.currentAudioFile();
					if(current != null) {
						current.play(); // Thread ist danach "blockiert"
					}
					
				} catch (NotPlayableException e) {
					e.printStackTrace();
				}
    			
            	try {
            		Thread.sleep(200);
                } catch (InterruptedException e) {
                }
			}
		}
	}
	
	public class TimerThread extends Thread {
		private boolean stopped;
		
		public void terminate() {
			stopped = true;
			timerThread = null;
		}
		
		public void run() {
            while (!stopped) {
//    			System.out.println("TimerThread");
    			
    			updatePlayTimeLabel(playList.currentAudioFile());
    			
            	try {
            		Thread.sleep(200);
                } catch (InterruptedException e) {
                }
			}
		}
	}
	
	private void startThreads(boolean onlyTimer) {
		if(!onlyTimer) {
			startPlayerThread();
		}
		
		startTimerThread();
	}
	private void startPlayerThread() {
		if(playerThread == null) {
			playerThread = new PlayerThread();
			playerThread.start();
		}
	}
	private void startTimerThread() {
		if(timerThread == null) {
			timerThread = new TimerThread();
			timerThread.start();
		}
	}

	private void stopThreads(boolean onlyTimer) {
		if(!onlyTimer) {
			stopPlayerThread();
		}
		
		stopTimerThread();
	}
	private void stopPlayerThread() {
		if(playerThread != null) {
			playerThread.terminate();
		}
	}
	private void stopTimerThread() {
		if(timerThread != null) {
			timerThread.terminate();
		}
	}
}
