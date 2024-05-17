package com.butter.wypl.notification.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationTypeCode {
	GROUP("Group", "그룹 알림"),
	REVIEW("Review", "회고 알림")
	;

	private final String code;
	private final String description;
}
