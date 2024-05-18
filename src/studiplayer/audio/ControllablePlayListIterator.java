package studiplayer.audio;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ControllablePlayListIterator implements Iterator {
	private List<AudioFile> files;
	private int pos = 0;
	
	public ControllablePlayListIterator(List<AudioFile> files) {
		this.files = files;
	}
	
	public boolean hasNext() {
		return pos < files.size();
	}

	public AudioFile next() {
		return files.get(pos++);
	}

	public AudioFile jumpToAudioFile(AudioFile file) {
		if(files.indexOf(file) >= 0) {
			pos = files.indexOf(file); 
			pos++;
			return file;
		}
		
		return null;
	}
	
	
	public static void main(String[] args) throws NotPlayableException {
		List<AudioFile> files = Arrays.asList(
			new TaggedFile("audiofiles/Rock 812.mp3"),
			new TaggedFile("audiofiles/Eisbach Deep Snow.ogg"),
			new TaggedFile("audiofiles/wellenmeister_awakening.ogg")
		);
		
		ControllablePlayListIterator it = new ControllablePlayListIterator(files);
		
		while(it.hasNext()) {
			System.out.println(it.next());
		}
		
		it.jumpToAudioFile(files.get(1));
		while(it.hasNext()) {
			System.out.println(it.next());
		}
	}
}
