package studiplayer.audio;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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


public class PlayList implements Iterable<AudioFile> {
	
	private LinkedList<AudioFile> files = new LinkedList<AudioFile>();
	private int current; // Position in der Abspielliste
	private String search;
	private SortCriterion sortCriterion = SortCriterion.DEFAULT;

	public PlayList() {
		current = 0;
	}
	
	public PlayList(String m3uPathname) throws NotPlayableException {
		this();
		loadFromM3U(m3uPathname);
	}
	
	
	public void setSearch(String search) {
		this.search = search;
		
		if(search != null & search != "") {
			
			for(int i = 0; i < files.size(); i++) {
				AudioFile a = files.get(i);
				
				String autor = a.getAuthor();
				if(autor.contains(search)) {
					i++;
					continue;
				}
				
				String title = a.getTitle();
				if(title.contains(search)) {
					i++;
					continue;
				}
				
				String album = "";
				if(a instanceof TaggedFile) {
					album = ((TaggedFile) a).getAlbum();
					if(album.contains(search)) {
						i++;
						continue;
					}
				}
				
				files.remove(i);
			}
			
		}
	}
	public String getSearch() {
		return search;
	}
	
	public void setSortCriterion(SortCriterion sortCriterion) {
		this.sortCriterion = sortCriterion;
	}
    
	
	public SortCriterion getSortCriterion() {
		return sortCriterion;
	}
	
	
	
	public void add(AudioFile file) {
		files.add(file);
	}
	
	public void remove(AudioFile file) {
		files.remove(file);
	}
	
	public int size() {
		return files.size();
	}
	
	public AudioFile currentAudioFile() {
		if(files.size() == 0) {
			return null;
		}
		
		return this.files.get(current);
	}
	
	public void nextSong() {
		if(current > (files.size() -1)) {
			// wenn Index auf einer ungültigen Position steht, dann 0
			current = 0;
		} else {
			current = (current +1) % (files.size());	
		}
	}
	
	public void loadFromM3U(String pathname) {
		// neu initialisieren
		current = 0;
		while(files.size() > 0) {
			files.remove(0);
		}


		List<String> data = readFile(pathname);
		
		for(int i = 0; i < data.size(); i++) {
			String line = data.get(i);
			String path = line.substring(5);

			if(!path.trim().equals("") && !path.substring(0, 1).equals("#")) {
				try {
					files.add(AudioFileFactory.createAudioFile(path));
				} catch (Exception e) {
					System.out.println(e + " Diese Datei Existiert nicht: " + path );
					// throw new Exception("Datei nicht lesbar: " + pathname);
				}
			}
		}
	}
	
	public void saveAsM3U(String pathname) {
		String[] data = new String[files.size()];
		for (int i = 0; i < files.size(); i++) {
		    data[i] = files.get(i).getPathname();
		}
		
		writeFile(pathname, data);
	}
	
	public List<AudioFile> getList() {
		return this.files;
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

	public Iterator<AudioFile> iterator() {
		return new ControllablePlayListIterator(files, search, sortCriterion);
	}
	
	public String toString() {
		String text = "";
		
	    for (int i = 0; i < files.size(); i++) {
	        AudioFile file = files.get(i);
	        text += file.toString() + ' ';
	    }
		
		return text;
	}
}
