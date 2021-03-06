package pl.mskruch.ping.engine

import groovy.util.logging.Log
import pl.mskruch.exception.InvalidURL
import pl.mskruch.exception.MultipleFailure
import pl.mskruch.ping.system.Config

import static java.lang.System.currentTimeMillis
import static pl.mskruch.exception.Retry.retry
import static pl.mskruch.ping.check.Status.DOWN
import static pl.mskruch.ping.check.Status.UP

@Log
class Pinger
{
	Config config

	Pinger(Config config)
	{
		this.config = config
	}

	Result ping(String urlString) throws IOException
	{
		long start = currentTimeMillis();
		return pingInternal(urlString)
				.inMilliseconds(currentTimeMillis() - start);

	}

	private Result pingInternal(String urlString)
	{
		try {
			int connectTimeout = config.getAsInt("http.connectTimeout", 10000)
			int readTimeout = config.getAsInt("http.readTimeout", 30000)

			URL url = new URL(urlString)

			retry(3, { sleep(1000) }) {

				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setConnectTimeout(connectTimeout)
				connection.setReadTimeout(readTimeout)
				connection.setRequestMethod("GET")
				connection.connect()

				int code = connection.getResponseCode()
				if (code >= 200 && code < 300) {
					return new Result(UP, code)
				} else {
					return new Result(DOWN, code)
				}
			} as Result

		} catch (MultipleFailure e) {
			def last = e.exceptions.last()
			def message = last instanceof SocketTimeoutException ? 'socket timeout' : "<i>$last.message (${last.getClass()})</i>"
			return new Result(DOWN, message)
		} catch (MalformedURLException e) {
			throw new InvalidURL(urlString)
		}
	}
}