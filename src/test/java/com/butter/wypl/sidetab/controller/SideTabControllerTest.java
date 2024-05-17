package com.butter.wypl.sidetab.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.global.common.ControllerTest;
import com.butter.wypl.global.utils.LocalDateUtils;
import com.butter.wypl.sidetab.data.request.DDayUpdateRequest;
import com.butter.wypl.sidetab.data.request.GoalUpdateRequest;
import com.butter.wypl.sidetab.data.request.MemoUpdateRequest;
import com.butter.wypl.sidetab.data.response.DDayWidgetResponse;
import com.butter.wypl.sidetab.data.response.GoalWidgetResponse;
import com.butter.wypl.sidetab.data.response.MemoWidgetResponse;
import com.butter.wypl.sidetab.fixture.WeatherFixture;
import com.butter.wypl.sidetab.service.SideTabLoadService;
import com.butter.wypl.sidetab.service.SideTabModifyService;
import com.butter.wypl.sidetab.service.WeatherWidgetService;

class SideTabControllerTest extends ControllerTest {

	@Autowired
	private SideTabController sideTabController;

	@MockBean
	private SideTabModifyService sideTabModifyService;
	@MockBean
	private SideTabLoadService sideTabLoadService;
	@MockBean
	private WeatherWidgetService weatherWidgetService;

