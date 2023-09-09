package com.unitedthinkers.fly.exception;

import com.intellij.openapi.diagnostic.Logger;

/**
 * @author Kirill Zhukov
 * @company UnitedThinkers
 * @since 2023/09/03
 */
public class BetterFlyException extends RuntimeException {

	private final static Logger LOG = Logger.getInstance(BetterFlyException.class);

	private FailureCode failureCode;
	private String description;

	public enum FailureCode {
		B01("File not specified", true),
		B02("Google Sheets File Id not specified", true),
		B03("Google communication error", false),
		B04("Google security problem", false),
		B05("Parse file exception", false),
		B06("Copy file exception", false),
		;

		final String message;
		final boolean showSettings;

		FailureCode(String message, boolean showSettings) {
			this.message = message;
			this.showSettings = showSettings;
		}

		public String getMessage() {
			return message;
		}

		public boolean isShowSettings() {
			return showSettings;
		}
	}

	private BetterFlyException(Throwable originCause, FailureCode failureCode) {
		this(originCause, failureCode, null);
	}

	private BetterFlyException(FailureCode failureCode, String description) {
		this(null, failureCode, description);
	}

	private BetterFlyException(Throwable originCause, FailureCode failureCode, String description) {
		super();
		this.failureCode = failureCode;
		this.description = description;
		String message = failureCode.getMessage();
		if (description != null) {
			message += ':' + description;
		}
		log(originCause, message);
	}

	private void log(Throwable originCause, String message) {
		LOG.error(message, originCause);
	}

	public String getUiMessage() {
		String message = failureCode.getMessage();
		if (description != null) {
			message += ": " + description;
		}
		return message;
	}

	public FailureCode getFailureCode() {
		return failureCode;
	}

	public static BetterFlyException b01(String description) {
		return new BetterFlyException(FailureCode.B01, description);
	}

	public static BetterFlyException b02(String description) {
		return new BetterFlyException(FailureCode.B02, description);
	}

	public static BetterFlyException b03(Throwable originCause) {
		return new BetterFlyException(originCause, FailureCode.B03);
	}

	public static BetterFlyException b04(Throwable originCause) {
		return new BetterFlyException(originCause, FailureCode.B04);
	}

	public static BetterFlyException b05(Throwable originCause, String description) {
		return new BetterFlyException(originCause, FailureCode.B05, description);
	}

	public static BetterFlyException b06(Throwable originCause, String description) {
		return new BetterFlyException(originCause, FailureCode.B06, description);
	}
}
