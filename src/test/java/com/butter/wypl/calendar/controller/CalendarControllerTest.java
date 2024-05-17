package com.butter.wypl.calendar.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import com.butter.wypl.calendar.data.CalendarType;
import com.butter.wypl.calendar.data.response.BlockListResponse;
import com.butter.wypl.calendar.data.response.BlockResponse;
import com.butter.wypl.calendar.data.response.CalendarListResponse;
import com.butter.wypl.calendar.data.response.CalendarResponse;
import com.butter.wypl.calendar.data.response.GroupCalendarListResponse;
import com.butter.wypl.calendar.data.response.GroupCalendarResponse;
import com.butter.wypl.calendar.service.CalendarService;
import com.butter.wypl.global.common.Color;
import com.butter.wypl.global.common.ControllerTest;
import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.group.fixture.GroupFixture;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.fixture.ScheduleFixture;

public class CalendarControllerTest extends ControllerTest {

	private final CalendarController calendarController;

	@MockBean
	private CalendarService calendarService;

	@Autowired
	public CalendarControllerTest(CalendarController calendarController) {
		this.calendarController = calendarController;
	}

	@Test
	@DisplayName("일년 달력 조회")
	void getYearCalendar() throws Exception {
		// Given
		given(calendarService.getVisualization(anyInt(), any(LocalDate.class)))
				.willReturn(
						BlockListResponse.from(
								List.of(
										new BlockResponse(LocalDate.of(2024, 5, 6), 0),
										new BlockResponse(LocalDate.of(2024, 5, 7), 660),
										new BlockResponse(LocalDate.of(2024, 5, 8), 770),
										new BlockResponse(LocalDate.of(2024, 5, 9), 0)
								)
						)
				);
		// When
		ResultActions resultActions = mockMvc.perform(
				RestDocumentationRequestBuilders.get("/calendar/v1/calendars/years")
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
						.queryParam("date", "2024-05-08")
		);

		// Then
		resultActions.andDo(print())
				.andDo(document("calendar/year",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						queryParameters(
								parameterWithName("date").optional().description("기준 날짜")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
								fieldWithPath("body.block_count").type(JsonFieldType.NUMBER).description("날 개수"),
								fieldWithPath("body.blocks[].date").type(JsonFieldType.STRING).description("날짜"),
								fieldWithPath("body.blocks[].time").type(JsonFieldType.NUMBER)
										.description("해당 날짜의 총 시간")
						)
				))
				.andExpect(status().isOk());

	}

