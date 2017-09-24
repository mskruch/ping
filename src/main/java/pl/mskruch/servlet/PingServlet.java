package pl.mskruch.servlet;

//import org.slf4j.Logger;
import pl.mskruch.data.Check;
import pl.mskruch.service.Checks;
import pl.mskruch.service.Pinger;
import pl.mskruch.service.Result;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PingServlet extends HttpServlet
{
	 static Logger logger = Logger.getLogger(PingServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException
	{
		logger.info("ping checks requested");

		Checks checks = new Checks(req);
		List<Check> all = checks.all();
		logger.info(all.size() + " checks found");

		Pinger pinger = new Pinger();
		for (Check check : all) {
			Result result = pinger.ping(check.getUrl());
			logger.info( "ping " + check.getUrl() + " " + result.status());
			checks.update(check, result);
		}

//		if (req.getUserPrincipal() != null) {
//			String name = req.getUserPrincipal().getName();
//
//			logger.info("ping done by: " + name);
//			// Or you can issue a query
//			User fetched = ofy().load().type(User.class).filter("email", name)
//				.first().now();
//			if (fetched != null) {
//				write(resp, "Hello again, " + fetched.email());
//				logger.info("fetched: " + fetched);
//			} else {
//				User user = new User(name);
//				ofy().save().entity(user).now(); // async without the now()
//				logger.info("created" + user);
//				write(resp, "Hello, " + user.email()
//					+ ", your account has been created");
//
//			}
//
//			URL url = new URL("http://google.pl");
//			long start = System.currentTimeMillis();
//			HttpURLConnection connection = (HttpURLConnection) url
//				.openConnection();
//			connection.setRequestMethod("GET");
//			connection.connect();
//			long elapsed = System.currentTimeMillis() - start;
//
//			try {
//				int code = connection.getResponseCode();
//				write(resp, "<b>Response: </b> " + code);
//				write(resp, "<b>Time: </b> " + elapsed + " ms");
//				InputStream in = connection.getInputStream();
//				long bytes = 0;
//				while (in.read() != -1) {
//					bytes++;
//				}
//				write(resp, "<b>Bytes: </b> " + bytes);
//			} catch (SocketTimeoutException e) {
//				write(resp, "<b>Timeout: </b> " + e.getMessage());
//			} catch (IOException e) {
//				write(resp, "<b>Error: </b> " + e.getMessage());
//			}
//		}

	}
}
