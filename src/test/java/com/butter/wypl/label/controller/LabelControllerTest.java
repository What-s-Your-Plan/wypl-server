package com.butter.wypl.label.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.mockito.ArgumentMatchers.*;
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
import com.butter.wypl.group.domain.Group;
import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.group.fixture.GroupFixture;
import com.butter.wypl.label.data.request.LabelRequest;
import com.butter.wypl.label.data.response.AllLabelListResponse;
import com.butter.wypl.label.data.response.AllLabelResponse;
import com.butter.wypl.label.data.response.LabelIdResponse;
import com.butter.wypl.label.data.response.LabelListResponse;
import com.butter.wypl.label.data.response.LabelResponse;
import com.butter.wypl.label.domain.Label;
import com.butter.wypl.label.fixture.LabelFixture;
import com.butter.wypl.label.service.LabelModifyService;
import com.butter.wypl.label.service.LabelReadService;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;

public class LabelControllerTest extends ControllerTest {
	private final LabelController labelController;

	@MockBean
	private LabelModifyService labelModifyService;
	@MockBean
	private LabelReadService labelReadService;

	@Autowired
	public LabelControllerTest(LabelController labelController) {
		this.labelController = labelController;
	}

	@Test
	@DisplayName("라벨 생성")
	void createLabel() throws Exception {
		// Given
		Label label = LabelFixture.STUDY_LABEL.toLabel();
		String json = convertToJson(new LabelRequest(label.getTitle(), label.getColor()));

		given(labelModifyService.createLabel(any(Integer.class), any(LabelRequest.class)))
			.willReturn(new LabelResponse(1, 1, label.getTitle(), label.getColor()));

		// When
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/label/v1/labels")
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
		);

