package pl.mskruch.ping.notification

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus

import java.util.logging.Logger

import static org.springframework.http.HttpStatus.OK
import static org.springframework.web.bind.annotation.RequestMethod.POST

@Controller
@RequestMapping("/task")
class NotificationController
{
	static Logger logger = Logger.getLogger(NotificationController.class.getName());

	Mailgun mailing

	NotificationController(Mailgun mailing)
	{
		this.mailing = mailing
	}

	@RequestMapping(value = "/mail", method = POST)
	@ResponseStatus(OK)
	sendMail(@RequestParam("to") String to, @RequestParam String subject, @RequestParam String body)
	{
		logger.info("sending email to $to")

		mailing.send(to, subject, body)

//		MailServiceFactory.getMailService()
//				.sendToAdmins(new MailService.Message(SENDER, null,
//				"Unable to send notification", e.getMessage()));
	}
}
