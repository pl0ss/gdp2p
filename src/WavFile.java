import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile {
	
	/*
	 - Liest Dateieigenschaften aus der Metainformation der Audiodatei.
	 */

	WavFile() {
		
	}
	
	WavFile(String path) {
		super(path);
		readAndSetDurationFromFile();
	}
	
	
	public void readAndSetDurationFromFile() {
		WavParamReader.readParams(getPathname());
		float frameRate = WavParamReader.getFrameRate();
		long numberOfFrames = WavParamReader.getNumberOfFrames();
		
		setDuration(computeDuration(numberOfFrames, frameRate));
	}
	
	public String toString() {
		return super.toString() + " - " + formatDuration();
	}
	
	public static long computeDuration(long numberOfFrames, float frameRate) {
		long duration = (long) (((double) numberOfFrames) / frameRate * 1_000_000); // 1sec = 1.000.000 micro sec
		return duration;
	}
}
