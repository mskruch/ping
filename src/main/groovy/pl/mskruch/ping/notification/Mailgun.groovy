package pl.mskruch.ping.notification

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.api.client.WebResource
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter
import com.sun.jersey.core.util.MultivaluedMapImpl
import com.sun.jersey.multipart.FormDataMultiPart
import com.sun.jersey.multipart.file.FileDataBodyPart
import groovy.util.logging.Log

import static javax.ws.rs.core.MediaType.*

@Log
class Mailgun
{
	private String key
	private String host

	Mailgun(String key, String host)
	{
		this.key = key
		this.host = host
	}

	def send(String to, String subject, String body)
	{
		Client client = Client.create()
		client.addFilter(new HTTPBasicAuthFilter("api", key))

		WebResource webResource = client
				.resource("https://api.mailgun.net/v2/" + host + "/messages")

		MultivaluedMapImpl formData = new MultivaluedMapImpl()
		formData.add("from", from())
		formData.add("to", to)
		formData.add("subject", subject)
		formData.add("html", body)

		ClientResponse clientResponse = webResource
				.type(APPLICATION_FORM_URLENCODED)
				.post(ClientResponse.class, formData)

		String output = clientResponse.getEntity(String.class)

		log.info("Email sent successfully : " + output)
	}

	private String from()
	{
		"Ping <ping@" + host + ">"
	}

	def sendHtml(String to, String subject, String body, List<File> files)
	{
		log.fine("sending email")
		Client client = Client.create()
		client.addFilter(new HTTPBasicAuthFilter("api", key))
		WebResource webResource = client.resource("https://api.mailgun.net/v3/$host/messages")
		FormDataMultiPart form = new FormDataMultiPart();
		form.field("from", from())
		form.field("to", to)
		form.field("subject", subject)
		form.field("html", body)

		files.each { file ->
			form.bodyPart(new FileDataBodyPart("inline", file, APPLICATION_OCTET_STREAM_TYPE))
		}
		webResource.type(MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, form)
		log.info("email sent")
	}
}
