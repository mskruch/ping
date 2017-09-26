package pl.mskruch.service;

import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import com.google.appengine.api.utils.SystemProperty;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class Mailgun
{
	static Logger logger = Logger.getLogger(Mailgun.class.getName());

	private final String key;
	private final String host;

	public Mailgun()
	{
		Config config = new Config();
		key = config.get("mailgun.key");
		host = config.get("mailgun.host");
		if (key == null || key.isEmpty()) {
			throw new IllegalStateException("<mailgun.key> not configured");
		}
		if (host == null || host.isEmpty()) {
			throw new IllegalStateException("<mailgun.host> not configured");
		}
	}

	public void send(String to, String subject, String body)
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

		if (SystemProperty.environment
			.value() != SystemProperty.Environment.Value.Production) {
			System.out.println(
				"Email not sent - not production environment: " + subject);
			return;
		}

		Client client = Client.create();
		client.addFilter(new HTTPBasicAuthFilter("api", key));

		WebResource webResource = client
			.resource("https://api.mailgun.net/v2/" + host + "/messages");

		MultivaluedMapImpl formData = new MultivaluedMapImpl();
		formData.add("from", "Ping <ping@" + host + ">");
		formData.add("to", to);
		formData.add("subject", subject);
		formData.add("html", body);

		ClientResponse clientResponse = webResource
			.type(MediaType.APPLICATION_FORM_URLENCODED)
			.post(ClientResponse.class, formData);
		String output = clientResponse.getEntity(String.class);

		logger.info("Email sent successfully : " + output);

	}
}
