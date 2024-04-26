// import java.util.ArrayList;
// import java.util.List;
import java.util.Map;

import studiplayer.basic.TagReader;

public class TaggedFile extends SampledFile {
	
	/*
	 - Liest Dateieigenschaften aus den Tags der Audiodatei.
	 */
	
	private String album;

	
	TaggedFile() {
		
	}
	
	TaggedFile(String path) {
		super(path);
		readAndStoreTags();
	}
	
	
	public String getAlbum() {
		return this.album;
	}
	
	public void readAndStoreTags() {
		String path = getPathname();
		
		Map<String, Object> tagMap = TagReader.readTags(path);
		
		String title = (String) tagMap.get("title");
		setTitle(title.trim());
		String author = (String) tagMap.get("author");
		setAuthor(author.trim());
		String album = (String) tagMap.get("album");
		this.album = album.trim();
		long duration = (long) tagMap.get("duration");
		super.duration = duration;
	}
	
	public String toString() {
		String album = getAlbum();
		String album_str = (album != null) ? album + " - " : "";
		
		return super.toString() + " - " + album_str + formatDuration();
	}
	
	// Alternative Lösung
//	public String toString() {
//		String album = getAlbum();
//		
//		List<String> res = new ArrayList<>();
//		
//		res.add(super.toString());
//		if(album != null) {
//			res.add(album);
//		}
//		res.add(formatDuration());
//		
//		return String.join(" - ", res);
//	}
}
