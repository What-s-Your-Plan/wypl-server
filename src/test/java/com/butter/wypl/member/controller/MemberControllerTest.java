package com.butter.wypl.member.controller;

import static com.butter.wypl.file.fixture.FileFixture.*;
import static com.butter.wypl.member.fixture.MemberFixture.*;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.global.common.Color;
import com.butter.wypl.global.common.ControllerTest;
import com.butter.wypl.member.data.MemberSearchInfo;
import com.butter.wypl.member.data.request.MemberBirthdayUpdateRequest;
import com.butter.wypl.member.data.request.MemberColorUpdateRequest;
import com.butter.wypl.member.data.request.MemberNicknameUpdateRequest;
import com.butter.wypl.member.data.request.MemberTimezoneUpdateRequest;
import com.butter.wypl.member.data.response.FindMemberProfileInfoResponse;
import com.butter.wypl.member.data.response.FindTimezonesResponse;
import com.butter.wypl.member.data.response.MemberBirthdayUpdateResponse;
import com.butter.wypl.member.data.response.MemberColorUpdateResponse;
import com.butter.wypl.member.data.response.MemberColorsResponse;
import com.butter.wypl.member.data.response.MemberNicknameUpdateResponse;
import com.butter.wypl.member.data.response.MemberProfileImageUpdateResponse;
import com.butter.wypl.member.data.response.MemberSearchResponse;
import com.butter.wypl.member.data.response.MemberTimezoneUpdateResponse;
import com.butter.wypl.member.domain.CalendarTimeZone;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.member.repository.query.data.MemberSearchCond;
import com.butter.wypl.member.service.MemberLoadService;
import com.butter.wypl.member.service.MemberModifyService;

class MemberControllerTest extends ControllerTest {

	@Autowired
	private MemberController memberController;

	@MockBean
	private MemberModifyService memberModifyService;
	@MockBean
	private MemberLoadService memberLoadService;

