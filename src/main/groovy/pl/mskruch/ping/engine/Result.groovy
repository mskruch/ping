package pl.mskruch.ping.engine;

import groovy.transform.ToString;
import pl.mskruch.ping.check.Status;

@ToString
public class Result
{
	Status status;
	Integer responseCode;
	String message;
	Long elapsedInMilliseconds;

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

}
