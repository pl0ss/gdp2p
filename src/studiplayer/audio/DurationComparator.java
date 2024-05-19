package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile> {
	
	public int compare(AudioFile a1, AudioFile a2) {
		Long d1 = (long) 0; 
		if(a1 instanceof SampledFile) {
			d1 = ((SampledFile) a1).getDuration();
		}
		
		Long d2 = (long) 0; 
		if(a2 instanceof SampledFile) {
			d2 = ((SampledFile) a2).getDuration();
		}
		
		return d1.compareTo(d2);
	}

}
