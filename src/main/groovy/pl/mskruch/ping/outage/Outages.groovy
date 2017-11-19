package pl.mskruch.ping.outage

import groovy.util.logging.Log
import pl.mskruch.ping.check.Checks

@Log
class Outages
{
	private final OutagesRoot root
	private final Checks checks

	Outages(OutagesRoot root, Checks checks)
	{
		this.root = root
		this.checks = checks
	}

	def find(long checkId)
	{
		def check = checks.get(checkId)
		root.find(check.id)
	}
}
