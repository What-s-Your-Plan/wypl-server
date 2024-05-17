package com.butter.wypl.notification.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterRepository {

	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
	private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

	public void save(final String emitterId, final SseEmitter emitter) {
		emitters.put(emitterId, emitter);
	}

	public void saveEventCache(final String eventCacheId, final Object event) {
		eventCache.put(eventCacheId, event);
	}

	public Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId) {
		String memId = memberId + "_";

		return emitters.entrySet().stream()
			.filter(entry -> entry.getKey().startsWith(memId))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId) {
		String memId = memberId + "_";

		return eventCache.entrySet().stream()
			.filter(entry -> entry.getKey().startsWith(memId))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public void deleteByEmitterId(final String emitterId) {
		emitters.remove(emitterId);
	}
}
