package pl.mskruch.service;

import static java.lang.System.currentTimeMillis;
import static pl.mskruch.data.Status.DOWN;
import static pl.mskruch.data.Status.UP;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.logging.Logger;

public class Pinger
{
	static Logger logger = Logger.getLogger(Pinger.class.getName());

	private Config config = new Config();

	public Result ping(String urlString) throws IOException
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

			logger.fine("Using connection timeout " + connectTimeout
				+ " and read timeout " + readTimeout);

			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url
				.openConnection();
			connection.setConnectTimeout(connectTimeout);
			connection.setReadTimeout(readTimeout);
			connection.setRequestMethod("GET");
			connection.connect();

			int code = connection.getResponseCode();
			if (code >= 200 && code < 300) {
				return new Result(UP);
			} else {
				return new Result(DOWN, code);
			}
		} catch (MalformedURLException e) {
			return new Result(DOWN);
		} catch (SocketTimeoutException e) {
			return new Result(DOWN, formatException(e));
		} catch (IOException e) {
			return new Result(DOWN, formatException(e));
		}
	}

	private String formatException(Exception e)
	{
		return e.getClass() + ": " + e.getMessage();
	}
}
