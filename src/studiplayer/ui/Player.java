package studiplayer.ui;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import studiplayer.audio.PlayList;

public class Player extends Application {
	
	public static final String DEFAULT_PLAYLIST = "playList.cert.m3u";
	private PlayList playList;
	public boolean useCertPlayList = false;

	public Player() {

	}
	
	
	public void setUseCertPlayList(boolean value) {
		useCertPlayList = value;
	}
	
	public void setPlayList(String pathname) {
		
	}
	
	
	public void start(Stage stage) {
		stage.setTitle("APA Player");
		Pane pane = new FlowPane();
		Scene scene = new Scene(pane, 600, 400);
		
		stage.setScene(scene);
		stage.show();
		
		if(!useCertPlayList) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			File selectedFile = fileChooser.showOpenDialog(stage);
	        if (selectedFile != null) {
	            String filePath = selectedFile.getAbsolutePath();
	            // System.out.println("Selected file path: " + filePath);
	            loadPlayList(filePath);
	        } else {
	            // System.out.println("File selection cancelled.");
	        }
		} else {
			loadPlayList();
		}
	}
	
	public void loadPlayList() {
		loadPlayList(null);
	}
	public void loadPlayList(String pathname) {
		if(pathname == null || pathname.trim().equals("")) {
			playList = new PlayList(DEFAULT_PLAYLIST);
		} else {
			playList = new PlayList(pathname);
		}
	}
	
	public static void main(String[] args) {
		launch();
	}

}
