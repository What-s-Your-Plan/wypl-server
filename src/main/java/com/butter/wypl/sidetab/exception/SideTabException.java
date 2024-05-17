package com.butter.wypl.sidetab.exception;

import com.butter.wypl.global.exception.CustomException;

public class SideTabException extends CustomException {
	public SideTabException(SideTabErrorCode errorCode) {
		super(errorCode);
	}
}
