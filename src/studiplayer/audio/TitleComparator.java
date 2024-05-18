package studiplayer.audio;

import java.util.Comparator;

public class TitleComparator implements Comparator<AudioFile> {

	public int compare(AudioFile a1, AudioFile a2) {
		return a1.getTitle().compareTo(a2.getTitle());
	}

}
