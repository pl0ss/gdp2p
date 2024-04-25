
public class WavFile extends SampledFile {
	
	/*
	 - Liest Dateieigenschaften aus der Metainformation der Audiodatei.
	 */

	WavFile() {
		
	}
	
	WavFile(String path) {
		super(path);
	}
	
	
	public void readAndSetDurationFromFile() {
		
	}
	
	public String toString() {
		return "";
	}
	
	public static long computeDuration(long numberOfFrames, float frameRate) {
		return 0;
	}
}
