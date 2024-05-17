package com.butter.wypl.schedule.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import com.butter.wypl.global.common.ControllerTest;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.schedule.data.ModificationType;
import com.butter.wypl.schedule.data.request.RepetitionRequest;
import com.butter.wypl.schedule.data.request.ScheduleCreateRequest;
import com.butter.wypl.schedule.data.request.ScheduleUpdateRequest;
import com.butter.wypl.schedule.data.response.MemberIdResponse;
import com.butter.wypl.schedule.data.response.ScheduleDetailResponse;
import com.butter.wypl.schedule.data.response.ScheduleIdListResponse;
import com.butter.wypl.schedule.data.response.ScheduleIdResponse;
import com.butter.wypl.schedule.data.response.ScheduleResponse;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.fixture.ScheduleFixture;
import com.butter.wypl.schedule.service.ScheduleModifyService;
import com.butter.wypl.schedule.service.ScheduleReadService;

public class ScheduleControllerTest extends ControllerTest {
	private final ScheduleController scheduleController;

	@MockBean
	private ScheduleModifyService scheduleModifyService;

	@MockBean
	private ScheduleReadService scheduleReadService;

	@Autowired
	public ScheduleControllerTest(ScheduleController scheduleController) {
		this.scheduleController = scheduleController;
	}

	@Test
	@DisplayName("간략 일정 조회")
	void getSchedule() throws Exception {
		//given
		Schedule schedule = ScheduleFixture.LABEL_GROUP_SCHEDUEL.toSchedule();
		given(scheduleReadService.getScheduleByScheduleId(anyInt(), anyInt()))
			.willReturn(
				ScheduleResponse.of(
					schedule,
					List.of(
						MemberFixture.JWA_SO_YEON.toMemberWithId(1),
						MemberFixture.JO_DA_MIN.toMemberWithId(2)
					)
				)
			);
		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/schedule/v1/schedules/{scheduleId}", 1)
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
		);

