package pl.mskruch.ping.outage

import groovy.transform.TupleConstructor
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import static org.springframework.web.bind.annotation.RequestMethod.GET

@Controller
@RequestMapping(["/checks", "/api/checks"])
@TupleConstructor
class OutageResource
{
	Outages outages

	@RequestMapping(value = "/{checkId}/outages", method = GET)
	@ResponseBody
	def list(@PathVariable("checkId") Long checkId)
	{
		outages.find(checkId)
	}
}
