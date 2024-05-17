package com.butter.wypl.notification.data.response;

import java.util.List;

import org.springframework.data.domain.Page;

import com.butter.wypl.notification.domain.Notification;
import com.fasterxml.jackson.annotation.JsonProperty;

public record NotificationPageResponse(
	@JsonProperty("notifications")
	List<NotificationResponse> notifications, // 알림 List
	@JsonProperty("last_id")
	String lastId, // 마지막조회 알림 ID
	@JsonProperty("total_notification_count")
	long totalNotificationCount, // 총 알림개수
	@JsonProperty("total_page_count")
	int totalPageCount, // 총 페이지수
	@JsonProperty("has_next")
	boolean hasNext, // 다음 페이지 여부
	@JsonProperty("page_size")
	int pageSize // 한 페이지 조회 건수
) {

	public static NotificationPageResponse from(Page<Notification> page) {
		// List<Notification> content = page.getContent();
		List<NotificationResponse> content = NotificationResponse.from(page.getContent());
		return new NotificationPageResponse(
			content,
    		content.isEmpty() ? null : content.get(content.size() - 1).id(),
    		page.getTotalElements(),
    		page.getTotalPages(),
    		page.hasNext(),
    		page.getSize()
    	);
	}
}
