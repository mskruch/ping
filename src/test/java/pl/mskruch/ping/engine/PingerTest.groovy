package pl.mskruch.ping.engine

import pl.mskruch.exception.InvalidURL
import pl.mskruch.ping.system.Config

class PingerTest extends GroovyTestCase
{
	private config

	void setUp()
	{
		config = [getInt: { key, defValue -> defValue }] as Config
	}

	void testPing()
	{
		def pinger = new Pinger(config)

		shouldFail(InvalidURL) {
			def result = pinger.ping("foo")
		}
//		assert result.status == DOWN
//		assert result.message == 'invalid url'
	}
}
