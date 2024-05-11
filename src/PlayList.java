import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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
	
	private LinkedList<AudioFile> audioFileList = new LinkedList<AudioFile>();
	private int current; // Position in der Abspielliste
	

	public PlayList() {
		current = 0;
	}
	
	public PlayList(String m3uPathname) {
		this();
		loadFromM3U(m3uPathname);
	}
	
	
	
	public void add(AudioFile file) {
		audioFileList.add(file);
	}
	
	public void remove(AudioFile file) {
		audioFileList.remove(file);
	}
	
	public int size() {
		return audioFileList.size();
	}
	
	public AudioFile currentAudioFile() {
		if(audioFileList.size() == 0) {
			return null;
		}
		
		return this.audioFileList.get(current);
	}
	
	public void nextSong() {
		if(current > (audioFileList.size() -1)) {
			// wenn Index auf einer ungültigen Position steht, dann 0
			current = 0;
		} else {
			current = (current +1) % (audioFileList.size());	
		}
	}
	
	public void loadFromM3U(String pathname) {
		
		// neu initialisieren
		current = 0;
		while(audioFileList.size() > 0) {
			audioFileList.remove(0);
		}
		
		
		// code aus vorlesung
//		File file = new File(pathname);
//		Scanner scanner = null;
//		try {
//			scanner = new Scanner(file);
//			while(scanner.hasNextLine()) {
//				String line = scanner.nextLine();
//				// ...
//				System.out.println(line);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally { // wird immer ausgeführt
//			scanner.close();
//		}
		
		List<String> data = readFile(pathname);
		
		for(int i = 0; i < data.size(); i++) {
			String line = data.get(i);
			String path = line.substring(5);
			if(!path.trim().equals("") && !path.substring(0, 1).equals("#")) {
				audioFileList.add(AudioFileFactory.createAudioFile(path));
			}
		}
		
		System.out.println(audioFileList.size());
	}
	
	public void saveAsM3U(String pathname) {
		// code aus vorlesung
//		File file = new File(pathname);
//		FileWriter writer = null;
//		
//		try {
//			writer = new FileWriter(file);
//			writer.write("Test \n");
//			writer.write("Test \n");
//			writer.write("Test \n");
//			writer.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally { // wird immer ausgeführt
//			try {
//				writer.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		
		String[] data = new String[audioFileList.size()];
		for (int i = 0; i < audioFileList.size(); i++) {
		    data[i] = audioFileList.get(i).getPathname();
		}
		
		writeFile(pathname, data);
	}
	
	public List<AudioFile> getList() {
		return this.audioFileList;
	}
	
	public int getCurrent() {
		return this.current;
	}
	
	public void setCurrent(int current) {
		this.current = current;
	}
	
	
	static private void writeFile(String path, String[] lines) {
		// aus examples (Vorgabe)
		FileWriter writer = null;
		String sep = System.getProperty("line.separator");
		
		try {
			// create the file if it does not exist, otherwise reset the file and open it for writing
			writer = new FileWriter(path);
			for (String line : lines) {
				// write the current line + newline char
				writer.write(line + sep);
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to write file " + path + "!");
		} finally { // wird immer ausgeführt
			try {
				// close the file writing back all buffers
				writer.close();
			} catch (Exception e) {
				System.out.println("writeFile path: " + path + " error: " + e);
			}
		}
	}
	
	
	static private List<String> readFile(String path) {
		List<String> lines = new ArrayList<>();
		Scanner scanner = null;
		
		try {
			// open the file for reading
			scanner = new Scanner(new File(path));
			int lineNo = 1;
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				// create line with line number prefix
				String lineWithNumber = String.format("%03d: %s", lineNo++, line);
				// add enhanced line to result list
				lines.add(lineWithNumber);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				System.out.println("File " + path + " read!");
				scanner.close();	
			} catch (Exception e) {
				System.out.println("readFile path: " + path + " error: " + e);
			}
		}
		
		return lines;
	}
}
