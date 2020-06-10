package com.vibe.utils;

@SuppressWarnings("serial")
public class FormJsonBulider extends FormJson {
	public FormJsonBulider withFail() {
		this.setSuccess(false);
		return this;
	}
	public FormJsonBulider withSuccess() {
		this.setSuccess(true);
		return this;
	}
	public FormJsonBulider withCause(String msg) {
		this.setMessage(msg);
		return this;
	}

	public static FormJsonBulider fail(String msg) {
		return new FormJsonBulider().withFail().withCause(msg);
	}
	public static FormJsonBulider success() {
		return new FormJsonBulider().withSuccess();
	}
	@Override
	public String toString() {
		return "FormJson [success=" + this.isSuccess() + ", message=" + this.getMessage() + "]";
	}
}