	@Nested
	@DisplayName("개인 페이지 달력 조회")
	class personalCalendar {
		@Test
		@DisplayName("개인페이지 달력 일정 조회")
		void getPersonalCalendar() throws Exception {
			// Given
			Schedule schedule1 = ScheduleFixture.LABEL_PERSONAL_SCHEDULE.toSchedule();
			Schedule schedule2 = ScheduleFixture.LABEL_GROUP_SCHEDUEL.toSchedule();

			Member member = MemberFixture.JO_DA_MIN.toMember();

			MemberGroup memberGroup = MemberGroup.of(member, GroupFixture.GROUP_STUDY.toGroup(member),
					Color.labelBlue);

			given(
					calendarService.getCalendarSchedules(anyInt(), any(CalendarType.class), any(), any()))
					.willReturn(
							CalendarListResponse.from(
									List.of(CalendarResponse.of(schedule1, null),
											CalendarResponse.of(schedule2, memberGroup))
							)
					);

			// When
			ResultActions resultActions = mockMvc.perform(
					RestDocumentationRequestBuilders.get("/calendar/v1/calendars/{type}", CalendarType.MONTH)
							.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
							.contentType(MediaType.APPLICATION_JSON)
							.queryParam("labelId", "1")
							.queryParam("date", "2024-04-08")
			);

			// Then
			resultActions.andDo(print())
					.andDo(document("calendar/personal_label_date",
							preprocessRequest(prettyPrint()),
							preprocessResponse(prettyPrint()),
							pathParameters(
									parameterWithName("type").description("캘린더 종류")
							),
							queryParameters(
									parameterWithName("labelId").optional().description("원하는 라벨 id"),
									parameterWithName("date").optional().description("기준 날짜")
							),
							responseFields(
									fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
									fieldWithPath("body.schedule_count").type(JsonFieldType.NUMBER)
											.description("일정 개수"),
									fieldWithPath("body.schedules[].schedule_id").type(JsonFieldType.NUMBER)
											.description("일정 인덱스"),
									fieldWithPath("body.schedules[].title").type(JsonFieldType.STRING)
											.description("일정 제목"),
									fieldWithPath("body.schedules[].description").type(JsonFieldType.STRING)
											.description("일정 설명"),
									fieldWithPath("body.schedules[].category").type(JsonFieldType.STRING)
											.description("일정 종류"),
									fieldWithPath("body.schedules[].start_date").type(JsonFieldType.STRING)
											.description("일정 시작 날짜, 시간"),
									fieldWithPath("body.schedules[].end_date").type(JsonFieldType.STRING)
											.description("일정 끝 날짜, 시간"),
									fieldWithPath("body.schedules[].label").optional().description("일정의 라벨"),
									fieldWithPath("body.schedules[].label.label_id").optional()
											.type(JsonFieldType.NUMBER)
											.description("일정의 라벨의 인덱스"),
									fieldWithPath("body.schedules[].label.member_id").optional()
											.type(JsonFieldType.NUMBER)
											.description("일정의 라벨의 멤버 인덱스"),
									fieldWithPath("body.schedules[].label.title").optional()
											.type(JsonFieldType.STRING)
											.description("일정의 라벨의 제목"),
									fieldWithPath("body.schedules[].label.color").optional()
											.type(JsonFieldType.STRING)
											.description("일정의 라벨의 색상"),
									fieldWithPath("body.schedules[].group").optional().description("그룹"),
									fieldWithPath("body.schedules[].group.group_id").optional()
											.type(JsonFieldType.NUMBER)
											.description("그룹의 인덱스"),
									fieldWithPath("body.schedules[].group.color").optional()
											.type(JsonFieldType.STRING)
											.description("그룹의 색상"),
									fieldWithPath("body.schedules[].group.title").optional()
											.type(JsonFieldType.STRING)
											.description("그룹의 제목"),
									fieldWithPath("body.schedules[].group.description").optional()
											.type(JsonFieldType.STRING)
											.description("그룹의 제목")
							)
					))
					.andExpect(status().isOk());

		}

