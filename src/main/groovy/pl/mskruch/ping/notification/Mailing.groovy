package pl.mskruch.ping.notification

import com.google.appengine.api.utils.SystemProperty
import groovy.util.logging.Log
import pl.mskruch.ping.system.Config

import javax.servlet.ServletContext

import static com.google.appengine.api.utils.SystemProperty.Environment.Value.Production

@Log
class Mailing
{
	private Config config
	private ServletContext servletContext

	Mailing(Config config, ServletContext servletContext)
	{
		this.servletContext = servletContext
		this.config = config
	}

	def send(String to, String subject, String body)
	{
		if (!isProduction()) {
			log.info "Email not sent (not production): $subject"
			return
		}

		mailgun().send(to, subject, body)
	}

	private Mailgun mailgun()
	{
		new Mailgun(config.get("mailgun.key"), config.get("mailgun.host"))
	}

	static private isProduction()
	{
		SystemProperty.environment.value() == Production
	}

	def sendUp(String to, String subject, String url)
	{
		if (!isProduction()) {
			log.info "Email not sent (not production): $subject"
			return
		}

		def image = new File(servletContext.getRealPath("/WEB-INF/images/up.png"))

		def body = """
			<html>
				<p>Yes! Your site is <b>UP</b></p>
				<p><img src=\"cid:up.png\"></p>
				<p><a href="$url">$url</a></p>
				<p>Yours truly,<br/>Ping</p>
			</html>
		"""

		mailgun().sendHtml(to, subject, body, [image])
	}

	def sendDown(String to, String subject, String url, String reason)
	{
		if (!isProduction()) {
			log.info "Email not sent (not production): $subject"
			return
		}

		def image = new File(servletContext.getRealPath("/WEB-INF/images/down.png"))

		def body = """
			<html>
				<p>Oh no! Your site is <b>DOWN</b></p>
				<p><img src=\"cid:down.png\"></p>
				<p>And the reason is $reason</p>
				<p><a href="$url">$url</a></p>
				<p>Yours truly,<br/>Ping</p>
			</html>
		"""

		mailgun().sendHtml(to, subject, body, [image])
	}
}
