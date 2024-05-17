package com.butter.wypl.file.exception;

import com.butter.wypl.global.exception.CustomException;

public class FileException extends CustomException {
	public FileException(FileErrorCode errorCode) {
		super(errorCode);
	}
}
