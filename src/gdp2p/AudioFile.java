package gdp2p;

public class AudioFile {
	// AudioFile
	static String pathname;
	static String filename;
	static String author;
	static String title;
	
	
	static void parsePathname(String path) { // met. nur wegen vorgabe
		parseSetPathname(path);
	}
	static void parseSetPathname(String path) {
		pathname = parseReturnPathname(path, isWindows());
	}
	static String parseReturnPathname(String path, boolean isWindows) {
		
		// wenn path nur aus Leerzeichen und Tabs besteht, dann return ""
		if(new String(path.trim()).equals("")) {
			return "";
		}
		
		// wenn path nur aus Leerzeichen und Tabs und einem - besteht, dann return "-"
		if(new String(path.trim()).equals("-")) {
			return "-";
		}
		
		String newPathname = path;
		String replaceThis;
		String replaceToThat;
		
		if(isWindows) {
			newPathname = newPathname.replaceAll("/", "\\\\");
		}

		
//		if(isWindows) {
//			replaceThis = "\\\\\\\\"; // also \\
//			replaceToThat = "\\\\"; // zu \
//		} else {
//			replaceThis = "//";
//			replaceToThat = "/";
//		}
//		
//		while(newPathname.split(replaceThis).length > 1) {
//			newPathname = newPathname.replaceAll(replaceThis, replaceToThat);
//		}
		
		// Alle Doppelten / oder \ entfernen
		
		replaceThis = "\\\\\\\\"; // also \\
		replaceToThat = "\\\\"; // zu \
		while(newPathname.split(replaceThis).length > 1) {
			newPathname = newPathname.replaceAll(replaceThis, replaceToThat);
		}
		
		replaceThis = "//";
		replaceToThat = "/";
		while(newPathname.split(replaceThis).length > 1) {
			newPathname = newPathname.replaceAll(replaceThis, replaceToThat);
		}
		
		
		if(!isWindows) {
			newPathname = newPathname.replaceAll("\\\\", "/");
			if(newPathname.charAt(1) == ':') {
				newPathname = newPathname.replaceAll(":", "");
				newPathname = '/' + newPathname;
			}
		}
		
		return newPathname;
	}
	
	
	static void parseFilename(String path) { // met. nur wegen vorgabe
		parseSetFilename(path);
	}
	static void parseSetFilename(String path) {
		filename = parseReturnFilename(path, isWindows());
	}
	static String parseReturnFilename(String path, boolean isWindows) {
		
		// wenn path nur aus Leerzeichen und Tabs besteht, dann return ""
		if(new String(path.trim()).equals("")) {
			return "";
		}
		
		// wenn path nur aus Leerzeichen und Tabs und einem - besteht, dann return "-"
		if(new String(path.trim()).equals("-")) {
			return "-";
		}
		
		String splitBy;
		String filename;

		if(isWindows) {
			splitBy = "\\\\"; // nur zwei machen probleme
		} else {
			splitBy = "/";
		}
		
		// Prüfen, on letztes element ein / bzw \\ ist
		String[] pathArr = path.split("");
		String letzteZeichen = pathArr[pathArr.length -1];
		
		{ // Strings vergleichen
			// ==
				// geht nicht, weil Referenzen damit verglichen werden
			// (int)letzteZeichen.toCharArray()[0] == (int)splitBy.toCharArray()[0]
				// geht nur wenn String ein zeichen lang ist
			// new String(letzteZeichen).equals(splitBy)
				// müsste immer gehen
		}
		
		if(new String(letzteZeichen).equals("\\\\") || new String(letzteZeichen).equals("/")) {
			filename = "";
		} else {
			String thisPath = path;
			if(isWindows) {
				thisPath = thisPath.replaceAll("/", "\\\\");
			}
			
			String[] pathElements = thisPath.split(splitBy);
			filename = pathElements[pathElements.length -1];
		}
		
		return filename;
	}
	
	static String getPathname() {
		return pathname;
	}
	
	static String getFilename() {
		return filename;
	}
	
	static String getAuthor() {
		return author;
	}
	
	static String getTitle() {
		return title;
	}
	
	private static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
	}
	
	static void log(String str) { // Log String
		System.out.println(str);
	}
	static void log(long num) { // Log Long
		System.out.println(num);
	}
	static void log(double num) { // Log Double
		System.out.println(num);
	}
	static void log(boolean x) { // Log boolean
		System.out.println(x);
	}
	static void log(String[] arr) {
		for (int i = 0; i < arr.length; i++) { // Log String Array
			System.out.printf("i:%d\t%s\n", i, arr[i]);
		}
		System.out.println();
	}
	static void log(double[] arr) {
		for (int i = 0; i < arr.length; i++) { // Log Double Array
			System.out.printf("i:%d\t%s\n", i, arr[i]);
		}
		System.out.println();
	}
	
	static void test(String a, String b) {
		if(!new String(a).equals(b)) {
			System.out.printf("%b %s %s\n", new String(a).equals(b), a, b);
		}
	}
	static void test(String a, String b, int id) { // id ist frei wählbar
		if(!new String(a).equals(b)) {
			System.out.printf("%d\t%b\t%s\t%s\n", id, new String(a).equals(b), a, b);
		}
	}
	
	static void test_parse() {
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
		
	}
	
	public static void main(String[] args) {
		
		test_parse();

	}
}
