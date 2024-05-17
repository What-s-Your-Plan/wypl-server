package com.butter.wypl.member.exception;

import com.butter.wypl.global.exception.CustomException;

public class MemberException extends CustomException {
	public MemberException(MemberErrorCode errorCode) {
		super(errorCode);
	}
}