		@Test
		@DisplayName("라벨 없는 개인페이지 달력 일정 조회")
		void getPersonalCalendarNoLabel() throws Exception {
			// Given
			Schedule schedule1 = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
			Schedule schedule2 = ScheduleFixture.GROUP_SCHEDUEL.toSchedule();

			Member member = MemberFixture.JO_DA_MIN.toMember();

			MemberGroup memberGroup = MemberGroup.of(member, GroupFixture.GROUP_STUDY.toGroup(member),
					Color.labelBlue);

			given(
					calendarService.getCalendarSchedules(anyInt(), any(CalendarType.class), any(), any()))
					.willReturn(
							CalendarListResponse.from(
									List.of(CalendarResponse.of(schedule1, null),
											CalendarResponse.of(schedule2, memberGroup))
							)
					);

			// When
			ResultActions resultActions = mockMvc.perform(
					RestDocumentationRequestBuilders.get("/calendar/v1/calendars/{type}", CalendarType.MONTH)
							.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
							.contentType(MediaType.APPLICATION_JSON)
							.queryParam("date", "2024-04-28")
			);

			// Then
			resultActions.andDo(print())
					.andDo(document("calendar/personal_date",
							preprocessRequest(prettyPrint()),
							preprocessResponse(prettyPrint()),
							pathParameters(
									parameterWithName("type").description("캘린더 종류")
							),
							queryParameters(
									parameterWithName("date").optional().description("기준 날짜")
							),
							responseFields(
									fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
									fieldWithPath("body.schedule_count").type(JsonFieldType.NUMBER)
											.description("일정 개수"),
									fieldWithPath("body.schedules[].schedule_id").type(JsonFieldType.NUMBER)
											.description("일정 인덱스"),
									fieldWithPath("body.schedules[].title").type(JsonFieldType.STRING)
											.description("일정 제목"),
									fieldWithPath("body.schedules[].description").type(JsonFieldType.STRING)
											.description("일정 설명"),
									fieldWithPath("body.schedules[].category").type(JsonFieldType.STRING)
											.description("일정 종류"),
									fieldWithPath("body.schedules[].start_date").type(JsonFieldType.STRING)
											.description("일정 시작 날짜, 시간"),
									fieldWithPath("body.schedules[].end_date").type(JsonFieldType.STRING)
											.description("일정 끝 날짜, 시간"),
									fieldWithPath("body.schedules[].label").optional().description("일정의 라벨"),
									fieldWithPath("body.schedules[].label.label_id").optional()
											.type(JsonFieldType.NUMBER)
											.description("일정의 라벨의 인덱스"),
									fieldWithPath("body.schedules[].label.member_id").optional()
											.type(JsonFieldType.NUMBER)
											.description("일정의 라벨의 멤버 인덱스"),
									fieldWithPath("body.schedules[].label.title").optional()
											.type(JsonFieldType.STRING)
											.description("일정의 라벨의 제목"),
									fieldWithPath("body.schedules[].label.color").optional()
											.type(JsonFieldType.STRING)
											.description("일정의 라벨의 색상"),
									fieldWithPath("body.schedules[].group").optional().description("그룹"),
									fieldWithPath("body.schedules[].group.group_id").optional()
											.type(JsonFieldType.NUMBER)
											.description("그룹의 인덱스"),
									fieldWithPath("body.schedules[].group.color").optional()
											.type(JsonFieldType.STRING)
											.description("그룹의 색상"),
									fieldWithPath("body.schedules[].group.title").optional()
											.type(JsonFieldType.STRING)
											.description("그룹의 제목"),
									fieldWithPath("body.schedules[].group.description").optional()
											.type(JsonFieldType.STRING)
											.description("그룹의 설명")
							)
					))
					.andExpect(status().isOk());

		}

		@Test
		@DisplayName("지정 날짜 없는 개인페이지 달력 일정 조회")
		void getPersonalCalendarNoDate() throws Exception {
			// Given
			Schedule schedule1 = ScheduleFixture.LABEL_PERSONAL_SCHEDULE.toSchedule();
			Schedule schedule2 = ScheduleFixture.LABEL_GROUP_SCHEDUEL.toSchedule();
			Member member = MemberFixture.JO_DA_MIN.toMember();

			MemberGroup memberGroup = MemberGroup.of(member, GroupFixture.GROUP_STUDY.toGroup(member),
					Color.labelBlue);

			given(
					calendarService.getCalendarSchedules(anyInt(), any(CalendarType.class), any(), any()))
					.willReturn(
							CalendarListResponse.from(
									List.of(CalendarResponse.of(schedule1, null),
											CalendarResponse.of(schedule2, memberGroup))
							)
					);

			// When
			ResultActions resultActions = mockMvc.perform(
					RestDocumentationRequestBuilders.get("/calendar/v1/calendars/{type}", CalendarType.MONTH)
							.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
							.contentType(MediaType.APPLICATION_JSON)
							.queryParam("labelId", "1")
			);

			// Then
			resultActions.andDo(print())
					.andDo(document("calendar/personal_label",
							preprocessRequest(prettyPrint()),
							preprocessResponse(prettyPrint()),
							pathParameters(
									parameterWithName("type").description("캘린더 종류")
							),
							queryParameters(
									parameterWithName("labelId").optional().description("라벨 id")
							),
							responseFields(
									fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
									fieldWithPath("body.schedule_count").type(JsonFieldType.NUMBER)
											.description("일정 개수"),
									fieldWithPath("body.schedules[].schedule_id").type(JsonFieldType.NUMBER)
											.description("일정 인덱스"),
									fieldWithPath("body.schedules[].title").type(JsonFieldType.STRING)
											.description("일정 제목"),
									fieldWithPath("body.schedules[].description").type(JsonFieldType.STRING)
											.description("일정 설명"),
									fieldWithPath("body.schedules[].category").type(JsonFieldType.STRING)
											.description("일정 종류"),
									fieldWithPath("body.schedules[].start_date").type(JsonFieldType.STRING)
											.description("일정 시작 날짜, 시간"),
									fieldWithPath("body.schedules[].end_date").type(JsonFieldType.STRING)
											.description("일정 끝 날짜, 시간"),
									fieldWithPath("body.schedules[].label").optional().description("일정의 라벨"),
									fieldWithPath("body.schedules[].label.label_id").optional()
											.type(JsonFieldType.NUMBER)
											.description("일정의 라벨의 인덱스"),
									fieldWithPath("body.schedules[].label.member_id").optional()
											.type(JsonFieldType.NUMBER)
											.description("일정의 라벨의 멤버 인덱스"),
									fieldWithPath("body.schedules[].label.title").optional()
											.type(JsonFieldType.STRING)
											.description("일정의 라벨의 제목"),
									fieldWithPath("body.schedules[].label.color").optional()
											.type(JsonFieldType.STRING)
											.description("일정의 라벨의 색상"),
									fieldWithPath("body.schedules[].group").optional().description("그룹"),
									fieldWithPath("body.schedules[].group.group_id").optional()
											.type(JsonFieldType.NUMBER)
											.description("그룹의 인덱스"),
									fieldWithPath("body.schedules[].group.color").optional()
											.type(JsonFieldType.STRING)
											.description("그룹의 색상"),
									fieldWithPath("body.schedules[].group.title").optional()
											.type(JsonFieldType.STRING)
											.description("그룹의 제목"),
									fieldWithPath("body.schedules[].group.description").optional()
											.type(JsonFieldType.STRING)
											.description("그룹의 설명")

							)
					))
					.andExpect(status().isOk());

		}

