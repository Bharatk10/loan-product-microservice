package com.zettamine.mpa.lpm.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.zettamine.mpa.lpm.model.ErrorResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler  {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
    	
    	HashMap<String,String> errors = new HashMap<>();
    	
    	  ex.getBindingResult().getFieldErrors().forEach(error -> {
    	        errors.put(error.getField(), error.getDefaultMessage());
    	    });
        return ResponseEntity.badRequest().body(errors);
    }

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponseDto> resourceNotFound(WebRequest request, ResourceNotFoundException ex) {
		ErrorResponseDto errResponseDto = new ErrorResponseDto();
		errResponseDto.setApiPath(request.getDescription(false));
		errResponseDto.setErrorCode(HttpStatus.BAD_REQUEST);
		errResponseDto.setErrorMessage(ex.getMessage());
		errResponseDto.setErrorTime(LocalDateTime.now());
		return new ResponseEntity<ErrorResponseDto>(errResponseDto, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<ErrorResponseDto> resourceAlreadyExists(WebRequest request, ResourceAlreadyExistsException ex) {
		ErrorResponseDto errResponseDto = new ErrorResponseDto();
		errResponseDto.setApiPath(request.getDescription(false));
		errResponseDto.setErrorCode(HttpStatus.CONFLICT);
		errResponseDto.setErrorMessage(ex.getMessage());
		errResponseDto.setErrorTime(LocalDateTime.now());
		return new ResponseEntity<ErrorResponseDto>(errResponseDto, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponseDto> httpMessageNoteReadable(WebRequest request,
			HttpMessageNotReadableException ex) {
		ErrorResponseDto errResponseDto = new ErrorResponseDto();
		errResponseDto.setApiPath(request.getDescription(false));
		errResponseDto.setErrorCode(HttpStatus.CONFLICT);
		errResponseDto.setErrorMessage(ex.getMessage());
		errResponseDto.setErrorTime(LocalDateTime.now());
		return new ResponseEntity<ErrorResponseDto>(errResponseDto, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(DataViolationException.class)
	public ResponseEntity<?> dataViolationException(DataViolationException ex) {
		 
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(MismatchFoundException.class)
	public ResponseEntity<ErrorResponseDto> httpMismatchFoundException(WebRequest request,
			MismatchFoundException ex) {
		ErrorResponseDto errResponseDto = new ErrorResponseDto();
		errResponseDto.setApiPath(request.getDescription(false));
		errResponseDto.setErrorCode(HttpStatus.BAD_REQUEST);
		errResponseDto.setErrorMessage(ex.getMessage());
		errResponseDto.setErrorTime(LocalDateTime.now());
		return new ResponseEntity<ErrorResponseDto>(errResponseDto, HttpStatus.BAD_REQUEST);
		
	}
	
	

}
