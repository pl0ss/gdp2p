package studiplayer.audio;


import java.util.Map;

import studiplayer.basic.TagReader;

public class TaggedFile extends SampledFile {
	
	/*
	 - Liest Dateieigenschaften aus den Tags der Audiodatei.
	 */
	
	private String album;

	
	public TaggedFile() {
		
	}
	
	public TaggedFile(String path) throws NotPlayableException {
		super(path);
		readAndStoreTags();
	}
	
	
	public String getAlbum() {
		return this.album;
	}
	
	public void readAndStoreTags() throws NotPlayableException {
		try {
			String path = getPathname();
			Map<String, Object> tagMap = TagReader.readTags(path);
			
			String title = (String) tagMap.get("title");
			if(title != null) {
				setTitle(title.trim());
			}
			
			String author = (String) tagMap.get("author");
			if(author != null) {
				setAuthor(author.trim());
			}
			
			String album = (String) tagMap.get("album");
			if(album != null) {
				this.album = album.trim();
			}
			
			long duration = (long) tagMap.get("duration");
			super.duration = duration;
		} catch (Exception e) {
			throw new NotPlayableException(getPathname(), e);
		}
	}
	
	public String toString() {
		String album = getAlbum();
		String album_str = (album != "" && album != null) ? album + " - " : "";
		
		return super.toString() + " - " + album_str + formatDuration();
	}
}