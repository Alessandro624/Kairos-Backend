package it.unical.demacs.informatica.KairosBackend.config.handler;

import io.swagger.v3.oas.annotations.Hidden;
import it.unical.demacs.informatica.KairosBackend.dto.ServiceError;
import it.unical.demacs.informatica.KairosBackend.exception.ResourceAlreadyExistsException;
import it.unical.demacs.informatica.KairosBackend.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.UnexpectedTypeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Date;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // TODO add missing exceptions and create new custom ones for them.

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServiceError defaultErrorHandler(WebRequest req, Exception ex) {
        log.warn("Unhandled exception occurred : {}", ex.getMessage());
        return errorResponse(req, ex.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServiceError onNullPointerException(WebRequest req, NullPointerException ex) {
        log.warn("Null pointer exception occurred : {}", ex.getMessage());
        return errorResponse(req, ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceError onBadRequestException(WebRequest req, BadRequestException ex) {
        log.info(ex.getMessage());
        return errorResponse(req, ex.getMessage());
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceError onUnexpectedTypeException(WebRequest req, UnexpectedTypeException ex) {
        log.info(ex.getMessage());
        return errorResponse(req, ex.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @Hidden
    public ServiceError onHttpMediaTypeException(WebRequest req, HttpMediaTypeException ex) {
        log.info(ex.getMessage());
        return errorResponse(req, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceError onIllegalArgumentException(WebRequest req, IllegalArgumentException ex) {
        log.info(ex.getMessage());
        return errorResponse(req, ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceError onIllegalStateException(WebRequest req, IllegalStateException ex) {
        log.info(ex.getMessage());
        return errorResponse(req, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceError onMethodArgumentTypeMismatchException(WebRequest req, MethodArgumentTypeMismatchException ex) {
        log.info(ex.getMessage());
        return errorResponse(req, ex.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @Hidden
    public ServiceError onNoResourceFoundException(WebRequest req, NoResourceFoundException ex) {
        log.info(ex.getMessage());
        return errorResponse(req, ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @Hidden
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServiceError onResourceNotFoundException(WebRequest req, ResourceNotFoundException ex) {
        log.info(ex.getMessage());
        return errorResponse(req, ex.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @Hidden
    @ResponseStatus(HttpStatus.CONFLICT)
    public ServiceError onResourceAlreadyExistsException(WebRequest req, ResourceAlreadyExistsException ex) {
        log.info(ex.getMessage());
        return errorResponse(req, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServiceError onMethodArgumentNotValid(WebRequest req, MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(viol -> {
                    String defaultMessage = viol.getDefaultMessage() != null ? viol.getDefaultMessage() : "Unknown error";
                    return viol.getField().concat(" : ").concat(defaultMessage);
                })
                .collect(Collectors.joining(" , "));
        log.info("Validation errors: {}", message);
        return errorResponse(req, message);
    }

    private ServiceError errorResponse(WebRequest req, String message) {
        HttpServletRequest httpReq = (HttpServletRequest) req.resolveReference(WebRequest.REFERENCE_REQUEST);
        assert httpReq != null;
        String uri = httpReq.getRequestURI();
        final ServiceError output = new ServiceError(new Date(), uri, message);
        log.error("Exception handler :::: {}", output);
        return output;
    }
}
