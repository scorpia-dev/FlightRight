package flightright.exceptions;

public abstract class Exception extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String msg;

	public Exception(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public static class ValidationException extends Exception {
		private static final long serialVersionUID = 1L;

		public ValidationException(String msg) {
			super(msg);
		}
	}

	public static class NotFoundException extends Exception {
		private static final long serialVersionUID = 1L;

		public NotFoundException(String msg) {
			super(msg);
		}

	}

}
