package visorus.bss;

public class MapException extends RuntimeException {

	private static final long serialVersionUID = -1981321987613219875L;
	private Throwable cause;

	public MapException(String message) {
		super(message);
	}

	public MapException(Throwable t) {
		super(t.getMessage());
		this.cause = t;
	}
	public MapException(String message, Throwable t) {
		super(message);
		this.cause = t;
	}

	@Override
	public Throwable getCause() {
		return cause;
	}
}
