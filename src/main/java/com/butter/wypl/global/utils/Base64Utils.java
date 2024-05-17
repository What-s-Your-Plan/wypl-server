package com.butter.wypl.global.utils;

import java.util.Base64;

import com.butter.wypl.global.annotation.Generated;
import com.butter.wypl.global.exception.CallConstructorException;

public class Base64Utils {

	@Generated
	private Base64Utils() {
		throw new CallConstructorException();
	}

	public static String decode(String base64) {
		byte[] bytes = Base64.getDecoder().decode(base64);
		return new String(bytes);
	}
}
