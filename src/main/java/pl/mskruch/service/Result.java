package pl.mskruch.service;

import pl.mskruch.data.Status;

public class Result
{
	private Status status;
	private Integer responseCode;
	private String message;

	public Result(Status status)
	{
		this.status = status;
	}

	public Result(Status status, int code) {
		this(status);
		this.responseCode = code;
	}

	public Result(Status status, String message) {
		this(status);
		this.message = message;
	}

	public Status status()
	{
		return status;
	}

	public Integer responseCode() {
		return responseCode;
	}

	public String message() {
		return message;
	}

	@Override
	public String toString() {
		return "Result{" + "status=" + status + ", responseCode=" + responseCode + ", message='" + message + '\'' +
				'}';
	}
}
