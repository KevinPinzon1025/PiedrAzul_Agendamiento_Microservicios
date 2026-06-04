package co.unicauca.adapter.in.web;

import co.unicauca.application.dto.ErrorResponse;
import co.unicauca.domain.exception.DomainException;
import co.unicauca.domain.exception.ProfessionalNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(ProfessionalNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ProfessionalNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler({DomainException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }
}