		// Then
		resultActions.andDo(print())
			.andDo(document("label/create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("title").type(JsonFieldType.STRING).description("라벨 제목"),
					fieldWithPath("color").type(JsonFieldType.STRING).description("라벨 색상")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
					fieldWithPath("body.label_id").type(JsonFieldType.NUMBER).description("생성된 라벨의 인덱스"),
					fieldWithPath("body.member_id").type(JsonFieldType.NUMBER).description("라벨을 생성한 멤버의 인덱스"),
					fieldWithPath("body.title").type(JsonFieldType.STRING).description("생성된 라벨의 제목"),
					fieldWithPath("body.color").type(JsonFieldType.STRING).description("생성된 라벨의 색상")
				)
			))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("라벨 수정")
	void updateLabel() throws Exception {
		// Given
		Label label = LabelFixture.STUDY_LABEL.toLabel();
		String json = convertToJson(new LabelRequest(label.getTitle(), label.getColor()));

		given(labelModifyService.updateLabel(any(Integer.class), any(Integer.class), any(LabelRequest.class)))
			.willReturn(new LabelResponse(1, 1, label.getTitle(), label.getColor()));

		// When
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.patch("/label/v1/labels/{labelId}", 1)
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
		);

		// Then
		resultActions.andDo(print())
			.andDo(document("label/update",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("labelId").description("라벨 id")
				),
				requestFields(
					fieldWithPath("title").type(JsonFieldType.STRING).description("라벨 제목"),
					fieldWithPath("color").type(JsonFieldType.STRING).description("라벨 색상")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
					fieldWithPath("body.label_id").type(JsonFieldType.NUMBER).description("수정된 라벨의 인덱스"),
					fieldWithPath("body.member_id").type(JsonFieldType.NUMBER).description("라벨을 수정한 멤버의 인덱스"),
					fieldWithPath("body.title").type(JsonFieldType.STRING).description("수정된 라벨의 제목"),
					fieldWithPath("body.color").type(JsonFieldType.STRING).description("수정된 라벨의 색상")
				)
			))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("라벨 id로 라벨 조회")
	void getLabelByLabelId() throws Exception {
		// Given
		Label label = LabelFixture.STUDY_LABEL.toLabel();
		given(labelReadService.getLabelByLabelId(any(Integer.class)))
			.willReturn(new LabelResponse(1, 1, label.getTitle(), label.getColor()));

		// When
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/label/v1/labels/{labelId}", 1)
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		resultActions.andDo(print())
			.andDo(document("label/getLabel",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("labelId").description("라벨 id")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
					fieldWithPath("body.label_id").type(JsonFieldType.NUMBER).description("조회한 라벨의 인덱스"),
					fieldWithPath("body.member_id").type(JsonFieldType.NUMBER).description("라벨을 조회한 멤버의 인덱스"),
					fieldWithPath("body.title").type(JsonFieldType.STRING).description("조회된 라벨의 제목"),
					fieldWithPath("body.color").type(JsonFieldType.STRING).description("조회된 라벨의 색상")
				)
			))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("멤버 id로 라벨 리스트 조회")
	void getLabelsByMemberId() throws Exception {
		// Given
		given(labelReadService.getLabelsByMemberId(any(Integer.class)))
			.willReturn(new LabelListResponse(
				2,
				List.of(LabelResponse.from(LabelFixture.STUDY_LABEL.toLabel()),
					LabelResponse.from(LabelFixture.EXERCISE_LABEL.toLabel()))
			));

		// When
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/label/v1/labels")
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		resultActions.andDo(print())
			.andDo(document("label/getLabels",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
					fieldWithPath("body.label_count").type(JsonFieldType.NUMBER).description("조회한 라벨의 개수"),
					fieldWithPath("body.labels[].member_id").type(JsonFieldType.NUMBER).description("라벨 주인의 인덱스"),
					fieldWithPath("body.labels[].label_id").type(JsonFieldType.NUMBER).description("라벨의 인덱스"),
					fieldWithPath("body.labels[].title").type(JsonFieldType.STRING).description("라벨의 제목"),
					fieldWithPath("body.labels[].color").type(JsonFieldType.STRING).description("라벨의 색상")
				)
			))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("라벨 삭제")
	void deleteLabel() throws Exception {
		// Given
		given(labelModifyService.deleteLabel(any(Integer.class), any(Integer.class)))
			.willReturn(new LabelIdResponse(1));

		// When
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/label/v1/labels/{labelId}", 1)
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		resultActions.andDo(print())
			.andDo(document("label/deleteLabel",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("labelId").description("라벨 id")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
					fieldWithPath("body.label_id").type(JsonFieldType.NUMBER).description("삭제한 라벨의 인덱스")
				)
			))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("멤버의 모든 라벨과 그룹 정보 조회")
	void getAllLabel() throws Exception {
		// Given
		Member member = MemberFixture.JWA_SO_YEON.toMember();
		Group group = GroupFixture.GROUP_WORK.toGroup(member);

		given(labelReadService.getAllLabelsByMemberId(anyInt()))
			.willReturn(AllLabelListResponse.from(
				List.of(
					AllLabelResponse.from(LabelFixture.STUDY_LABEL.toLabel()),
					AllLabelResponse.of(
						MemberGroup.of(member, group), group
					)
				)
			));
		// When
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/label/v1/labels/main")
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		resultActions.andDo(print())
			.andDo(document("label/getAllLabel",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
					fieldWithPath("body.label_count").type(JsonFieldType.NUMBER).description("라벨과 그룹의 총 개수"),
					fieldWithPath("body.labels[].id").type(JsonFieldType.NUMBER).description("라벨이나 그룹의 아이디"),
					fieldWithPath("body.labels[].category").type(JsonFieldType.STRING).description("타입"),
					fieldWithPath("body.labels[].title").type(JsonFieldType.STRING).description("라벨이나 그룹의 제목"),
					fieldWithPath("body.labels[].color").type(JsonFieldType.STRING).description("라벨이나 그룹의 색상")
				)
			))
			.andExpect(status().isOk());

	}
}
