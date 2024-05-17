package com.butter.wypl.notification.service;

import com.butter.wypl.notification.data.response.NotificationPageResponse;

public interface NotificationLoadService {

	NotificationPageResponse getNotifications(final int memberId, final String lastId);

}
