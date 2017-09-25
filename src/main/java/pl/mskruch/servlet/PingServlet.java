package pl.mskruch.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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

				notify(check, result);
			}
		}

		// if (req.getUserPrincipal() != null) {
		// String name = req.getUserPrincipal().getName();
		// User fetched = ofy().load().type(User.class).filter("email", name)
		// .first().now();
		// User user = new User(name);
		// ofy().save().entity(user).now(); // async without the now()

	}

	private void notify(Check check, Result result)
	{
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(
				new InternetAddress("noreply@czasowki-feeder.appspotmail.com", "Ping"));
			msg.addRecipient(Message.RecipientType.TO,
				new InternetAddress(check.getOwnerEmail()));
			msg.setSubject("Ping status changed to " + result.status());
			msg.setText("Ping status changed to  " + result.status() + " for " + check.getUrl());
			Transport.send(msg);
		} catch (AddressException e) {
			logger.severe(e.getMessage());
		} catch (MessagingException e) {
			logger.severe(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			logger.severe(e.getMessage());
		}
	}
}
