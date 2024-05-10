import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

//- Speichern und Löschen von AudioFile-Instanzen in bzw. aus der Liste
//- Aktuelle Abspielposition in der Liste:
//	 - SetzenundLesenderaktuellenAudioFile-Instanz
//	 - WeiterschaltenaufdasnächsteAudioFile
//- Sequenzieller Abspielmodus
//- Laden der Liste aus einer Textdatei im M3U-Format
//- Speichern der Liste im M3U-Format


public class PlayList {

	public PlayList() {
		
	}
	
	public PlayList(String m3uPathname) {
		this();
		// ! was mit m3uPathname?
	}
	
	
	public void add(AudioFile file) {
		
	}
	
	public void remove(AudioFile file) {
		
	}
	
	public int size() {
		return 0;
		
	}
	
	public AudioFile currentAudioFile() {
		return null;
		
	}
	
	public void nextSong() {
		
	}
	
	public void loadFromM3U(String pathname) {
		File file = new File(pathname);
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				// ...
				System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // wird immer ausgeführt
			scanner.close();
		}
	}
	
	public void saveAsM3U(String pathname) {
		File file = new File(pathname);
		FileWriter writer = null;
		
		try {
			writer = new FileWriter(file);
			writer.write("Test \n");
			writer.write("Test \n");
			writer.write("Test \n");
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // wird immer ausgeführt
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<AudioFile> getList() {
		return null;
		
	}
	
	public int getCurrent() {
		return 0;
		
	}
	
	public void setCurrent(int current) {
		
	}
}
