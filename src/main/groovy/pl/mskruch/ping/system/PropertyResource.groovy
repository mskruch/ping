package pl.mskruch.ping.system

import groovy.transform.TupleConstructor
import groovy.util.logging.Log
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import static org.springframework.web.bind.annotation.RequestMethod.GET

@Controller
@Log
@RequestMapping(["/api/properties"])
@TupleConstructor
class PropertyResource
{
	Config config

	@RequestMapping(method = GET)
	@ResponseBody
	def list()
	{
		config.all()
	}
}