		@Test
		@DisplayName("라벨 없고 지정 날짜 없는 개인 페이지 달력 일정 조회")
		void getPersonalCalendarNoLabelNoDate() throws Exception {
			// Given
			Schedule schedule1 = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
			Schedule schedule2 = ScheduleFixture.GROUP_SCHEDUEL.toSchedule();

			Member member = MemberFixture.JO_DA_MIN.toMember();

			MemberGroup memberGroup = MemberGroup.of(member, GroupFixture.GROUP_STUDY.toGroup(member),
					Color.labelBlue);

			given(
					calendarService.getCalendarSchedules(anyInt(), any(CalendarType.class), any(), any()))
					.willReturn(
							CalendarListResponse.from(
									List.of(CalendarResponse.of(schedule1, null),
											CalendarResponse.of(schedule2, memberGroup))
							)
					);
			// When
			ResultActions resultActions = mockMvc.perform(
					RestDocumentationRequestBuilders.get("/calendar/v1/calendars/{type}", CalendarType.MONTH)
							.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
							.contentType(MediaType.APPLICATION_JSON)
			);

			// Then
			resultActions.andDo(print())
					.andDo(document("calendar/personal",
							preprocessRequest(prettyPrint()),
							preprocessResponse(prettyPrint()),
							pathParameters(
									parameterWithName("type").description("캘린더 종류")
							),
							responseFields(
									fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
									fieldWithPath("body.schedule_count").type(JsonFieldType.NUMBER)
											.description("일정 개수"),
									fieldWithPath("body.schedules[].schedule_id").type(JsonFieldType.NUMBER)
											.description("일정 인덱스"),
									fieldWithPath("body.schedules[].title").type(JsonFieldType.STRING)
											.description("일정 제목"),
									fieldWithPath("body.schedules[].description").type(JsonFieldType.STRING)
											.description("일정 설명"),
									fieldWithPath("body.schedules[].category").type(JsonFieldType.STRING)
											.description("일정 종류"),
									fieldWithPath("body.schedules[].start_date").type(JsonFieldType.STRING)
											.description("일정 시작 날짜, 시간"),
									fieldWithPath("body.schedules[].end_date").type(JsonFieldType.STRING)
											.description("일정 끝 날짜, 시간"),
									fieldWithPath("body.schedules[].label").optional().description("일정의 라벨"),
									fieldWithPath("body.schedules[].label.label_id").optional()
											.type(JsonFieldType.NUMBER)
											.description("일정의 라벨의 인덱스"),
									fieldWithPath("body.schedules[].label.member_id").optional()
											.type(JsonFieldType.NUMBER)
											.description("일정의 라벨의 멤버 인덱스"),
									fieldWithPath("body.schedules[].label.title").optional()
											.type(JsonFieldType.STRING)
											.description("일정의 라벨의 제목"),
									fieldWithPath("body.schedules[].label.color").optional()
											.type(JsonFieldType.STRING)
											.description("일정의 라벨의 색상"),
									fieldWithPath("body.schedules[].group").optional().description("그룹"),
									fieldWithPath("body.schedules[].group.group_id").optional()
											.type(JsonFieldType.NUMBER)
											.description("그룹의 인덱스"),
									fieldWithPath("body.schedules[].group.color").optional()
											.type(JsonFieldType.STRING)
											.description("그룹의 색상"),
									fieldWithPath("body.schedules[].group.title").optional()
											.type(JsonFieldType.STRING)
											.description("그룹의 제목"),
									fieldWithPath("body.schedules[].group.description").optional()
											.type(JsonFieldType.STRING)
											.description("그룹의 설명")

							)
					))
					.andExpect(status().isOk());

		}
	}

