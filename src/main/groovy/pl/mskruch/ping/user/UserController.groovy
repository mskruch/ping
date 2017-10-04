package pl.mskruch.ping.user

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import pl.mskruch.exception.BadRequest
import pl.mskruch.ping.check.ChecksRoot

import static java.util.logging.Logger.getLogger
import static org.springframework.web.bind.annotation.RequestMethod.DELETE
import static org.springframework.web.bind.annotation.RequestMethod.GET

@Controller
@RequestMapping("/admin/users")
class UserController {
    static def logger = getLogger(UserController.class.getName());

    ChecksRoot checks;
    Users users;

    UserController(ChecksRoot checks, Users users) {
        this.checks = checks
        this.users = users
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    @ResponseBody
    delete(@PathVariable("id") Long id) {
        def user = users.get(id)
        def email = user.getEmail()

        if (checks.ownedBy(email).size() > 0){
            throw new BadRequest("user has checks")
        }
        users.delete(id)
    }

    // TODO: change to PATCH
    @RequestMapping(value = "/{id}", method = GET)
    switchEnabled(@PathVariable("id") Long id)
    {
        users.switchEnabled(id);
        "redirect:/admin"
    }

}
