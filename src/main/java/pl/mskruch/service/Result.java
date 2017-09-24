package pl.mskruch.service;

import pl.mskruch.data.Status;

public class Result
{
	private Status status;

	public Result(Status status)
	{
		this.status = status;
	}

	public Status status()
	{
		return status;
	}
}
