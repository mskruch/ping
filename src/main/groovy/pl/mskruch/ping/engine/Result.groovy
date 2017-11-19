package pl.mskruch.ping.engine

import groovy.transform.ToString
import pl.mskruch.ping.check.Status

@ToString
class Result
{
	Status status
	Integer responseCode
	String message
	Long elapsedInMilliseconds

	Result(Status status)
	{
		this.status = status
	}

	Result(Status status, int code)
	{
		this(status)
		this.responseCode = code
	}

	Result(Status status, String message)
	{
		this(status)
		this.message = message
	}

	Result inMilliseconds(long elapsed)
	{
		this.elapsedInMilliseconds = elapsed
		return this
	}

}
