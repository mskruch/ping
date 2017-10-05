package pl.mskruch.exception

import java.util.logging.Logger

class Retry
{
	static Logger logger = Logger.getLogger(Retry.class.getName());

	def static retry(int times = 5, Closure errorHandler = { e -> logger.fine(e.message) }
					 , Closure body)
	{
		int retries = 0
		def exceptions = []
		while (retries++ < times) {
			try {
				return body.call()
			} catch (e) {
				exceptions << e
				errorHandler.call(e)
			}
		}
		throw new MultipleFailure("Failed after $times retries", exceptions)
	}
}
