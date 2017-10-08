package pl.mskruch.ping.engine

import com.google.appengine.api.mail.MailService
import com.google.appengine.api.mail.MailServiceFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import pl.mskruch.ping.check.Check
import pl.mskruch.ping.check.ChecksRoot
import pl.mskruch.ping.service.Mailgun
import pl.mskruch.ping.service.Pinger
import pl.mskruch.ping.service.Result

import java.util.logging.Logger

import static org.springframework.web.bind.annotation.RequestMethod.GET

@Controller
class PingController
{
	public static
	final String SENDER = "noreply@czasowki-feeder.appspotmail.com";

	static Logger logger = Logger.getLogger(PingController.class.getName());

	ChecksRoot checks

	PingController(ChecksRoot checks)
	{
		this.checks = checks
	}

	@RequestMapping(value = "/ping/{id}", method = GET)
	@ResponseBody
	def processSingleCheck(@PathVariable("id") Long id)
	{
		logger.info("processing " + id)

		def check = checks.get(id)

		Pinger pinger = new Pinger();
		Result result = pinger.ping(check.getUrl());
		logger.fine("ping " + check.getUrl() + " " + result.status());
		boolean updated = checks.update(check.id, result)
		if (updated) {
			logger.info(
					"status changed, sending notification, result: " + result);
			notify(check, result);
		}
	}

	private void notify(Check check, Result result) throws IOException
	{
		Mailgun mailgun = new Mailgun();
		try {
			String durationString = result.elapsedInMilliseconds() != null ? (result.elapsedInMilliseconds() / 1000) + " seconds" : "";
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
