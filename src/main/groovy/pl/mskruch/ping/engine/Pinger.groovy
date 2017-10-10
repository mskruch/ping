package pl.mskruch.ping.engine

import pl.mskruch.exception.MultipleFailure
import pl.mskruch.ping.service.Config

import java.util.logging.Logger

import static java.lang.System.currentTimeMillis
import static pl.mskruch.exception.Retry.retry
import static pl.mskruch.ping.check.Status.DOWN
import static pl.mskruch.ping.check.Status.UP

class Pinger
{
	static Logger logger = Logger.getLogger(Pinger.class.getName());

	private Config config = new Config();

	Result ping(String urlString) throws IOException
	{
		long start = currentTimeMillis();
		return pingInternal(urlString)
				.inMilliseconds(currentTimeMillis() - start);

	}

	private Result pingInternal(String urlString)
	{
		try {
			int connectTimeout = config.getInt("http.connectTimeout", 10000);
			int readTimeout = config.getInt("http.readTimeout", 30000);

			URL url = new URL(urlString);

			retry(3, { sleep(1000) }) {

				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setConnectTimeout(connectTimeout);
				connection.setReadTimeout(readTimeout);
				connection.setRequestMethod("GET");
				connection.connect();

				int code = connection.getResponseCode();
				if (code >= 200 && code < 300) {
					return new Result(UP, code);
				} else {
					return new Result(DOWN, code);
				}
			} as Result

		} catch (MultipleFailure e) {
			def message = e.message + " " + e.exceptions.collect {
				"${it.class}:${it.message}"
			}.join(', ')
			return new Result(DOWN, message);
		} catch (MalformedURLException e) {
			return new Result(DOWN);
		}
	}
}