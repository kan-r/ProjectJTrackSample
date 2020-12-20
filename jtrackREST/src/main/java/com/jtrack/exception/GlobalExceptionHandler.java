package com.jtrack.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.jtrack.dto.ErrorResp;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(InvalidDataException.class)
	public ResponseEntity<Object> handleException(InvalidDataException e){
		logger.warn("InvalidDataException -> {}", e.getMessage());
	    
	    ErrorResp errorResp = new ErrorResp();
	    errorResp.setError("invalid_data");
	    errorResp.setMessage(e.getMessage());
	        
		return ResponseEntity.badRequest().body(errorResp);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValidExceptiion(MethodArgumentNotValidException e) {
		logger.warn("MethodArgumentNotValidException -> {}", e.getMessage());
		
		StringBuilder sbErrMsg = new StringBuilder();
		e.getBindingResult().getAllErrors().forEach(err -> {
			if(sbErrMsg.length() != 0) {
				sbErrMsg.append(", ");
			}
			sbErrMsg.append(err.getDefaultMessage());
		});
		
	    ErrorResp errorResp = new ErrorResp();
	    errorResp.setError("invalid_data");
	    errorResp.setMessage(sbErrMsg.toString());
	        
		return ResponseEntity.badRequest().body(errorResp);
    }
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Object> handleRuntimeException(RuntimeException e){
		logger.error("RuntimeException -> {}", e.getMessage());
		
		e.printStackTrace();
		 
	    ErrorResp errorResp = new ErrorResp();
	    errorResp.setError("runtime_error");
	    errorResp.setMessage(ExceptionUtils.getRootCauseMessage(e));
	        
		return ResponseEntity.badRequest().body(errorResp);
	}
}
