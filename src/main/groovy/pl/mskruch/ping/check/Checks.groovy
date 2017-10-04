package pl.mskruch.ping.check

import pl.mskruch.exception.NotFound
import pl.mskruch.ping.service.Auth

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

	Long add(String url)
	{
		def email = auth.email()
		root.create(email, url)
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
		if (patch.getNotificationDelayInMilliseconds() != null) {
			check.setNotificationDelayInMilliseconds(
					patch.getNotificationDelayInMilliseconds())
		}
		if (patch.name) {
			check.setName(patch.name)
		}
		root.save(check)
	}

	def delete(Long id)
	{
		Check check = get(id)
		root.delete(check)
	}

}
