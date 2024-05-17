package com.butter.wypl.notification.service;

import org.springframework.stereotype.Service;

import com.butter.wypl.notification.data.request.NotificationCreateRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReviewAndGroupNotificationServiceImpl implements GroupNotificationService, ReviewNotificationService {

	private final NotificationModifyService notificationModifyService;

	@Override
	public void createGroupNotification(int memberId, String nickName, String groupName, int groupId) {
		notificationModifyService.createNotification(
			NotificationCreateRequest.makeGroupNotification(memberId,nickName, groupName, groupId)
		);
	}

	@Override
	public void createReviewNotification(int memberId, String nickName, String scheduleTitle, int scheduleId) {
		notificationModifyService.createNotification(
			NotificationCreateRequest.makeReviewNotification(memberId, nickName, scheduleTitle, scheduleId)
		);
	}
}
