package pl.mskruch.ping.check

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import pl.mskruch.ping.service.Checks

import java.util.logging.Logger

import static org.springframework.web.bind.annotation.RequestMethod.*

@Controller
@RequestMapping("/checks")
class CheckResource
{
    static Logger logger = Logger.getLogger(CheckResource.class.getName());

    Checks checks;

    CheckResource(Checks checks) {
        this.checks = checks
    }

    @RequestMapping(value = "/{id}", method = PATCH)
    @ResponseBody
    Check update(@PathVariable("id") Long id, @RequestBody Check body) {
        logger.info("update check " + id + " with " + body)
        body.id = id
        checks.patch(body)
    }

    @RequestMapping(value = "/{id}", method = GET)
    @ResponseBody
    Check get(@PathVariable("id") Long id){
        checks.get(id)
    }

    @RequestMapping(value = "/", method = GET)
    @ResponseBody
    List<Check> list(){
        checks.list()
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    @ResponseBody
    delete(@PathVariable("id") Long id){
        checks.delete(id)
    }
}