	@DisplayName("회원의 이메일을 가지고 조회한다.")
	@Test
	void memberSearchTest() throws Exception {
		/* Given */
		MemberFixture[] values = MemberFixture.values();
		List<MemberSearchInfo> memberSearchInfos = IntStream.rangeClosed(1, values.length)
				.mapToObj(i -> {
					Member member = values[i - 1].toMemberWithId(i);
					member.changeProfileImage("image_url_" + i);
					return member;
				})
				.map(MemberSearchInfo::from)
				.toList();
		given(memberLoadService.searchMembers(any(AuthMember.class), any(MemberSearchCond.class)))
				.willReturn(MemberSearchResponse.from(memberSearchInfos));

		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.get("/member/v1/members?q={q}&size={size}",
								"work",
								"10")
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
		);
		/* Then */
		actions.andDo(print())
				.andDo(document("member/search-members",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						queryParameters(
								parameterWithName("q")
										.description("검색 대상의 이메일"),
								parameterWithName("size").optional()
										.description("검색 요청의 데이터 건수")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.members[]").type(JsonFieldType.ARRAY)
										.description("조회된 사용자들의 목록"),
								fieldWithPath("body.member_count").type(JsonFieldType.NUMBER)
										.description("조회된 사용자들의 수"),
								fieldWithPath("body.members[].id").type(JsonFieldType.NUMBER)
										.description("조회한 사용자의 식별자"),
								fieldWithPath("body.members[].email").type(JsonFieldType.STRING)
										.description("조회한 사용자의 이메일"),
								fieldWithPath("body.members[].nickname").type(JsonFieldType.STRING)
										.description("조회한 사용자의 닉네임"),
								fieldWithPath("body.members[].profile_image_url").type(JsonFieldType.STRING).optional()
										.description("조회한 사용자의 프로필 이미지")
						)
				))
				.andExpect(status().isOk());
	}

	@DisplayName("회원의 메인 컬러와 서버의 색상을 조회한다.")
	@Test
	void findColorTest() throws Exception {
		/* Given */
		given(memberLoadService.findColors(any(AuthMember.class)))
				.willReturn(
						MemberColorsResponse.of(
								KIM_JEONG_UK.toMember().getColor(),
								Arrays.stream(Color.values()).toList()
						));

		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.get("/member/v1/members/colors")
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
		);


		/* Then */
		actions.andDo(print())
				.andDo(document("member/find-color",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.select_color").type(JsonFieldType.STRING)
										.description("사용자 메인 컬러"),
								fieldWithPath("body.colors[]").type(JsonFieldType.ARRAY)
										.description("서버의 컬러 색상 목록"),
								fieldWithPath("body.color_count").type(JsonFieldType.NUMBER)
										.description("서버의 컬러 색상의 갯수")
						)
				))
				.andExpect(status().isOk());
	}

	@DisplayName("사용자가 닉네임을 수정한다.")
	@Test
	void updateColorTest() throws Exception {
		/* Given */
		String json = convertToJson(new MemberColorUpdateRequest(Color.labelLavender));

		given(memberModifyService.updateColor(any(AuthMember.class), any(MemberColorUpdateRequest.class)))
				.willReturn(new MemberColorUpdateResponse(Color.labelLavender));

		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/member/v1/members/colors")
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("member/update-color",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("color").type(JsonFieldType.STRING)
										.description("변경 요청한 컬러 코드")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.color").type(JsonFieldType.STRING)
										.description("변경한 컬러 코드")
						)
				))
				.andExpect(status().isOk());
	}

	@DisplayName("사용자가 서버의 타임존을 조회한다.")
	@Test
	void findTimezones() throws Exception {
		/* Given */
		given(memberLoadService.findAllTimezones(any(AuthMember.class)))
				.willReturn(FindTimezonesResponse.of(KIM_JEONG_UK.toMember().getTimeZone(),
						CalendarTimeZone.getTimeZones()));

		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.get("/member/v1/timezones")
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("member/timezones",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.member_timezone").type(JsonFieldType.STRING)
										.description("사용자의 타임존"),
								fieldWithPath("body.timezones[]").type(JsonFieldType.ARRAY)
										.description("서버의 타임존 목록"),
								fieldWithPath("body.timezone_count").type(JsonFieldType.NUMBER)
										.description("서버의 타임존 개수")
						)
				))
				.andExpect(status().isOk());
	}

	@DisplayName("사용자의 프로필을 조회한다.")
	@Test
	void findProfile() throws Exception {
		/* Given */
		Member member = KIM_JEONG_UK.toMember();
		member.changeProfileImage("profile_url");
		given(memberLoadService.findProfileInfo(any(AuthMember.class), anyInt()))
				.willReturn(FindMemberProfileInfoResponse.from(member));

		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.get("/member/v1/members/{member_id}", 1)
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("member/profile",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.id").type(JsonFieldType.NUMBER)
										.description("사용자 식별자"),
								fieldWithPath("body.email").type(JsonFieldType.STRING)
										.description("사용자 이메일"),
								fieldWithPath("body.nickname").type(JsonFieldType.STRING)
										.description("사용자 닉네임"),
								fieldWithPath("body.profile_image_url").type(JsonFieldType.STRING).optional()
										.description("사용자 프로필 이미지 URL"),
								fieldWithPath("body.main_color").type(JsonFieldType.STRING)
										.description("사용자 기본 색상")
						)
				))
				.andExpect(status().isOk());
	}

	@DisplayName("사용자가 닉네임을 수정한다.")
	@Test
	void updateNicknameTest() throws Exception {
		/* Given */
		String nickname = "wypl";
		String json = convertToJson(new MemberNicknameUpdateRequest(nickname));

		given(memberModifyService.updateNickname(any(AuthMember.class), any(MemberNicknameUpdateRequest.class)))
				.willReturn(new MemberNicknameUpdateResponse(nickname));

		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/member/v1/members/nickname")
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("member/update-nickname",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("nickname").type(JsonFieldType.STRING)
										.description("변경 요청한 닉네임")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.nickname").type(JsonFieldType.STRING)
										.description("변경한 닉네임")
						)
				))
				.andExpect(status().isOk());
	}

	@DisplayName("사용자가 생일을 수정한다.")
	@Test
	void updateBirthdayTest() throws Exception {
		/* Given */
		LocalDate birthday = KIM_JEONG_UK.getBirthday();
		String json = convertToJson(new MemberBirthdayUpdateRequest(birthday));

		given(memberModifyService.updateBirthday(any(AuthMember.class), any(MemberBirthdayUpdateRequest.class)))
				.willReturn(MemberBirthdayUpdateResponse.from(birthday));

		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/member/v1/members/birthday")
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("member/update-birthday",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("birthday").type(JsonFieldType.STRING)
										.description("변경 요청한 사용자의 생일")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.birthday").type(JsonFieldType.STRING)
										.description("변경한 사용자의 생일 형식"),
								fieldWithPath("body.birthday_as_string").type(JsonFieldType.STRING)
										.description("변경한 사용자의 생일")
						)
				))
				.andExpect(status().isOk());
	}

	@DisplayName("사용자가 타임존을 수정한다.")
	@Test
	void updateTimezoneTest() throws Exception {
		/* Given */
		String json = convertToJson(new MemberTimezoneUpdateRequest(CalendarTimeZone.ENGLAND));

		given(memberModifyService.updateTimezone(any(AuthMember.class), any(MemberTimezoneUpdateRequest.class)))
				.willReturn(new MemberTimezoneUpdateResponse(CalendarTimeZone.ENGLAND));

		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.patch("/member/v1/members/timezones")
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("member/update-timezone",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestFields(
								fieldWithPath("timezone").type(JsonFieldType.STRING)
										.description("변경 요청한 사용자의 타임존")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.timezone").type(JsonFieldType.STRING)
										.description("변경한 사용자의 타임존")
						)
				))
				.andExpect(status().isOk());
	}

	@DisplayName("사용자가 프로필 이미지를 수정한다.")
	@Test
	void updateProfileImageTest() throws Exception {
		/* Given */
		String newProfileImageUrl = "aws.image.url";

		given(memberModifyService.updateProfileImage(any(AuthMember.class), any(MultipartFile.class)))
				.willReturn(new MemberProfileImageUpdateResponse(newProfileImageUrl));

		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.multipart("/member/v1/members/profile-image")
						.file(PNG_IMAGE.getMockMultipartFile())
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("member/update-profile-image",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestParts(
								partWithName("image").description("변경 요청한 사용자 프로필 이미지")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.profile_image_url").type(JsonFieldType.STRING)
										.description("변경한 사용자의 프로필 이미지 URL")
						)
				))
				.andExpect(status().isOk());
	}
}