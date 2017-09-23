package pl.mskruch;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.mskruch.data.User;

public class Ping extends HttpServlet
{
	static Logger logger = Logger.getLogger(Ping.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException
	{
		resp.setContentType("text/html");
		resp.getWriter().println("<h3>ping</h3>");
		if (req.getUserPrincipal() != null) {
			String name = req.getUserPrincipal().getName();

			logger.info("ping done by: " + name);
			// Or you can issue a query
			User fetched = ofy().load().type(User.class).filter("email", name)
				.first().now();
			if (fetched != null) {
				write(resp, "Hello again, " + fetched.email());
				logger.info("fetched: " + fetched);
			} else {
				User user = new User(name);
				ofy().save().entity(user).now(); // async without the now()
				logger.info("created" + user);
				write(resp, "Hello, " + user.email()
					+ ", your account has been created");

			}

			URL url = new URL("http://google.pl");
			BufferedReader reader = new BufferedReader(
				new InputStreamReader(url.openStream()));
			StringBuffer response = new StringBuffer();
			String line;

			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();

			write(resp, "<b>Response:</b> " + response.toString());
		}

	}

	private void write(HttpServletResponse resp, String message)
		throws IOException
	{
		resp.getWriter().println("<p>" + message + "</p>");
	}
}
