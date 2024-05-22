package studiplayer.audio;

public class NotPlayableException extends Exception {
	private String pathname;
	
	public NotPlayableException(String pathname, String msg) {
		super(msg + " " + pathname);
		this.pathname = pathname;
	}
	
	public NotPlayableException(String pathname, Throwable t) {
		super(pathname + " " + t, t);
		this.pathname = pathname;
	}
	
	public NotPlayableException(String pathname, String msg, Throwable t) {
		super(msg + " " + pathname + " " + t, t);
		this.pathname = pathname;
	}
}