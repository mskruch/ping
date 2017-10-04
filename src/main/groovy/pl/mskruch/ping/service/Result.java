package pl.mskruch.ping.service;

import pl.mskruch.ping.check.Status;

public class Result
{
	private Status status;
	private Integer responseCode;
	private String message;
	private Long elapsedInMilliseconds;

	public Result(Status status)
	{
		this.status = status;
	}

	public Result(Status status, int code)
	{
		this(status);
		this.responseCode = code;
	}

	public Result(Status status, String message)
	{
		this(status);
		this.message = message;
	}

	public Status status()
	{
		return status;
	}

	public Integer responseCode()
	{
		return responseCode;
	}

	public String message()
	{
		return message;
	}

	public Result inMilliseconds(long elapsed)
	{
		this.elapsedInMilliseconds = elapsed;
		return this;
	}

	public Long elapsedInMilliseconds() {
		return elapsedInMilliseconds;
	}

	@Override
	public String toString() {
		return "Result{" + "status=" + status + ", responseCode=" + responseCode + ", message='" + message + '\'' + "," +
				" elapsedInMilliseconds=" + elapsedInMilliseconds + '}';
	}
}
