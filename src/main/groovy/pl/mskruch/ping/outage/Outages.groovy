package pl.mskruch.ping.outage

import groovy.transform.TupleConstructor
import groovy.util.logging.Log
import pl.mskruch.ping.check.Check
import pl.mskruch.ping.check.Checks

@Log
@TupleConstructor
class Outages
{
	final OutagesRoot root
	final Checks checks

	List<Check> find(long checkId)
	{
		Check check = checks.get(checkId)
		root.find(check.id)
	}
}
