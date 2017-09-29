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

import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailServiceFactory;
import pl.mskruch.data.Check;
import pl.mskruch.service.Checks;
import pl.mskruch.service.Mailgun;
import pl.mskruch.service.Pinger;
import pl.mskruch.service.Result;

public class PingServlet extends HttpServlet
{
	public static final String SENDER = "noreply@czasowki-feeder.appspotmail.com";
	static Logger logger = Logger.getLogger(PingServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException
	{
		logger.fine("ping checks requested");

		Checks checks = new Checks(req);
		List<Check> all = checks.all();
		logger.fine(all.size() + " checks found");

		Pinger pinger = new Pinger();
		Mailgun mailgun = new Mailgun();
		for (Check check : all) {
			Result result = pinger.ping(check.getUrl());
			logger.fine("ping " + check.getUrl() + " " + result.status());
			if (checks.update(check, result)) {
				logger.info("status changed, sending notification, result: " + result);

				try {
					// notify(check, result);
					String durationString = result.elapsedInMilliseconds() != null
						? (result.elapsedInMilliseconds() / 1000) + " seconds"
						: "";
					mailgun.send(check.getOwnerEmail(),
						check.getUrl() + " is " + result.status(),
						check.getUrl() + " is " + result.status() + "\n"
							+ "Status code: " + result.responseCode() + "\n"
							+ "Time: " + durationString + "\n"
							+ "Details: " + result.message());
				} catch (Exception e) {
					logger.severe(
						"unable to send notification: " + e.getMessage());
					MailServiceFactory.getMailService()
						.sendToAdmins(new MailService.Message(SENDER, null,
							"Unable to send notification", e.getMessage()));
				}
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
			msg.setFrom(new InternetAddress(SENDER, "Ping"));
			msg.addRecipient(Message.RecipientType.TO,
				new InternetAddress(check.getOwnerEmail()));
			msg.setSubject("Ping status changed to " + result.status());
			msg.setText("Ping status changed to  " + result.status() + " for "
				+ check.getUrl());
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