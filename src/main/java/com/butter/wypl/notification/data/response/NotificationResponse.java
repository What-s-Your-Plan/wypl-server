package com.butter.wypl.notification.data.response;

import java.util.List;

import com.butter.wypl.notification.data.NotificationTypeCode;
import com.butter.wypl.notification.domain.Notification;
import com.fasterxml.jackson.annotation.JsonProperty;

public record NotificationResponse(
	@JsonProperty("id")
	String id,
	@JsonProperty("member_id")
	int memberId,
	@JsonProperty("message")
	String message,
	@JsonProperty("is_read")
	boolean isRead,
	@JsonProperty("is_acted")
	boolean isActed,
	@JsonProperty("type_code")
	NotificationTypeCode typeCode,
	@JsonProperty("target_id")
	int targetId
) {

	public static NotificationResponse from(Notification notification) {

		return new NotificationResponse(
			notification.getId(),
			notification.getMemberId(),
			notification.getMessage(),
			notification.getIsRead(),
			notification.getIsActed(),
			notification.getTypeCode(),
			notification.getTargetId()
		);
	}

	public static List<NotificationResponse> from(List<Notification> notifications) {
		return notifications.stream()
			.map(NotificationResponse::from)
			.toList();
	}
}
