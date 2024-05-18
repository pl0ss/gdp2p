package studiplayer.audio;

import java.util.Comparator;

public class AuthorComparator implements Comparator<AudioFile> {

	public int compare(AudioFile a1, AudioFile a2) {
		return a1.getAuthor().compareTo(a2.getAuthor());
	}

}