	@Nested
	@DisplayName("그룹 페이지 달력 조회")
	class groupCalendar {

		@Test
		@DisplayName("날짜 지정이 없는 달력 조회")
		void getGroupCalendarNoDate() throws Exception {
			// Given
			Schedule schedule1 = ScheduleFixture.LABEL_PERSONAL_SCHEDULE.toSchedule();
			Schedule schedule2 = ScheduleFixture.LABEL_GROUP_SCHEDUEL.toSchedule();

			Member member1 = MemberFixture.JWA_SO_YEON.toMember();
			Member member2 = MemberFixture.JO_DA_MIN.toMember();

			MemberGroup memberGroup = MemberGroup.of(member1, GroupFixture.GROUP_STUDY.toGroup(member1),
					Color.labelBlue);

			given(
					calendarService.getGroupCalendarSchedule(anyInt(), any(CalendarType.class), any(),
							anyInt()))
					.willReturn(
							GroupCalendarListResponse.of(
									List.of(GroupCalendarResponse.of(schedule1, List.of(member1)),
											GroupCalendarResponse.of(schedule2, List.of(member1, member2))),
									memberGroup
							)
					);

			// When
			ResultActions resultActions = mockMvc.perform(
					RestDocumentationRequestBuilders.get("/calendar/v1/calendars/{type}/{groupId}", CalendarType.MONTH,
									1)
							.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
							.contentType(MediaType.APPLICATION_JSON)
			);

			// Then
			resultActions.andDo(print())
					.andDo(document("calendar/group",
							preprocessRequest(prettyPrint()),
							preprocessResponse(prettyPrint()),
							pathParameters(
									parameterWithName("type").description("캘린더 종류"),
									parameterWithName("groupId").description("그룹 인덱스")
							),
							responseFields(
									fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
									fieldWithPath("body.schedule_count").type(JsonFieldType.NUMBER)
											.description("일정 개수"),
									fieldWithPath("body.schedules[].schedule_id").type(JsonFieldType.NUMBER)
											.description("일정 인덱스"),
									fieldWithPath("body.schedules[].title").type(JsonFieldType.STRING)
											.description("일정 제목"),
									fieldWithPath("body.schedules[].category").type(JsonFieldType.STRING)
											.description("일정 종류"),
									fieldWithPath("body.schedules[].start_date").type(JsonFieldType.STRING)
											.description("일정 시작 날짜, 시간"),
									fieldWithPath("body.schedules[].end_date").type(JsonFieldType.STRING)
											.description("일정 끝 날짜, 시간"),
									fieldWithPath("body.schedules[].member_count").type(JsonFieldType.NUMBER)
											.description("일정의 속한 멤버 수"),
									fieldWithPath("body.schedules[].members[].member_id").type(JsonFieldType.NUMBER)
											.description("일정의 속한 멤버 인덱스"),
									fieldWithPath("body.schedules[].members[].nickname").type(JsonFieldType.STRING)
											.description("일정의 속한 멤버의 닉네임"),
									fieldWithPath("body.schedules[].members[].color").type(JsonFieldType.STRING)
											.description("일정의 속한 멤버의 기본 색상"),
									fieldWithPath("body.group.group_id").type(JsonFieldType.NUMBER)
											.description("그룹의 그룹 id"),
									fieldWithPath("body.group.title").type(JsonFieldType.STRING)
											.description("그룹의 그룹 이름"),
									fieldWithPath("body.group.color").type(JsonFieldType.STRING)
											.description("그룹의 그룹 색상")
							)
					))
					.andExpect(status().isOk());

		}