	@DisplayName("사이드탭의 목표를 수정한다.")
	@Test
	void updateSideTabGoalTest() throws Exception {
		/* Given */
		String goalAsString = "새로운 목표";
		String json = convertToJson(new GoalUpdateRequest(goalAsString));

		given(sideTabModifyService.updateGoal(any(AuthMember.class), anyInt(), any(GoalUpdateRequest.class)))
				.willReturn(new GoalWidgetResponse(0, goalAsString));

		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/side/v1/goals/{goal_id}", 0)
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("side-tab/update-goal",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("goal_id").description("목표 식별자")
						),
						requestFields(
								fieldWithPath("content").type(JsonFieldType.STRING)
										.description("수정 요청한 목표 내용")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.goal_id").type(JsonFieldType.NUMBER)
										.description("목표 식별자"),
								fieldWithPath("body.content").type(JsonFieldType.STRING)
										.description("수정한 목표 내용")
						)
				))
				.andExpect(status().isOk());
	}

	@DisplayName("사이드탭의 목표를 조회한다.")
	@Test
	void findSideTabGoalTest() throws Exception {
		/* Given */
		String goalAsString = "사이드탭의 목표";

		given(sideTabLoadService.findGoal(any(AuthMember.class), anyInt()))
				.willReturn(new GoalWidgetResponse(0, goalAsString));

		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.get("/side/v1/goals/{goal_id}", 0)
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("side-tab/find-goal",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("goal_id").description("목표 식별자")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.goal_id").type(JsonFieldType.NUMBER)
										.description("목표 식별자"),
								fieldWithPath("body.content").type(JsonFieldType.STRING)
										.description("수정한 목표 내용")
						)
				))
				.andExpect(status().isOk());
	}

	@DisplayName("사이드탭의 디데이를 수정한다.")
	@Test
	void updateSideTabDDayTest() throws Exception {
		/* Given */
		String title = "디데이의 제목";
		String json = convertToJson(new DDayUpdateRequest(title, LocalDate.now()));

		given(sideTabModifyService.updateDDay(any(AuthMember.class), anyInt(), any(DDayUpdateRequest.class)))
				.willReturn(new DDayWidgetResponse(
						"디데이의 제목",
						"D-DAY",
						LocalDateUtils.toString(LocalDate.now()),
						LocalDate.now()));

		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/side/v1/d-day/{d_day_id}", 0)
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("side-tab/update-d-day",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("d_day_id").description("디데이 식별자")
						),
						requestFields(
								fieldWithPath("title").type(JsonFieldType.STRING)
										.description("수정 요청한 디데이 제목"),
								fieldWithPath("date").type(JsonFieldType.STRING)
										.description("수정 요청한 디데이 날짜")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.title").type(JsonFieldType.STRING)
										.description("수정한 디데이 제목"),
								fieldWithPath("body.d_day").type(JsonFieldType.STRING)
										.description("수정한 디데이"),
								fieldWithPath("body.date").type(JsonFieldType.STRING)
										.description("수정한 디데이 날짜"),
								fieldWithPath("body.local_date").type(JsonFieldType.STRING)
										.description("수정한 디데이 포멧")
						)
				))
				.andExpect(status().isOk());
	}

	@DisplayName("사이드탭의 디데이를 조회한다.")
	@Test
	void findSideTabDDayTest() throws Exception {
		/* Given */
		given(sideTabLoadService.findDDay(any(AuthMember.class), anyInt()))
				.willReturn(new DDayWidgetResponse(
						"디데이의 제목",
						"D-DAY",
						LocalDateUtils.toString(LocalDate.now()),
						LocalDate.now()));

		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.get("/side/v1/d-day/{d_day_id}", 0)
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("side-tab/find-d-day",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("d_day_id").description("디데이 식별자")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.title").type(JsonFieldType.STRING)
										.description("수정한 디데이 제목"),
								fieldWithPath("body.d_day").type(JsonFieldType.STRING)
										.description("수정한 디데이"),
								fieldWithPath("body.date").type(JsonFieldType.STRING)
										.description("수정한 디데이 날짜"),
								fieldWithPath("body.local_date").type(JsonFieldType.STRING)
										.description("수정한 디데이 포멧")
						)
				))
				.andExpect(status().isOk());
	}

	@DisplayName("사이드탭의 메모를 조회한다.")
	@Test
	void findSideTabMemoTest() throws Exception {
		/* Given */
		given(sideTabLoadService.findMemo(any(AuthMember.class), anyInt()))
				.willReturn(new MemoWidgetResponse("메모의 내용"));

		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.get("/side/v1/memo/{memo_id}", 0)
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("side-tab/find-memo",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("memo_id").description("메모 식별자")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.memo").type(JsonFieldType.STRING)
										.description("조회한 메모")
						)
				))
				.andExpect(status().isOk());
	}

	@DisplayName("사이드탭의 메모를 수정한다.")
	@Test
	void updateSideTabMemoTest() throws Exception {
		/* Given */
		String memo = "메모의 내용";
		String json = convertToJson(new MemoUpdateRequest(memo));

		given(sideTabModifyService.updateMemo(any(AuthMember.class), anyInt(), any(MemoUpdateRequest.class)))
				.willReturn(new MemoWidgetResponse("메모의 내용"));

		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/side/v1/memo/{memo_id}", 0)
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("side-tab/update-memo",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						pathParameters(
								parameterWithName("memo_id").description("메모 식별자")
						),
						requestFields(
								fieldWithPath("memo").type(JsonFieldType.STRING)
										.description("수정 요청한 메모")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.memo").type(JsonFieldType.STRING)
										.description("수정한 메모")
						)
				))
				.andExpect(status().isOk());
	}

	@DisplayName("사이드탭의 날씨를 조회한다.")
	@Test
	void findSideTabWeatherTest() throws Exception {
		/* Given */
		given(weatherWidgetService.findCurrentWeather(any(AuthMember.class), anyBoolean(), anyBoolean()))
				.willReturn(WeatherFixture.DEGREE_KR_KOREA.toWeatherWidgetResponse());

		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.get("/side/v1/weathers?metric={metric}&lang={lang}",
								"true", "true")
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("side-tab/find-weather",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						queryParameters(
								parameterWithName("metric").optional()
										.description("도씨 유무"),
								parameterWithName("lang").optional()
										.description("한국어 유무")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.city").type(JsonFieldType.STRING)
										.description("날씨를 측정한 도시"),
								fieldWithPath("body.weather_id").type(JsonFieldType.NUMBER)
										.description("날씨 식별자"),
								fieldWithPath("body.temp").type(JsonFieldType.NUMBER)
										.description("온도"),
								fieldWithPath("body.min_temp").type(JsonFieldType.NUMBER)
										.description("최소 온도"),
								fieldWithPath("body.max_temp").type(JsonFieldType.NUMBER)
										.description("최대 온도"),
								fieldWithPath("body.update_time").type(JsonFieldType.STRING)
										.description("날씨를 조회한 시간"),
								fieldWithPath("body.main").type(JsonFieldType.STRING)
										.description("날씨"),
								fieldWithPath("body.desc").type(JsonFieldType.STRING)
										.description("날씨 설명"),
								fieldWithPath("body.is_sunrise").type(JsonFieldType.BOOLEAN)
										.description("해가 떠있는지의 유무")
						)
				))
				.andExpect(status().isOk());
	}
}