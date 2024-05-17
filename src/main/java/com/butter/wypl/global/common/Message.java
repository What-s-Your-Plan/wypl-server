package com.butter.wypl.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Message<T> {
	@JsonProperty("message")
	private String message;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("body")
	private T body;

	public Message() {
		this.message = null;
		this.body = null;
	}

	//메세지만 있는 경우
	public Message(final String message) {
		this.message = message;
		this.body = null;
	}

	//메세지와 데이터 모두 있는 경우
	public Message(final String message, T body) {
		this.message = message;
		this.body = body;
	}

	public static Message<Void> onlyMessage(
			final String message
	) {
		return new Message<>(message);
	}

	public static <T> Message<T> withBody(
			final String message,
			final T body
	) {
		return new Message<>(message, body);
	}
}
