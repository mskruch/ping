package pl.mskruch.service;

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

	public Result ping(String urlString) throws IOException
	{
		try {
			URL url = new URL(urlString);
			long start = System.currentTimeMillis();
			HttpURLConnection connection = (HttpURLConnection) url
				.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();

			long elapsed = System.currentTimeMillis() - start;
			int code = connection.getResponseCode();
			// InputStream in = connection.getInputStream();
			// long bytes = 0;
			// while (in.read() != -1) {
			// bytes++;
			// }
			logger.fine("response status code: " + code);
			if (code >= 200 && code < 300) {
				return new Result(UP);
			} else {
				return new Result(DOWN);
			}
		} catch (MalformedURLException e) {
			return new Result(DOWN);
		} catch (SocketTimeoutException e) {
			return new Result(DOWN);
		} catch (IOException e) {
			return new Result(DOWN);
		}
	}
}
