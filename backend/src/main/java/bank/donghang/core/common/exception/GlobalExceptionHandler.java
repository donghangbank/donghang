package bank.donghang.core.common.exception;

import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException methodArgumentNotValidException, HttpHeaders headers, HttpStatusCode status,
		WebRequest request) {
		log.warn(methodArgumentNotValidException.getMessage(), methodArgumentNotValidException);
		String message = Objects.requireNonNull(methodArgumentNotValidException.getBindingResult().getFieldError())
			.getDefaultMessage();
		return ResponseEntity.badRequest().body(new ExceptionResponse(ErrorCode.INVALID_REQUEST.getCode(), message));
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException badRequestException) {
		log.warn(badRequestException.getMessage(), badRequestException);
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(new ExceptionResponse(badRequestException.getCode(), badRequestException.getMessage()));
	}
}
