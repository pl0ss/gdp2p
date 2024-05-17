package studiplayer.audio;

public class NotPlayableException extends Exception {
	private String pathname;
	
	public NotPlayableException(String pathname, String msg) {
		super();
		this.pathname = pathname;
		//! msg?
	}
	
	public NotPlayableException(String pathname, Throwable t) {
		super();
		this.pathname = pathname;
		//! t?
	}
	
	public NotPlayableException(String pathname, String msg, Throwable t) {
		super();
		this.pathname = pathname;
		//! msg & t?
	}
}