
public abstract class SampledFile extends AudioFile {
	
	/*
	 - Implementiert die in AudioFile vorgegebenen abstrakten Methoden.
	 */
	
	protected long duration = 0;
	

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
	
	public static String sec2time(long sec) {
		return sec2time(sec, false);
	}
	public static String sec2time(long sec, boolean minZweistellig) {
		if(sec < 0) {
			throw new RuntimeException("sec2time: time negativ" + sec);
		}
		long min = sec / 60;
		long sec_rest = sec % 60;
		if(minZweistellig) {
			return String.format("%02d:%02d", min, sec_rest);
		} else {
			return String.format("%d:%02d", min, sec_rest);
		}
	}
	
	public String formatDuration() { // zB 01:02
		long duration = getDuration() / 1_000_000;
		return sec2time(duration, true);
	}
	
	public String formatPosition() { // zB 01:02
		long pos = studiplayer.basic.BasicPlayer.getPosition();
		long sec = pos / 1_000_000;
		return sec2time(sec, true);
	}
	
	public static String timeFormatter(long timeInMicroSeconds) { // zB 01:02
		long sec = timeInMicroSeconds / 1_000_000;
		return sec2time(sec, true);
	}
	
	public long getDuration() {
		return duration;
	}
	
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	public static void main(String[] args) {

	}
}
