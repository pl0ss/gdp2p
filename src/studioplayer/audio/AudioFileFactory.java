package studioplayer.audio;


import java.util.Arrays;

public class AudioFileFactory {
	
	public static AudioFile createAudioFile(String path) {
		if(path == null) {
			return null;
		}
		
		String[] pathParts = path.split("\\."); // nur "." ist ein regulärer Ausdruck
		String extension = pathParts[pathParts.length -1].toLowerCase();
		
		String[] wavExtensions = {"wav"};
		if(Arrays.asList(wavExtensions).contains(extension)) {
			return new WavFile(path);
		}
		
		String[] taggedExtensions = {"ogg", "mp3"};
		if(Arrays.asList(taggedExtensions).contains(extension)) {
			return new TaggedFile(path);
		}
		
		throw new RuntimeException("Unknown suffix for AudioFile \"" + path + "\"");
	}
}
