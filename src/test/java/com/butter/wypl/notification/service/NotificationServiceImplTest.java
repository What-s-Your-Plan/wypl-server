package com.butter.wypl.notification.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.butter.wypl.global.annotation.MockServiceTest;
import com.butter.wypl.notification.data.NotificationTypeCode;
import com.butter.wypl.notification.data.request.NotificationCreateRequest;
import com.butter.wypl.notification.domain.Notification;
import com.butter.wypl.notification.exception.NotificationErrorCode;
import com.butter.wypl.notification.exception.NotificationException;
import com.butter.wypl.notification.fixture.NotificationFixture;
import com.butter.wypl.notification.repository.EmitterRepository;
import com.butter.wypl.notification.repository.NotificationRepository;

@MockServiceTest
class NotificationServiceImplTest {

	@Mock
	NotificationRepository notificationRepository;

	@Mock
	EmitterRepository emitterRepository;

	@InjectMocks
	NotificationServiceImpl notificationService;

	private static Logger logger;

	@BeforeEach
	void setLogger() {
		System.setProperty("log4j.configurationFile", "log4j2-dev.xml");

		logger = LogManager.getLogger();
	}

	@DisplayName("알림 생성 테스트")
	@ParameterizedTest()
	@EnumSource(NotificationFixture.class)
	void createNotification(NotificationFixture notificationFixture) {
		//given
		NotificationCreateRequest request = notificationFixture.toNotificationCreateRequest();
		given(notificationRepository.save(any(Notification.class)))
			.willReturn(notificationFixture.toNotification());
		//when
		//then
		assertThatCode(() -> notificationService.createNotification(request)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("회원 알림 전체 삭제")
	void deleteNotification() {
		//given
		int memberId = 1;
		//when
		willDoNothing().given(notificationRepository)
			.deleteByMemberId(anyInt());

		//then
		assertThatCode(() -> notificationService.deleteNotification(memberId)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("회원 알림 전체 읽음 처리")
	void readNotificationTest() {
		//given
		int memberId = 1;
		List<Notification> notifications = makeDummyNotification(memberId);

		given(notificationRepository.findAllByMemberId(anyInt()))
			.willReturn(notifications);

		//when
		//then
		assertThatCode(() -> notificationService.readNotification(memberId)).doesNotThrowAnyException();
	}

	List<Notification> makeDummyNotification(final int memberId) {
		return Stream.iterate(0, i -> i < 5, i -> i + 1)
			.map(i -> Notification.builder()
				.id(String.valueOf(i))
				.memberId(memberId)
				.message("테스트메시지" + i)
				.isRead(false)
				.build())
			.toList();
	}

	@Test
	public void updateIsActedToTrueTest() {
		//given
		final String notificationId = "abcdefg";
		Notification notification = Notification.builder()
			.id(notificationId)
			.memberId(1)
			.isActed(false)
			.typeCode(NotificationTypeCode.GROUP)
			.message("테스트")
			.build();

		given(notificationRepository.findById(notificationId))
			.willReturn(Optional.of(notification));

		//when
		notificationService.updateIsActedToTrue(notification.getMemberId(), notificationId);

		//then
		verify(notificationRepository, times(1)).findById(notificationId);
		assertThat(notification.getIsActed()).isTrue();
		verify(notificationRepository, times(1)).save(notification);
	}

	@Test
	public void whenUpdateIsActedToTrue_invalid_Id() {
		//given
		final String invalidId = "INVALID";

		//when
		given(notificationRepository.findById(invalidId))
			.willReturn(Optional.empty());

		//then
		assertThatThrownBy(() -> notificationService.updateIsActedToTrue(1, invalidId))
			.isInstanceOf(NotificationException.class)
			.hasMessage(NotificationErrorCode.NOTIFICATION_NOT_EXIST.getMessage());
	}

	@Test
	public void whenUpdateIsActedToTrue_invalid_member() {
		//given
		final int memberId = 1;
		String notificationId = "test";
		Notification notification = Notification.builder()
			.id(notificationId)
			.memberId(2)
			.isActed(false)
			.typeCode(NotificationTypeCode.GROUP)
			.message("테스트")
			.build();

		//when
		given(notificationRepository.findById(anyString()))
			.willReturn(Optional.of(notification));

		//then
		assertThatThrownBy(() -> notificationService.updateIsActedToTrue(memberId, notificationId))
			.isInstanceOf(NotificationException.class)
			.hasMessage(NotificationErrorCode.NOT_YOUR_NOTIFICATION.getMessage());
	}
}