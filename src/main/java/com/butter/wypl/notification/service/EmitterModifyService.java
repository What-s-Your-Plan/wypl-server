package com.butter.wypl.notification.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterModifyService {
	SseEmitter subscribeNotification(final int memberId, final String lastEventId);

	void sendEmitter(
		final SseEmitter emitter,
		final String eventId,
		final String emitterId,
		final Object object);
}
