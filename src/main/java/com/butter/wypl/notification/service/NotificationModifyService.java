package com.butter.wypl.notification.service;

import com.butter.wypl.notification.data.request.NotificationCreateRequest;

public interface NotificationModifyService {

	void createNotification(final NotificationCreateRequest notificationCreateRequest);
	void deleteNotification(final int memberId);
	void updateIsActedToTrue(final int memberId, final String id);
}
