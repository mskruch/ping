package pl.mskruch.ping.check

import groovy.transform.TupleConstructor
import pl.mskruch.ping.system.Config

@TupleConstructor
class ChecksConfig
{
	final Config config

	int getLimit()
	{
		config.getAsInt('checks.defaultLimit', 8)
	}
}
