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
		setTitle(title);
		String author = (String) tagMap.get("author");
		setAuthor(author);
		String album = (String) tagMap.get("album");
		this.album = album;
		long duration = (long) tagMap.get("duration");
		super.duration = duration;
	}
	
	public String toString() {
		return "";
	}
}