		//then
		resultActions.andDo(print())
			.andDo(document("schedule/getSchedule",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("scheduleId").description("스케줄 id")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
					fieldWithPath("body.schedule_id").type(JsonFieldType.NUMBER).description("조회한 일정의 인덱스"),
					fieldWithPath("body.title").type(JsonFieldType.STRING).description("조회된 일정의 제목"),
					fieldWithPath("body.description").type(JsonFieldType.STRING).optional().description("조회된 일정의 설명"),
					fieldWithPath("body.start_date").type(JsonFieldType.STRING).description("조회된 일정의 시작 날짜와 시간"),
					fieldWithPath("body.end_date").type(JsonFieldType.STRING).description("조회된 일정의 끝 날짜와 시간"),
					fieldWithPath("body.category").type(JsonFieldType.STRING).description("조회된 일정의 종류(그룹인지 개인인지)"),
					fieldWithPath("body.group_id").type(JsonFieldType.NUMBER)
						.optional()
						.description("조회된 그룹 일정의 그룹 id"),
					fieldWithPath("body.label").optional().description("조회된 일정의 라벨"),
					fieldWithPath("body.label.label_id").type(JsonFieldType.NUMBER)
						.optional()
						.description("조회된 일정의 라벨의 라벨 id"),
					fieldWithPath("body.label.member_id").type(JsonFieldType.NUMBER)
						.optional()
						.description("조회된 일정의 라벨의 주인 id"),
					fieldWithPath("body.label.title").type(JsonFieldType.STRING)
						.optional()
						.description("조회된 일정의 라벨의 재목"),
					fieldWithPath("body.label.color").type(JsonFieldType.STRING)
						.optional()
						.description("조회된 일정의 라벨의 색상"),
					fieldWithPath("body.member_count").type(JsonFieldType.NUMBER).description("조회한 일정에 속한 멤버의 수"),
					fieldWithPath("body.members[].member_id").type(JsonFieldType.NUMBER)
						.description("조회한 일정에 속한 멤버의 인덱스"),
					fieldWithPath("body.members[].nickname").type(JsonFieldType.STRING)
						.description("조회한 일정에 속한 멤버의 닉네임"),
					fieldWithPath("body.members[].profile_image").type(JsonFieldType.STRING).optional()
						.description("조회한 일정에 속한 멤버의 프로필 이미지")
				)
			))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("상세 일정 조회")
	void getDetailSchedule() throws Exception {
		//given
		Schedule schedule = ScheduleFixture.LABEL_REPEAT_PERSONAL_SCHEDULE.toSchedule();
		given(scheduleReadService.getDetailScheduleByScheduleId(anyInt(), anyInt()))
			.willReturn(
				ScheduleDetailResponse.of(
					schedule,
					List.of(
						MemberFixture.JWA_SO_YEON.toMemberWithId(1)
					)
				)
			);
		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/schedule/v1/schedules/details/{scheduleId}", 1)
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
		);

		//then
		resultActions.andDo(print())
			.andDo(document("schedule/getDetailSchedule",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("scheduleId").description("스케줄 id")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
					fieldWithPath("body.schedule_id").type(JsonFieldType.NUMBER).description("조회한 일정의 인덱스"),
					fieldWithPath("body.title").type(JsonFieldType.STRING).description("조회된 일정의 제목"),
					fieldWithPath("body.description").type(JsonFieldType.STRING).optional().description("조회된 일정의 설명"),
					fieldWithPath("body.start_date").type(JsonFieldType.STRING).description("조회된 일정의 시작 날짜와 시간"),
					fieldWithPath("body.end_date").type(JsonFieldType.STRING).description("조회된 일정의 끝 날짜와 시간"),
					fieldWithPath("body.category").type(JsonFieldType.STRING).description("조회된 일정의 종류(그룹인지 개인인지)"),
					fieldWithPath("body.group_id").type(JsonFieldType.NUMBER)
						.optional()
						.description("조회된 그룹 일정의 그룹 id"),
					fieldWithPath("body.label").optional().description("조회된 일정의 라벨"),
					fieldWithPath("body.label.label_id").type(JsonFieldType.NUMBER)
						.optional()
						.description("조회된 일정의 라벨의 라벨 id"),
					fieldWithPath("body.label.member_id").type(JsonFieldType.NUMBER)
						.optional()
						.description("조회된 일정의 라벨의 주인 id"),
					fieldWithPath("body.label.title").type(JsonFieldType.STRING)
						.optional()
						.description("조회된 일정의 라벨의 재목"),
					fieldWithPath("body.label.color").type(JsonFieldType.STRING)
						.optional()
						.description("조회된 일정의 라벨의 색상"),
					fieldWithPath("body.member_count").type(JsonFieldType.NUMBER).description("조회한 일정에 속한 멤버의 수"),
					fieldWithPath("body.members[].member_id").type(JsonFieldType.NUMBER)
						.description("조회한 일정에 속한 멤버의 인덱스"),
					fieldWithPath("body.members[].nickname").type(JsonFieldType.STRING)
						.description("조회한 일정에 속한 멤버의 닉네임"),
					fieldWithPath("body.members[].profile_image").type(JsonFieldType.STRING).optional()
						.description("조회한 일정에 속한 멤버의 프로필 이미지"),
					fieldWithPath("body.repetition").optional().description("조회된 일정의 반복 정보"),
					fieldWithPath("body.repetition.repetition_id").type(JsonFieldType.NUMBER)
						.optional()
						.description("조회된 일정의 반복 id"),
					fieldWithPath("body.repetition.repetition_cycle").type(JsonFieldType.STRING)
						.optional()
						.description("조회된 일정의 반복 종류"),
					fieldWithPath("body.repetition.repetition_start_date").type(JsonFieldType.STRING)
						.optional()
						.description("조회된 일정의 반복 시작 날짜"),
					fieldWithPath("body.repetition.repetition_end_date").type(JsonFieldType.STRING)
						.optional()
						.description("조회된 일정의 반복 끝 날짜"),
					fieldWithPath("body.repetition.day_of_week").type(JsonFieldType.NUMBER)
						.optional()
						.description("조회된 일정의 주 반복시 반복 요일"),
					fieldWithPath("body.repetition.week").type(JsonFieldType.NUMBER)
						.optional()
						.description("조회된 일정의 주 반복시 몇주만의 반복인지")
				)))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("일정 생성")
	void createSchedule() throws Exception {
		//given
		Schedule schedule = ScheduleFixture.LABEL_REPEAT_PERSONAL_SCHEDULE.toSchedule();
		String json = convertToJson(
			new ScheduleCreateRequest(
				schedule.getTitle(),
				schedule.getDescription(),
				schedule.getStartDate(),
				schedule.getEndDate(),
				schedule.getCategory(),
				schedule.getGroupId(),
				schedule.getRepetition() == null ? null : RepetitionRequest.from(schedule.getRepetition()),
				schedule.getLabel().getLabelId(),
				List.of(new MemberIdResponse(1))
			)
		);

		given(scheduleModifyService.createSchedule(anyInt(), any(ScheduleCreateRequest.class)))
			.willReturn(
				ScheduleDetailResponse.of(
					schedule,
					List.of(
						MemberFixture.JWA_SO_YEON.toMemberWithId(1)
					)
				)
			);

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/schedule/v1/schedules")
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
		);

		//then
		resultActions.andDo(print())
			.andDo(document("schedule/createSchedule",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("title").type(JsonFieldType.STRING).description("일정 제목"),
					fieldWithPath("description").type(JsonFieldType.STRING).optional().description("일정 설명"),
					fieldWithPath("start_date").type(JsonFieldType.STRING).description("일정 시작 날짜와 시간"),
					fieldWithPath("end_date").type(JsonFieldType.STRING).description("일정 끝 날짜와 시간"),
					fieldWithPath("category").type(JsonFieldType.STRING).description("일정의 종류(그룹인지 개인인지)"),
					fieldWithPath("group_id").type(JsonFieldType.NUMBER)
						.optional()
						.description("그룹 일정의 그룹 id"),
					fieldWithPath("label_id").type(JsonFieldType.NUMBER)
						.optional()
						.description("일정의 라벨의 라벨 id"),
					fieldWithPath("members[].member_id").type(JsonFieldType.NUMBER)
						.description("일정에 속한 멤버의 인덱스"),
					fieldWithPath("body.members[].profile_image").type(JsonFieldType.STRING).optional()
						.description("조회한 일정에 속한 멤버의 프로필 이미지"),
					fieldWithPath("repetition").optional().description("일정의 반복 정보"),
					fieldWithPath("repetition.repetition_cycle").type(JsonFieldType.STRING)
						.optional()
						.description("일정의 반복 종류"),
					fieldWithPath("repetition.repetition_start_date").type(JsonFieldType.STRING)
						.optional()
						.description("일정의 반복 시작 날짜"),
					fieldWithPath("repetition.repetition_end_date").type(JsonFieldType.STRING)
						.optional()
						.description("일정의 반복 끝 날짜"),
					fieldWithPath("repetition.day_of_week").type(JsonFieldType.NUMBER)
						.optional()
						.description("일정의 주 반복시 반복 요일"),
					fieldWithPath("repetition.week").type(JsonFieldType.NUMBER)
						.optional()
						.description("일정의 주 반복시 몇주만의 반복인지")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
					fieldWithPath("body.schedule_id").type(JsonFieldType.NUMBER).description("생성된 일정의 인덱스"),
					fieldWithPath("body.title").type(JsonFieldType.STRING).description("생성된 일정의 제목"),
					fieldWithPath("body.description").type(JsonFieldType.STRING).optional().description("생성된 일정의 설명"),
					fieldWithPath("body.start_date").type(JsonFieldType.STRING).description("생성된 일정의 시작 날짜와 시간"),
					fieldWithPath("body.end_date").type(JsonFieldType.STRING).description("생성된 일정의 끝 날짜와 시간"),
					fieldWithPath("body.category").type(JsonFieldType.STRING).description("생성된 일정의 종류(그룹인지 개인인지)"),
					fieldWithPath("body.group_id").type(JsonFieldType.NUMBER)
						.optional()
						.description("생성된 그룹 일정의 그룹 id"),
					fieldWithPath("body.label").optional().description("생성된 일정의 라벨"),
					fieldWithPath("body.label.label_id").type(JsonFieldType.NUMBER)
						.optional()
						.description("생성된 일정의 라벨의 라벨 id"),
					fieldWithPath("body.label.member_id").type(JsonFieldType.NUMBER)
						.optional()
						.description("생성된 일정의 라벨의 주인 id"),
					fieldWithPath("body.members[].profile_image").type(JsonFieldType.STRING).optional()
						.description("조회한 일정에 속한 멤버의 프로필 이미지"),
					fieldWithPath("body.label.title").type(JsonFieldType.STRING)
						.optional()
						.description("생성된 일정의 라벨의 재목"),
					fieldWithPath("body.label.color").type(JsonFieldType.STRING)
						.optional()
						.description("생성된 일정의 라벨의 색상"),
					fieldWithPath("body.member_count").type(JsonFieldType.NUMBER).description("생성된 일정에 속한 멤버의 수"),
					fieldWithPath("body.members[].member_id").type(JsonFieldType.NUMBER)
						.description("생성된 일정에 속한 멤버의 인덱스"),
					fieldWithPath("body.members[].nickname").type(JsonFieldType.STRING)
						.description("생성된 일정에 속한 멤버의 닉네임"),
					fieldWithPath("body.repetition").optional().description("생성된 일정의 반복 정보"),
					fieldWithPath("body.repetition.repetition_id").type(JsonFieldType.NUMBER)
						.optional()
						.description("생성된 일정의 반복 id"),
					fieldWithPath("body.repetition.repetition_cycle").type(JsonFieldType.STRING)
						.optional()
						.description("생성된 일정의 반복 종류"),
					fieldWithPath("body.repetition.repetition_start_date").type(JsonFieldType.STRING)
						.optional()
						.description("생성된 일정의 반복 시작 날짜"),
					fieldWithPath("body.repetition.repetition_end_date").type(JsonFieldType.STRING)
						.optional()
						.description("생성된 일정의 반복 끝 날짜"),
					fieldWithPath("body.repetition.day_of_week").type(JsonFieldType.NUMBER)
						.optional()
						.description("생성된 일정의 주 반복시 반복 요일"),
					fieldWithPath("body.repetition.week").type(JsonFieldType.NUMBER)
						.optional()
						.description("생성된 일정의 주 반복시 몇주만의 반복인지")
				)))
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("일정 수정")
	void updateSchedule() throws Exception {
		//given
		Schedule schedule = ScheduleFixture.LABEL_PERSONAL_SCHEDULE.toSchedule();
		String json = convertToJson(
			new ScheduleCreateRequest(
				schedule.getTitle(),
				schedule.getDescription(),
				schedule.getStartDate(),
				schedule.getEndDate(),
				schedule.getCategory(),
				schedule.getGroupId(),
				schedule.getRepetition() == null ? null : RepetitionRequest.from(schedule.getRepetition()),
				schedule.getLabel().getLabelId(),
				List.of(new MemberIdResponse(1))
			)
		);

		given(scheduleModifyService.updateSchedule(anyInt(), anyInt(), any(ScheduleUpdateRequest.class)))
			.willReturn(
				ScheduleDetailResponse.of(
					schedule,
					List.of(
						MemberFixture.JWA_SO_YEON.toMemberWithId(1)
					)
				)
			);

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/schedule/v1/schedules/{scheduleId}", 1)
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
		);

		//then
		resultActions.andDo(print())
			.andDo(document("schedule/updateSchedule",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("scheduleId").description("스케줄 id")
				),
				requestFields(
					fieldWithPath("title").type(JsonFieldType.STRING).description("일정 제목"),
					fieldWithPath("description").type(JsonFieldType.STRING).optional().description("일정 설명"),
					fieldWithPath("start_date").type(JsonFieldType.STRING).description("일정 시작 날짜와 시간"),
					fieldWithPath("end_date").type(JsonFieldType.STRING).description("일정 끝 날짜와 시간"),
					fieldWithPath("category").type(JsonFieldType.STRING).description("일정의 종류(그룹인지 개인인지)"),
					fieldWithPath("group_id").type(JsonFieldType.NUMBER)
						.optional()
						.description("그룹 일정의 그룹 id"),
					fieldWithPath("label_id").type(JsonFieldType.NUMBER)
						.optional()
						.description("일정의 라벨의 라벨 id"),
					fieldWithPath("members[].member_id").type(JsonFieldType.NUMBER)
						.description("일정에 속한 멤버의 인덱스"),
					fieldWithPath("repetition").optional().description("일정의 반복 정보"),
					fieldWithPath("repetition.repetition_cycle").type(JsonFieldType.STRING)
						.optional()
						.description("일정의 반복 종류"),
					fieldWithPath("repetition.repetition_start_date").type(JsonFieldType.STRING)
						.optional()
						.description("일정의 반복 시작 날짜"),
					fieldWithPath("repetition.repetition_end_date").type(JsonFieldType.STRING)
						.optional()
						.description("일정의 반복 끝 날짜"),
					fieldWithPath("repetition.day_of_week").type(JsonFieldType.NUMBER)
						.optional()
						.description("일정의 주 반복시 반복 요일"),
					fieldWithPath("repetition.week").type(JsonFieldType.NUMBER)
						.optional()
						.description("일정의 주 반복시 몇주만의 반복인지")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
					fieldWithPath("body.schedule_id").type(JsonFieldType.NUMBER).description("생성된 일정의 인덱스"),
					fieldWithPath("body.title").type(JsonFieldType.STRING).description("생성된 일정의 제목"),
					fieldWithPath("body.description").type(JsonFieldType.STRING).optional().description("생성된 일정의 설명"),
					fieldWithPath("body.start_date").type(JsonFieldType.STRING).description("생성된 일정의 시작 날짜와 시간"),
					fieldWithPath("body.end_date").type(JsonFieldType.STRING).description("생성된 일정의 끝 날짜와 시간"),
					fieldWithPath("body.category").type(JsonFieldType.STRING).description("생성된 일정의 종류(그룹인지 개인인지)"),
					fieldWithPath("body.group_id").type(JsonFieldType.NUMBER)
						.optional()
						.description("생성된 그룹 일정의 그룹 id"),
					fieldWithPath("body.label").optional().description("생성된 일정의 라벨"),
					fieldWithPath("body.label.label_id").type(JsonFieldType.NUMBER)
						.optional()
						.description("생성된 일정의 라벨의 라벨 id"),
					fieldWithPath("body.label.member_id").type(JsonFieldType.NUMBER)
						.optional()
						.description("생성된 일정의 라벨의 주인 id"),
					fieldWithPath("body.label.title").type(JsonFieldType.STRING)
						.optional()
						.description("생성된 일정의 라벨의 재목"),
					fieldWithPath("body.label.color").type(JsonFieldType.STRING)
						.optional()
						.description("생성된 일정의 라벨의 색상"),
					fieldWithPath("body.member_count").type(JsonFieldType.NUMBER).description("생성된 일정에 속한 멤버의 수"),
					fieldWithPath("body.members[].member_id").type(JsonFieldType.NUMBER)
						.description("생성된 일정에 속한 멤버의 인덱스"),
					fieldWithPath("body.members[].nickname").type(JsonFieldType.STRING)
						.description("생성된 일정에 속한 멤버의 닉네임"),
					fieldWithPath("body.members[].profile_image").type(JsonFieldType.STRING).optional()
						.description("조회한 일정에 속한 멤버의 프로필 이미지"),
					fieldWithPath("body.repetition").optional().description("생성된 일정의 반복 정보"),
					fieldWithPath("body.repetition.repetition_id").type(JsonFieldType.NUMBER)
						.optional()
						.description("생성된 일정의 반복 id"),
					fieldWithPath("body.repetition.repetition_cycle").type(JsonFieldType.STRING)
						.optional()
						.description("생성된 일정의 반복 종류"),
					fieldWithPath("body.repetition.repetition_start_date").type(JsonFieldType.STRING)
						.optional()
						.description("생성된 일정의 반복 시작 날짜"),
					fieldWithPath("body.repetition.repetition_end_date").type(JsonFieldType.STRING)
						.optional()
						.description("생성된 일정의 반복 끝 날짜"),
					fieldWithPath("body.repetition.day_of_week").type(JsonFieldType.NUMBER)
						.optional()
						.description("생성된 일정의 주 반복시 반복 요일"),
					fieldWithPath("body.repetition.week").type(JsonFieldType.NUMBER)
						.optional()
						.description("생성된 일정의 주 반복시 몇주만의 반복인지")
				)))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("일정 삭제")
	void deleteSchedule() throws Exception {
		//given
		given(scheduleModifyService.deleteSchedule(anyInt(), anyInt(), any(ModificationType.class)))
			.willReturn(
				ScheduleIdListResponse.from(
					List.of(
						new ScheduleIdResponse(1),
						new ScheduleIdResponse(2)
					)
				)
			);

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/schedule/v1/schedules/{scheduleId}/{modificationType}", 1,
					ModificationType.ALL)
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
		);

		//then
		resultActions.andDo(print())
			.andDo(document("label/deleteLabel",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("scheduleId").description("일정 id"),
					parameterWithName("modificationType").description("변환 타입")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
					fieldWithPath("body.schedule_count").type(JsonFieldType.NUMBER).description("삭제한 일정의 개수"),
					fieldWithPath("body.schedules[].schedule_id").type(JsonFieldType.NUMBER).description("삭제한 일정의 인덱스")
				)
			))
			.andExpect(status().isOk());
	}

}
