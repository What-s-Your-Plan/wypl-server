package com.butter.wypl.global.exception.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

import com.butter.wypl.global.common.Message;
import com.butter.wypl.global.exception.CustomException;
import com.butter.wypl.global.exception.GlobalErrorCode;
import com.butter.wypl.global.exception.GlobalException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	protected ResponseEntity<Message<String>> internalErrorHandler(RuntimeException e, WebRequest request) {
		GlobalErrorCode errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;
		logError(e, request);
		return ResponseEntity.status(errorCode.getStatusCode())
			.body(new Message<>(errorCode.getMessage()));
	}

	@ExceptionHandler({CustomException.class})
	protected ResponseEntity<Message<String>> customExceptionHandler(CustomException e, WebRequest request) {
		logError(e, request);
		return ResponseEntity.status(e.getHttpStatus())
			.body(new Message<>(e.getMessage()));
	}

	@ExceptionHandler({GlobalException.class})
	protected ResponseEntity<Message<String>> globalExceptionHandler(GlobalException e, WebRequest request) {
		logError(e, request);
		return ResponseEntity.status(e.getHttpStatus())
			.body(new Message<>(e.getMessage()));
	}

	@ExceptionHandler({AsyncRequestTimeoutException.class})
	protected ResponseEntity<Message<String>> asyncRequestTimeoutExceptionHandler(AsyncRequestTimeoutException e,
		WebRequest request) {
		String errorMessage = "비동기 요청 Timeout";
		log.warn("\n 경고 정보 => {} \n 경고 유형 => {} \n 메시지 => {}"
			, request.getDescription(false)
			, e.getClass().getSimpleName()
			, errorMessage, e
		);

		return ResponseEntity.status(e.getStatusCode())
			.body(new Message<>(errorMessage));
	}

	private void logError(Exception e, WebRequest request) {
		log.error("\n 에러 발생 정보 => {} \n 에러유형 => {} \n 메시지 => {}"
			, request.getDescription(false)
			, e.getClass().getSimpleName()
			, e.getMessage(), e
		);
	}
}