package pl.mskruch.servlet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.mskruch.data.Check;
import pl.mskruch.service.Checks;
import pl.mskruch.service.Pinger;
import pl.mskruch.service.Result;

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
			logger.info("ping " + check.getUrl() + " " + result.status());
			if (checks.update(check, result)) {
				logger.info("status changed, sending notification");
				// TODO: send notification
			}
		}

		// if (req.getUserPrincipal() != null) {
		// String name = req.getUserPrincipal().getName();
		// User fetched = ofy().load().type(User.class).filter("email", name)
		// .first().now();
		// User user = new User(name);
		// ofy().save().entity(user).now(); // async without the now()

	}
}
