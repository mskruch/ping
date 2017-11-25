package pl.mskruch.ping.check

import pl.mskruch.exception.NotFound
import pl.mskruch.ping.security.Auth

import javax.servlet.http.HttpServletRequest
import java.util.logging.Logger

class Checks
{
	static Logger logger = Logger.getLogger(Checks.class.getName());

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

	Check patch(Check patch)
	{
		Check check = get(patch.getId());
		if (patch.notificationDelay != null) {
			check.notificationDelay = patch.notificationDelay
		}
		if (patch.name) {
			check.name = patch.name
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
