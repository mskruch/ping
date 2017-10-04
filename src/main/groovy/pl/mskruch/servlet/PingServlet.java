package pl.mskruch.servlet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailServiceFactory;
import pl.mskruch.ping.check.Check;
import pl.mskruch.ping.check.Checks;
import pl.mskruch.ping.check.ChecksRoot;
import pl.mskruch.ping.service.Mailgun;
import pl.mskruch.ping.service.Pinger;
import pl.mskruch.ping.service.Result;

public class PingServlet extends HttpServlet
{
	public static final String SENDER = "noreply@czasowki-feeder.appspotmail.com";
	static Logger logger = Logger.getLogger(PingServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException
	{
		logger.fine("ping checks requested");

		ChecksRoot checks = new ChecksRoot();
		List<Check> all = checks.all();
		logger.fine(all.size() + " checks found");

		Pinger pinger = new Pinger();

		for (Check check : all) {
			Result result = pinger.ping(check.getUrl());
			logger.fine("ping " + check.getUrl() + " " + result.status());
			boolean updated = checks.update(check, result);
			if (updated) {
				logger.info(
					"status changed, sending notification, result: " + result);
				notify(check, result);
			}
		}
	}

	private void notify(Check check, Result result) throws IOException
	{
		Mailgun mailgun = new Mailgun();
		try {
			String durationString = result.elapsedInMilliseconds() != null
				? (result.elapsedInMilliseconds() / 1000) + " seconds"
				: "";
			String name = check.getName() != null ? check.getName()
				: check.getUrl();
			mailgun.send(check.getOwnerEmail(), name + " is " + result.status(),
				check.getUrl() + " is " + result.status() + "\n"
					+ "Status code: " + result.responseCode() + "\n" + "Time: "
					+ durationString + "\n" + "Details: " + result.message());
		} catch (Exception e) {
			logger.severe("unable to send notification: " + e.getMessage());
			MailServiceFactory.getMailService()
				.sendToAdmins(new MailService.Message(SENDER, null,
					"Unable to send notification", e.getMessage()));
		}
	}

}
