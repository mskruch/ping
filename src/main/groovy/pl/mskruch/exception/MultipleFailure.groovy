package pl.mskruch.exception

class MultipleFailure extends RuntimeException
{
	final List<Exception> exceptions
	private final GString message

	MultipleFailure(GString message, List<Exception> exceptions)
	{
		this.message = message
		this.exceptions = exceptions
	}

	@Override
	String getMessage() {
		return message.toString()
	}

}
