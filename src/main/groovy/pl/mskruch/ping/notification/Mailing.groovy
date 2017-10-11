package pl.mskruch.ping.notification

import com.google.appengine.api.utils.SystemProperty
import groovy.util.logging.Log
import pl.mskruch.ping.system.Config

import javax.annotation.PostConstruct

@Log
class Mailing
{
	private String key
	private String host
	private Config config

	Mailing(Config config)
	{
		this.config = config
	}

//	@PostConstruct
//	def init()
//	{
//		key = config.get("mailgun.key");
//		host = config.get("mailgun.host");
//		if (isProduction()) {
//			if (key == null || key.isEmpty()) {
//				throw new IllegalStateException("<mailgun.key> not configured");
//			}
//			if (host == null || host.isEmpty()) {
//				throw new IllegalStateException(
//						"<mailgun.host> not configured");
//			}
//		}
//	}


	def send(String to, String subject, String body)
	{
		// ClientConfig clientConfigMail = new ClientConfig();
		// Client clientMail = ClientBuilder.newClient(clientConfigMail);
		// clientMail.register(HttpAuthenticationFeature.basic("api", key));
		// WebTarget targetMail = clientMail
		// .target("https://api.mailgun.net/v2/" + host + "/messages");
		// Form formData = new Form();
		// formData.param("from", "Ping <ping@" + host + ">");
		// formData.param("to", to);
		// formData.param("subject", subject);
		// formData.param("text", body);
		// Response response = targetMail.request().post(Entity.entity(formData,
		// MediaType.APPLICATION_FORM_URLENCODED_TYPE));
		// logger.info("Mail sent : " + response);
		key = config.get("mailgun.key");
		host = config.get("mailgun.host");
		if (isProduction()) {
			if (!key) {
				throw new IllegalStateException("<mailgun.key> not configured");
			}
			if (!host) {
				throw new IllegalStateException(
						"<mailgun.host> not configured");
			}
		}

		new Mailgun(key, host).send(to, subject, body)
	}

	private boolean isProduction()
	{
		return SystemProperty.environment
				.value() == SystemProperty.Environment.Value.Production;
	}
}
