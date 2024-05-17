package com.butter.wypl.notification.service;

public interface ReviewNotificationService {

	void createReviewNotification(int memberId, String nickName, String scheduleTitle, int scheduleId);
}
