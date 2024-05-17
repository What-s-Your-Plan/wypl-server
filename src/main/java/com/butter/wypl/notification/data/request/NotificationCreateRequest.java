package com.butter.wypl.notification.data.request;

import com.butter.wypl.notification.data.NotificationTypeCode;

public record NotificationCreateRequest(
	int memberId,
	String nickName,
	String scheduleTitle,
	String groupName,
	NotificationTypeCode typeCode,
	int targetId
) {
	public boolean typeCodeEquals(NotificationTypeCode notificationTypeCode) {
		return typeCode.name().equals(notificationTypeCode.name());
	}

	public static NotificationCreateRequest makeGroupNotification(int memberId, String nickName, String groupName, int groupId) {
		return new NotificationCreateRequest(memberId, nickName, null, groupName, NotificationTypeCode.GROUP, groupId);
	}

	public static NotificationCreateRequest makeReviewNotification(int memberId, String nickName, String scheduleTitle,
		int scheduleId) {

		return new NotificationCreateRequest(memberId, nickName, scheduleTitle, null, NotificationTypeCode.REVIEW,
			scheduleId);
	}
}
