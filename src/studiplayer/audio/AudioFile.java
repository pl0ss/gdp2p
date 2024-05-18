package studiplayer.audio;


import java.io.File;

public abstract class AudioFile {
	
	/*
	 - Bietet Methoden zum Parsen des Dateinamens der Audiodatei.
	 - Gibt abstrakte Methoden zur Wiedergabe der Audiodatei, zum Pausieren bzw. Anhalten
		der Wiedergabe der Audiodatei sowie zur Formatierung der Abspieldauer und Abspielposition vor.
	 */
	
	private String pathname;
	private String filename;
	private String author;
	private String title;

	
	AudioFile() {
		this.pathname = "";
		this.filename = "";
		this.author = "";
		this.title = "";
	}
	
	AudioFile(String path) throws NotPlayableException {
		this();
		parsePathname(path);
	}
	
	
	public String getPathname() {
		return this.pathname;
	}
	public void setPathname(String pathname) throws NotPlayableException {
		parsePathname(pathname);
	}
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}


	private boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
	}
	
	
	public void parsePathname(String path) throws NotPlayableException {
		String newPathname = parseReturnPathname(path);
		// Prüfen, ob Datei lesbar ist
        File datei = new File(newPathname);
        // if (!datei.canRead()) {
        //     throw new RuntimeException("Datei nicht lesbar: " + newPathname);
        // }

        if (!datei.canRead()) {
            throw new NotPlayableException(newPathname, "Datei nicht lesbar: " + newPathname);
        }

        this.pathname = newPathname;
		
		parseFilename(path);
	}
	private String parseReturnPathname(String path) {
		return parseReturnPathname(path, isWindows());
	}
	private String parseReturnPathname(String path, boolean isWindows) {
		String newPathname = path;
		
		// wenn path nur aus Leerzeichen und Tabs besteht, dann return ""
		if(new String(path.trim()).equals("")) {
			return "";
		}
		
		// wenn path nur aus Leerzeichen und Tabs und einem - besteht, dann return "-"
		if(new String(path.trim()).equals("-")) {
			return "-";
		}
		
		
		// Alle Doppelten / oder \ entfernen
		{
			while(newPathname.contains("\\\\\\\\")) {
				newPathname = newPathname.replaceAll("\\\\\\\\", "\\\\"); // also \\ zu \
			}
			
			while(newPathname.contains("//")) {
				newPathname = newPathname.replaceAll("//", "/");
			}
		}
		
		// path immer zu Linux
		newPathname = newPathname.replaceAll("\\\\", "/");
		
		// wenn newPathname kein / enthält, dann trim
			// nicht trim: "  /my-tmp/file.mp3"
			// trim "  A.U.T.O.R   -   T.I.T.E.L   .EXTENSION"
		if(newPathname.contains("/")) {
			newPathname = newPathname.trim();
		}
		
		// path zu windows Format, falls windows
		if(isWindows) {
			newPathname = newPathname.replaceAll("/", "\\\\");
		}
		
		
		// drive Replace, falls Linux
		if(!isWindows) {
			if(newPathname.charAt(1) == ':') {
				newPathname = newPathname.replaceAll(":", "");
				newPathname = '/' + newPathname;
			}
		}

		// Entferne spaces am ende
		while (newPathname.charAt(newPathname.length() - 1) == ' ') {
            newPathname = newPathname.substring(0, newPathname.length() - 1);
        }
		
		return newPathname;
	}
	
	
	public void parseFilename(String path) { // met. nur wegen vorgabe
		setFilename(parseReturnFilename(path));
		setAuthor(parseReturnAuthor(path));
		setTitle(parseReturnTitle(path));
	}
	private String parseReturnFilename(String path) {
		return parseReturnFilename(path, isWindows());
	}
	private String parseReturnFilename(String path, boolean isWindows) {
		path = parseReturnPathname(path, false); // LinuxFormat
		
		// wenn path nur aus Leerzeichen und Tabs besteht, dann return ""
		if(new String(path.trim()).equals("")) {
			return "";
		}
		
		// wenn path nur aus Leerzeichen und Tabs und einem - besteht, dann return "-"
		if(new String(path.trim()).equals("-")) {
			return "-";
		}
		
		
		// Prüfen, on letztes element ein / bzw \\ ist: Dann return ""
		{
			String[] pathArr = path.split("");
			String letzteZeichen = pathArr[pathArr.length -1];
			
			if(new String(letzteZeichen).equals("\\\\") || new String(letzteZeichen).equals("/")) {
				return "";
			}
		}
		
		// return letztes element zwischen Slashes
		String[] pathElements = path.split("/");
		String newFilename = pathElements[pathElements.length -1];

		newFilename = newFilename.trim();
		
		return newFilename;
	}
	
	private String parseReturnAuthor(String path) {
		String author = parseReturnFilename(path);
		if(author.contains(" - ")) {
			author = author.split(" - ")[0];
		} else { // wenn filename kein " - " enthält
			return "";
		}
		
		author = author.trim();
		
		return author;
	}
	
	private String parseReturnTitle(String path) {
		// falls " - " als path übergeben wird (JUnitTest), kann bei pathname nie entstehen
		if(new String(path).equals("-")) {
			return "-";
		}
		
		if(new String(path).equals(" - ")) {
			return "";
		}
		
		String title = parseReturnFilename(path);
		
		if(title.contains(" - ")) {
			title = title.split(" - ")[1];
		}
		
		int posLetzterPunkt = title.lastIndexOf(".");
		if(posLetzterPunkt >= 0) {
			title = title.substring(0, posLetzterPunkt);
		}
		
		title = title.trim();
		
		return title;
	}
	
	public String toString() {
		if(this.pathname.equals("-")) { // wegen JUnit test
			return "-";
		}
		if(this.author.equals("")) {
			return this.title;
		}
		if(this.title.equals("")) {
			return this.author;
		}
		
		return this.author + " - " + this.title;
	}
	
	
	abstract public void play() throws NotPlayableException;
	abstract public void togglePause();
	abstract public void stop();
	abstract public String formatDuration();
	abstract public String formatPosition();
	
	
	public static void main(String[] args) {

	}
}
