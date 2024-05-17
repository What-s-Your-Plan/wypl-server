package com.butter.wypl.notification.fixture;

import com.butter.wypl.notification.data.NotificationTypeCode;
import com.butter.wypl.notification.data.request.NotificationCreateRequest;
import com.butter.wypl.notification.domain.Notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationFixture {
	REVIEW_NOTI("abcdefg1",1, "모코코님 운동 일정은 어떠셨나요?", false,
		NotificationTypeCode.REVIEW),
	REVIEW_READ_NOTI("abcdefg2",1, "모코코님 운동 일정은 어떠셨나요?", true,
		NotificationTypeCode.REVIEW),
	GROUP_NOTI("abcdefg3",1, "A602 팀 초대가 왔어요", true, NotificationTypeCode.GROUP);

	private final String id;
	private final int memberId;
	private final String message;
	private final boolean isRead;
	private final NotificationTypeCode typeCode;

	public Notification toNotification() {
		return Notification.builder()
			.id(id)
			.memberId(memberId)
			.message(message)
			.isRead(isRead)
			.typeCode(typeCode)
			.build();
	}

	public NotificationCreateRequest toNotificationCreateRequest() {
		return new NotificationCreateRequest(memberId, "하이", "일정", "A602", typeCode, 1);
	}
}
