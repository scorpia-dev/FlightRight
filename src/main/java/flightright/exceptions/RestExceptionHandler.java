package flightright.exceptions;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class RestExceptionHandler {

	  @ExceptionHandler(ConstraintViolationException.class)
	  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	  ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
	    return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	  }
	
	  @ExceptionHandler(EntityNotFoundException.class)
	  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	  ResponseEntity<?> handleNotFoundException(EntityNotFoundException e) {
	    return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	  }
	  
	  @ExceptionHandler(HttpMessageNotReadableException.class)
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  ResponseEntity<?> handleBadRequest(HttpMessageNotReadableException e) {
	    return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	  }
	  
	  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
	    return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	  }

/*	  @ExceptionHandler(HttpMessageNotReadableException.class)
	  public ResponseEntity<ErrorResponse> handleMessageNotReadableException(Exception ex, WebRequest request) {
	    // some handling
	    return generateExceptionResponseEntity(errorMessage, status);
	  }*/
	  
}
