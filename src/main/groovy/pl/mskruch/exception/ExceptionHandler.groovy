package pl.mskruch.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler as SpringExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest

import java.util.logging.Level
import java.util.logging.Logger

import static java.util.logging.Logger.getLogger

@ControllerAdvice
class ExceptionHandler {
    static Logger logger = getLogger(ExceptionHandler.class.getName());

    @SpringExceptionHandler(NotFound.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    handleNotFound(NotFound e, WebRequest request) {
        ['message': e.name + " not found"]
    }

    @SpringExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    safetyNet(Exception e, WebRequest request) {
        def uuid = UUID.randomUUID().toString()
        logger.log(Level.SEVERE, uuid + ': ' + e.message, e)
        ['message': "internal server error", 'id': uuid]
    }

    @SpringExceptionHandler(Unauthorized.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    unauthorized(Unauthorized e)
    {
        ['message': "unauthorized"]
    }

    @SpringExceptionHandler(Forbidden.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    forbidden(Unauthorized e)
    {
        ['message': "forbidden"]
    }

    @SpringExceptionHandler(BadRequest.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    invalid(BadRequest e)
    {
        ['message': e.getMessage()]
    }
}
