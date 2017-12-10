package pl.mskruch.ping.check

import groovy.transform.TupleConstructor
import groovy.util.logging.Log
import pl.mskruch.exception.NotFound
import pl.mskruch.ping.security.Auth

@Log
@TupleConstructor
class Checks
{
	final Auth auth
	final ChecksRoot root

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
