package pl.mskruch.ping.check

import groovy.util.logging.Log
import pl.mskruch.exception.NotFound
import pl.mskruch.ping.security.Auth

import javax.servlet.http.HttpServletRequest
import java.util.logging.Logger

@Log
class Checks
{
	private final Auth auth
	private final ChecksRoot root

	Checks(Auth auth, ChecksRoot checksRoot)
	{
		this.auth = auth
		this.root = checksRoot
	}

	/* only for servlets */

	@Deprecated
	Checks(HttpServletRequest req)
	{
		this.auth = new Auth(req)
		this.root = new ChecksRoot()
	}

	List<Check> list()
	{
		root.ownedBy(auth.email())
	}

	def create(url, name)
	{
		def email = auth.email()
		root.create(email, url, name)
	}

	Check get(Long id)
	{
		Check check = root.get(id)
		if (!auth.email().equals(check.getOwnerEmail())) {
			throw new NotFound("check", id)
		}
		return check;
	}

	Check patch(Long id, name = null, notificationDelay = null, paused = null)
	{
		Check check = get(id);
		if (notificationDelay != null) {
			check.notificationDelay = notificationDelay
		}
		if (name != null) {
			check.name = name
		}
		if (paused != null) {
			check.paused = paused
		}
		root.save(check)
	}

	def delete(Long id)
	{
		try {
			Check check = get(id)
			root.delete(check)
		} catch (NotFound e) {
			/* already deleted - fine */
		}
	}

}
