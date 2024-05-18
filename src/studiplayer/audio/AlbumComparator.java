package studiplayer.audio;

import java.util.Comparator;

public class AlbumComparator implements Comparator<AudioFile> {

	public int compare(AudioFile a1, AudioFile a2) {
		String alb1 = ""; 
		if(a1 instanceof TaggedFile) {
			alb1 = ((TaggedFile) a1).getAlbum();
		}
		
		String alb2 = ""; 
		if(a2 instanceof TaggedFile) {
			alb2 = ((TaggedFile) a2).getAlbum();
		}
		
		return alb1.compareTo(alb2);
	}
}
