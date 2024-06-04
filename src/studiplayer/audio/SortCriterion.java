package studiplayer.audio;

public enum SortCriterion {
	DEFAULT, AUTHOR, TITLE, ALBUM, DURATION;
	
	public String toString() {
		if(this == DEFAULT) {
			return "Standard";
		}
		
		if(this == AUTHOR) {
			return "Autor";
		}
		
		if(this == TITLE) {
			return "Titel";
		}
		
		if(this == ALBUM) {
			return "Album";
		}
		
		if(this == DURATION) {
			return "Dauer";
		}
		
		return super.toString();
	}
}