package pl.mskruch.service;

import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailServiceFactory;
import pl.mskruch.servlet.ManageServlet;

import java.io.IOException;
import java.util.logging.Logger;

public class AdminMail
{
	static Logger logger = Logger.getLogger(AdminMail.class.getName());

	public static final String SENDER = "noreply@czasowki-feeder.appspotmail.com";

	public void notify(String subject, String body)
	{
		try {
			MailServiceFactory.getMailService().sendToAdmins(
				new MailService.Message(SENDER, null, subject, body));
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}
}
