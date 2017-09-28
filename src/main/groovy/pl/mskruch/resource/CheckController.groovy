package pl.mskruch.resource

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/checks")
class CheckController {
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    Map update(@PathVariable("id") String checkId) {
        ['id': checkId, 'foo':'bar']
    }
}
