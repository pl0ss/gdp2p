package studiplayer.audio;


public abstract class SampledFile extends AudioFile {
	
	/*
	 - Implementiert die in AudioFile vorgegebenen abstrakten Methoden.
	 */
	
	protected long duration = 0;
	

	SampledFile() {
		
	}
	
	SampledFile(String path) throws NotPlayableException {
		super(path);
	}
	
	
	public void play() throws NotPlayableException {
		try {
			studiplayer.basic.BasicPlayer.play(getPathname());
		} catch (Exception e) {
			throw new NotPlayableException(getPathname(), e + " " + getPathname());
		}
	}
	
	public void togglePause() {
		studiplayer.basic.BasicPlayer.togglePause();
	}
	
	public void stop() {
		studiplayer.basic.BasicPlayer.stop();
	}
	
	public static String microSec2time(long timeInMicroSeconds) {
		return microSec2time(timeInMicroSeconds, false);
	}
	public static String microSec2time(long timeInMicroSeconds, boolean minZweistellig) {
		if(timeInMicroSeconds < 0) {
			throw new RuntimeException("microSec2time: time negativ" + timeInMicroSeconds);
		}
		long sec = timeInMicroSeconds / 1_000_000;
		return sec2time(sec, minZweistellig);
	}
	public static String sec2time(long sec) {
		return sec2time(sec, false);
	}
	public static String sec2time(long sec, boolean minZweistellig) {
		if(sec < 0) {
			throw new RuntimeException("sec2time: time negativ" + sec);
		}
		if(sec >= 100 * 60) {
			throw new RuntimeException("sec2time: time to high" + sec);
		}
		long min = sec / 60;
		long sec_rest = sec % 60;
		if(minZweistellig) {
			return String.format("%02d:%02d", min, sec_rest);
		} else {
			return String.format("%d:%02d", min, sec_rest);
		}
	}
	
	public String formatDuration() {
		long duration = getDuration();
		return microSec2time(duration, true);
	}
	
	public String formatPosition() {
		long pos = studiplayer.basic.BasicPlayer.getPosition();
		return microSec2time(pos, true);
	}
	
	public static String timeFormatter(long timeInMicroSeconds) {
		return microSec2time(timeInMicroSeconds, true);
	}
	
	public long getDuration() {
		return duration;
	}
	
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	public String toString() {
		return super.toString();
	}
	
	
	public static void main(String[] args) {

	}
}
