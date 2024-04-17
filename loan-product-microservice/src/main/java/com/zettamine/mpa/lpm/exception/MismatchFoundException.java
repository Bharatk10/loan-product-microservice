package com.zettamine.mpa.lpm.exception;

import org.springframework.http.HttpStatus; 
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class MismatchFoundException extends RuntimeException {

	public MismatchFoundException(String resource, String fieldName, String resourceName) {
		super(String.format("%s is mismatched with %s of resource %s", resource, fieldName, resourceName));
	}
	
	public MismatchFoundException(String message) {
		super(message);
	}
}