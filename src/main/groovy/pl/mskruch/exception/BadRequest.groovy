package pl.mskruch.exception

class BadRequest extends RuntimeException
{
	BadRequest(String message)
	{
		super(message)
	}
}
