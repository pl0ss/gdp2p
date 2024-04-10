public class AudioFileDev {
	private String pathname;
	private String filename;
	private String author;
	private String title;
	
	AudioFileDev() {
		this.pathname = "";
		this.filename = "";
		this.author = "";
		this.title = "";
	}
	
	AudioFileDev(String path) {
		this();
		parsePathname(path);
	}
	
	
	public String getPathname() {
		return pathname;
	}
	private void setPathname(String pathname) {
		this.pathname = pathname;
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
	
	
	public void parsePathname(String path) { // met. nur wegen vorgabe
		setPathname(parseReturnPathname(path));
		parseFilename(path); // wegen JUnit test
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
			while(newPathname.split("\\\\\\\\").length > 1) {
				newPathname = newPathname.replaceAll("\\\\\\\\", "\\\\"); // also \\ zu \
			}
			
			while(newPathname.split("//").length > 1) {
				newPathname = newPathname.replaceAll("//", "/");
			}
		}
		
		// path immer zu Linux
		newPathname = newPathname.replaceAll("\\\\", "/");
		
		// wenn newPathname kein / enthält, dann trim
			// nicht trim: "  /my-tmp/file.mp3"
			// trim "  A.U.T.O.R   -   T.I.T.E.L   .EXTENSION"
		if(newPathname.split("/").length == 1) {
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
		filename = pathElements[pathElements.length -1];
		
		return filename;
	}
	
	private String parseReturnAuthor(String path) {
		String author = parseReturnFilename(path);
		if(author.split(" - ").length > 1) {
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
		
		if(title.split(" - ").length > 1) {
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
	
	// Freiwilliges Extra
//	public boolean equals(Object obj) { // wenn equals überschrieben wird, dann auch hashCode überschreiben
//		if(this == null || obj == null) {
//			return false;
//		}
//		
//		if(this == obj) { // wenn Referenc gleich ist, dann is es ja immer wahr
//			return true;
//		}
//		
//		AudioFile a = (AudioFile) obj;
//		return this.author == a.author &&
//				this.title == a.title &&
//				this.filename == a.filename &&
//				this.pathname == a.pathname;
//	}
	
	
	
	
	private void log(String str) { // Log String
		System.out.println(str);
	}

	private void log(long num) { // Log Long
		System.out.println(num);
	}
	private void log(double num) { // Log Double
		System.out.println(num);
	}
	private void log(boolean x) { // Log boolean
		System.out.println(x);
	}
	private void log(String[] arr) {
		for (int i = 0; i < arr.length; i++) { // Log String Array
			System.out.printf("i:%d\t%s\n", i, arr[i]);
		}
		System.out.println();
	}
	private void log(double[] arr) {
		for (int i = 0; i < arr.length; i++) { // Log Double Array
			System.out.printf("i:%d\t%s\n", i, arr[i]);
		}
		System.out.println();
	}
	
	private boolean test_correct_show = false; 
	
	public void test(String a, String b) {
		if(test_correct_show || !new String(a).equals(b)) {
			System.out.printf("%b %s %s\n", new String(a).equals(b), a, b);
		}
	}
	public void test(String a, String b, int id) { // id ist frei wählbar
		if(test_correct_show || !new String(a).equals(b)) {
			System.out.printf("%d\t%b\t%s\t%s\n", id, new String(a).equals(b), a, b);
		}
	}
	
	private void test_parse() {
		String test_str;
		int test_id = 1;
		
		
		test_str = " "; // 1-6
		parsePathname(test_str);
		parseFilename(test_str);
		test(getPathname(), "", test_id++);
		test(getFilename(), "", test_id++);
		test(parseReturnPathname(test_str, false), "", test_id++);
		test(parseReturnPathname(test_str, true), "", test_id++);
		test(parseReturnFilename(test_str, false), "", test_id++);
		test(parseReturnFilename(test_str, true), "", test_id++);
		
		test_str = "file.mp3"; // 7-12
		parsePathname(test_str);
		parseFilename(test_str);
		test(getPathname(), "file.mp3", test_id++);
		test(getFilename(), "file.mp3", test_id++);
		test(parseReturnPathname(test_str, false), "file.mp3", test_id++);
		test(parseReturnPathname(test_str, true), "file.mp3", test_id++);
		test(parseReturnFilename(test_str, false), "file.mp3", test_id++);
		test(parseReturnFilename(test_str, true), "file.mp3", test_id++);
		
		test_str = "  /my-tmp/file.mp3"; // 13-18
		parsePathname(test_str);
		parseFilename(test_str);
		test(getPathname(), "  /my-tmp/file.mp3", test_id++);
		test(getFilename(), "file.mp3", test_id++);
		test(parseReturnPathname(test_str, false), "  /my-tmp/file.mp3", test_id++);
		test(parseReturnPathname(test_str, true), "  \\my-tmp\\file.mp3", test_id++);
		test(parseReturnFilename(test_str, false), "file.mp3", test_id++);
		test(parseReturnFilename(test_str, true), "file.mp3", test_id++);
		
		test_str = "//my-tmp////part1//file.mp3/"; // 19-24
		parsePathname(test_str);
		parseFilename(test_str);
		test(getPathname(), "/my-tmp/part1/file.mp3/", test_id++);
		test(getFilename(), "", test_id++);
		test(parseReturnPathname(test_str, false), "/my-tmp/part1/file.mp3/", test_id++);
		test(parseReturnPathname(test_str, true), "\\my-tmp\\part1\\file.mp3\\", test_id++);
		test(parseReturnFilename(test_str, false), "", test_id++);
		test(parseReturnFilename(test_str, true), "", test_id++);
		
		test_str = "d:\\\\\\\\part1///file.mp3"; // 25 - 30
		parsePathname(test_str);
		parseFilename(test_str);
		test(getPathname(), "/d/part1/file.mp3", test_id++);
		test(getFilename(), "file.mp3", test_id++);
		test(parseReturnPathname(test_str, false), "/d/part1/file.mp3", test_id++);
		test(parseReturnPathname(test_str, true), "d:\\part1\\file.mp3", test_id++);
		test(parseReturnFilename(test_str, false), "file.mp3", test_id++);
		test(parseReturnFilename(test_str, true), "file.mp3", test_id++);
		
		test_str = "-"; // 31 - 36
		parsePathname(test_str);
		parseFilename(test_str);
		test(getPathname(), "-", test_id++);
		test(getFilename(), "-", test_id++);
		test(parseReturnPathname(test_str, false), "-", test_id++);
		test(parseReturnPathname(test_str, true), "-", test_id++);
		test(parseReturnFilename(test_str, false), "-", test_id++);
		test(parseReturnFilename(test_str, true), "-", test_id++);
		
		test_str = " - "; // 37 - 42
		parsePathname(test_str);
		parseFilename(test_str);
		test(getPathname(), "-", test_id++);
		test(getFilename(), "-", test_id++);
		test(parseReturnPathname(test_str, false), "-", test_id++);
		test(parseReturnPathname(test_str, true), "-", test_id++);
		test(parseReturnFilename(test_str, false), "-", test_id++);
		test(parseReturnFilename(test_str, true), "-", test_id++);
		
		
		
		test_str = "home\\meier\\Musik\\Falco - Rock Me Amadeus.mp3"; // 43 - 48
		parsePathname(test_str);
		parseFilename(test_str);
		test(getPathname(), "home/meier/Musik/Falco - Rock Me Amadeus.mp3", test_id++);
		test(getFilename(), "Falco - Rock Me Amadeus.mp3", test_id++);
		test(parseReturnPathname(test_str, false), "home/meier/Musik/Falco - Rock Me Amadeus.mp3", test_id++);
		test(parseReturnPathname(test_str, true), "home\\meier\\Musik\\Falco - Rock Me Amadeus.mp3", test_id++);
		test(parseReturnFilename(test_str, false), "Falco - Rock Me Amadeus.mp3", test_id++);
		test(parseReturnFilename(test_str, true), "Falco - Rock Me Amadeus.mp3", test_id++);
		
		log("test_id: " + test_id);
	}
	
	private void test_author_title() {
		String test_str;
		String author_str;
		String title_str;
		int test_id = 1;
		
		test_str = " Falco  -  Rock me    Amadeus .mp3  "; // 1-4
		author_str = "Falco";
		title_str = "Rock me    Amadeus";
		parsePathname(test_str);
		parseFilename(test_str);
		test(getAuthor(), author_str, test_id++);
		test(getTitle(), title_str, test_id++);
		test(parseReturnAuthor(test_str), author_str, test_id++);
		test(parseReturnTitle(test_str), title_str, test_id++);
		
		test_str = "Frankie Goes To Hollywood - The Power Of Love.ogg"; // 5-8
		author_str = "Frankie Goes To Hollywood";
		title_str = "The Power Of Love";
		parsePathname(test_str);
		parseFilename(test_str);
		test(getAuthor(), author_str, test_id++);
		test(getTitle(), title_str, test_id++);
		test(parseReturnAuthor(test_str), author_str, test_id++);
		test(parseReturnTitle(test_str), title_str, test_id++);
		
		test_str = "audiofile.aux"; // 9-12
		author_str = "";
		title_str = "audiofile";
		parsePathname(test_str);
		parseFilename(test_str);
		test(getAuthor(), author_str, test_id++);
		test(getTitle(), title_str, test_id++);
		test(parseReturnAuthor(test_str), author_str, test_id++);
		test(parseReturnTitle(test_str), title_str, test_id++);
		
		test_str = "   A.U.T.O.R   -  T.I.T.E.L  .EXTENSION"; // 13-16
		author_str = "A.U.T.O.R";
		title_str = "T.I.T.E.L";
		parsePathname(test_str);
		parseFilename(test_str);
		test(getAuthor(), author_str, test_id++);
		test(getTitle(), title_str, test_id++);
		test(parseReturnAuthor(test_str), author_str, test_id++);
		test(parseReturnTitle(test_str), title_str, test_id++);
		
		test_str = "Hans-Georg Sonstwas - Blue-eyed boy-friend.mp3"; // 17-20
		author_str = "Hans-Georg Sonstwas";
		title_str = "Blue-eyed boy-friend";
		parsePathname(test_str);
		parseFilename(test_str);
		test(getAuthor(), author_str, test_id++);
		test(getTitle(), title_str, test_id++);
		test(parseReturnAuthor(test_str), author_str, test_id++);
		test(parseReturnTitle(test_str), title_str, test_id++);
		
		test_str = ".mp3"; // 21-24
		author_str = "";
		title_str = "";
		parsePathname(test_str);
		parseFilename(test_str);
		test(getAuthor(), author_str, test_id++);
		test(getTitle(), title_str, test_id++);
		test(parseReturnAuthor(test_str), author_str, test_id++);
		test(parseReturnTitle(test_str), title_str, test_id++);
		
		test_str = "Falco - Rock me Amadeus."; // 25-28
		author_str = "Falco";
		title_str = "Rock me Amadeus";
		parsePathname(test_str);
		parseFilename(test_str);
		test(getAuthor(), author_str, test_id++);
		test(getTitle(), title_str, test_id++);
		test(parseReturnAuthor(test_str), author_str, test_id++);
		test(parseReturnTitle(test_str), title_str, test_id++);
		
		test_str = " - "; // 29-32
		author_str = "";
		title_str = "";
		parsePathname(test_str);
		parseFilename(test_str);
		test(getAuthor(), author_str, test_id++);
		test(getTitle(), title_str, test_id++);
		test(parseReturnAuthor(test_str), author_str, test_id++);
		test(parseReturnTitle(test_str), title_str, test_id++);
		
		test_str = "-"; // 33-36
		author_str = "";
		title_str = "-";
		parsePathname(test_str);
		parseFilename(test_str);
		test(getAuthor(), author_str, test_id++);
		test(getTitle(), title_str, test_id++);
		test(parseReturnAuthor(test_str), author_str, test_id++);
		test(parseReturnTitle(test_str), title_str, test_id++);
		
		
		test_str = "Falco - Rock Me Amadeus.mp3"; // 37-40
		author_str = "Falco";
		title_str = "Rock Me Amadeus";
		parsePathname(test_str);
		parseFilename(test_str);
		test(getAuthor(), author_str, test_id++);
		test(getTitle(), title_str, test_id++);
		test(parseReturnAuthor(test_str), author_str, test_id++);
		test(parseReturnTitle(test_str), title_str, test_id++);
		
		log("test_id: " + test_id);
	}
	
	public static void main(String[] args) {

		AudioFileDev testAudio = new AudioFileDev();
		testAudio.test_parse();
		testAudio.test_author_title();
		
		System.out.println("Done");

	}
}
