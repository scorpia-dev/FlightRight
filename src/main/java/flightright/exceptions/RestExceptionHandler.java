package flightright.exceptions;

import java.util.ArrayList;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
		ArrayList<String> errors = new ArrayList<String>();
		for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
			errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": "
					+ violation.getMessage());
		}
		return new ResponseEntity<>("not valid due to validation error: " + errors, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	ResponseEntity<?> handleNotFoundException(EntityNotFoundException e) {
		return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ResponseEntity<?> handleBadRequest(HttpMessageNotReadableException e) {
		return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
		String error = e.getName() + " should be of type " + e.getRequiredType().getName();
		return new ResponseEntity<>("not valid due to validation error: " + error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		final StringBuffer errors = new StringBuffer();
		for (final FieldError error : e.getBindingResult().getFieldErrors()) {
			errors.append(error.getField() + ": " + error.getDefaultMessage());
		}
		return new ResponseEntity<>("not valid due to validation error: " + errors.toString(), HttpStatus.BAD_REQUEST);
	}

}
