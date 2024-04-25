
public abstract class SampledFile extends AudioFile {
	
	/*
	 - Implementiert die in AudioFile vorgegebenen abstrakten Methoden.
	 */
	
	long duration = 0;

	SampledFile() {
		
	}
	
	SampledFile(String path) {
		super(path);
	}
	
	
	public void play() {
		studiplayer.basic.BasicPlayer.play(getPathname());
	}
	
	public void togglePause() {
		studiplayer.basic.BasicPlayer.togglePause();
	}
	
	public void stop() {
		studiplayer.basic.BasicPlayer.stop();
	}
	
	public String formatDuration() {
		return "";
	}
	
	public String formatPosition() {
		return "";
	}
	
	public static String timeFormatter(long timeInMicroSeconds) {
		return "";
	}
	
	public long getDuration() {
		return duration;
	}
}
