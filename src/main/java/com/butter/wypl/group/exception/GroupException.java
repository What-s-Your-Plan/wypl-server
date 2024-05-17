package com.butter.wypl.group.exception;

import com.butter.wypl.global.exception.CustomException;

public class GroupException extends CustomException {
	public GroupException(GroupErrorCode groupErrorCode) {
		super(groupErrorCode);
	}
}
