package com.butter.wypl.notification.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.butter.wypl.global.common.ControllerTest;
import com.butter.wypl.notification.data.response.NotificationPageResponse;
import com.butter.wypl.notification.data.response.NotificationResponse;
import com.butter.wypl.notification.domain.Notification;
import com.butter.wypl.notification.fixture.NotificationFixture;
import com.butter.wypl.notification.service.EmitterModifyService;
import com.butter.wypl.notification.service.NotificationLoadService;
import com.butter.wypl.notification.service.NotificationModifyService;

class NotificationControllerTest extends ControllerTest {

	private static final String URI_PATH = "/notification/v1/notifications";
	@Autowired
	private NotificationController notificationController;

	@MockBean
	private NotificationModifyService notificationModifyService;

	@MockBean
	private NotificationLoadService notificationLoadService;

	@MockBean
	private EmitterModifyService emitterModifyService;

	@Test
	@DisplayName("SSE 연결")
	void subscribe() throws Exception {
		given(emitterModifyService.subscribeNotification(anyInt(), anyString()))
			.willReturn(new SseEmitter());

		//when
		ResultActions actions = mockMvc.perform(
			RestDocumentationRequestBuilders.get(URI_PATH + "/subscribe")
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.TEXT_EVENT_STREAM)
		);

		//then
		actions.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("사용자 알림 조회")
	void getNotification() throws Exception {
		//given
		String lastId = "123124";
		List<Notification> list = new ArrayList<>();
		list.add(NotificationFixture.REVIEW_NOTI.toNotification());
		list.add(NotificationFixture.REVIEW_READ_NOTI.toNotification());
		list.add(NotificationFixture.GROUP_NOTI.toNotification());

		NotificationPageResponse response = new NotificationPageResponse(
			NotificationResponse.from(list),
			list.get(list.size() - 1).getId(),
			list.size(),
			1,
			false,
			10
		);
		given(notificationLoadService.getNotifications(anyInt(), anyString()))
			.willReturn(response);

		//when
		ResultActions actions = mockMvc.perform(
			RestDocumentationRequestBuilders.get(URI_PATH)
				.queryParam("lastId", lastId)
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
		);

		//then
		actions.andDo(print())
			.andDo(document("notification",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),

				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("body.last_id").type(JsonFieldType.STRING).description("마지막조회알림ID"),
					fieldWithPath("body.notifications[].id").type(JsonFieldType.STRING).description("알림ID"),
					fieldWithPath("body.notifications[].member_id").type(JsonFieldType.NUMBER).description("회원ID"),
					fieldWithPath("body.notifications[].message").type(JsonFieldType.STRING).description("메시지"),
					fieldWithPath("body.notifications[].is_read").type(JsonFieldType.BOOLEAN).description("알림읽음 여부"),
					fieldWithPath("body.notifications[].is_acted").type(JsonFieldType.BOOLEAN).description("회원 action 처리여부"),
					fieldWithPath("body.notifications[].type_code").type(JsonFieldType.STRING).description("알림 타입 코드"),
					fieldWithPath("body.notifications[].target_id").type(JsonFieldType.NUMBER).description("알림 대상 ID"),
					fieldWithPath("body.total_notification_count").type(JsonFieldType.NUMBER).description("총 알림개수"),
					fieldWithPath("body.total_page_count").type(JsonFieldType.NUMBER).description("총 페이지 수"),
					fieldWithPath("body.has_next").type(JsonFieldType.BOOLEAN).description("다음 페이지 여부"),
					fieldWithPath("body.page_size").type(JsonFieldType.NUMBER).description("페이지 조회 사이즈")
				)
			))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("회원 알림 전체 삭제")
	void deleteNotifications() throws Exception {
		//given
		willDoNothing()
			.given(notificationModifyService).deleteNotification(anyInt());

		//when
		ResultActions actions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete(URI_PATH)
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
		);

		//then
		actions.andDo(print())
			.andDo(document("notification/delete",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),

				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
				)
			))
			.andExpect(status().isOk());
	}

	@Test
	public void updateIsActedToTrue() throws Exception {
		//given
		willDoNothing()
			.given(notificationModifyService).updateIsActedToTrue(anyInt(), anyString());

		String notificationId = "test1234";
		//when
		ResultActions actions = mockMvc.perform(
			RestDocumentationRequestBuilders.patch(URI_PATH + "/action/{notificationId}", notificationId)
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
		);

		//then
		actions.andDo(print())
			.andDo(document("notification/update-action",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
				)
			))
			.andExpect(status().isOk());
	}
}