		@Test
		@DisplayName("달력 조회")
		void getGroupCalendar() throws Exception {
			// Given
			Schedule schedule1 = ScheduleFixture.LABEL_PERSONAL_SCHEDULE.toSchedule();
			Schedule schedule2 = ScheduleFixture.LABEL_GROUP_SCHEDUEL.toSchedule();

			Member member1 = MemberFixture.JWA_SO_YEON.toMember();
			Member member2 = MemberFixture.JO_DA_MIN.toMember();

			MemberGroup memberGroup =
					MemberGroup.of(member1, GroupFixture.GROUP_STUDY.toGroup(member1), Color.labelBlue);

			given(calendarService.getGroupCalendarSchedule(
					anyInt(), any(CalendarType.class), any(), anyInt()))
					.willReturn(
							GroupCalendarListResponse.of(
									List.of(GroupCalendarResponse.of(schedule1, List.of(member1)),
											GroupCalendarResponse.of(schedule2, List.of(member1, member2))),
									memberGroup
							)
					);

			// When
			ResultActions resultActions = mockMvc.perform(
					RestDocumentationRequestBuilders.get("/calendar/v1/calendars/{type}/{groupId}",
									CalendarType.MONTH,
									1)
							.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
							.contentType(MediaType.APPLICATION_JSON)
							.queryParam("date", "2024-04-28")
			);

			// Then
			resultActions.andDo(print())
					.andDo(document("calendar/group_date",
							preprocessRequest(prettyPrint()),
							preprocessResponse(prettyPrint()),
							queryParameters(
									parameterWithName("date").optional().description("기준 날짜")
							),
							pathParameters(
									parameterWithName("type").description("캘린더 종류"),
									parameterWithName("groupId").description("그룹 인덱스")
							),
							responseFields(
									fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
									fieldWithPath("body.schedule_count").type(JsonFieldType.NUMBER)
											.description("일정 개수"),
									fieldWithPath("body.schedules[].schedule_id").type(JsonFieldType.NUMBER)
											.description("일정 인덱스"),
									fieldWithPath("body.schedules[].title").type(JsonFieldType.STRING)
											.description("일정 제목"),
									fieldWithPath("body.schedules[].category").type(JsonFieldType.STRING)
											.description("일정 종류"),
									fieldWithPath("body.schedules[].start_date").type(JsonFieldType.STRING)
											.description("일정 시작 날짜, 시간"),
									fieldWithPath("body.schedules[].end_date").type(JsonFieldType.STRING)
											.description("일정 끝 날짜, 시간"),
									fieldWithPath("body.schedules[].member_count").type(JsonFieldType.NUMBER)
											.description("일정의 속한 멤버 수"),
									fieldWithPath("body.schedules[].members[].member_id").type(JsonFieldType.NUMBER)
											.description("일정의 속한 멤버 인덱스"),
									fieldWithPath("body.schedules[].members[].nickname").type(JsonFieldType.STRING)
											.description("일정의 속한 멤버의 닉네임"),
									fieldWithPath("body.schedules[].members[].color").type(JsonFieldType.STRING)
											.description("일정의 속한 멤버의 기본 색상"),
									fieldWithPath("body.group.group_id").type(JsonFieldType.NUMBER)
											.description("그룹의 그룹 id"),
									fieldWithPath("body.group.title").type(JsonFieldType.STRING)
											.description("그룹의 그룹 이름"),
									fieldWithPath("body.group.color").type(JsonFieldType.STRING)
											.description("그룹의 그룹 색상")
									)
					))
					.andExpect(status().isOk());

		}
	}
}
