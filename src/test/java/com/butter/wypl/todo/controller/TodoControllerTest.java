package com.butter.wypl.todo.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.todo.data.request.TodoSaveResquest;
import com.butter.wypl.todo.data.request.TodoUpdateRequest;
import com.butter.wypl.todo.data.response.TodoItem;
import com.butter.wypl.todo.data.response.TodoResponse;
import com.butter.wypl.todo.exception.TodoErrorCode;
import com.butter.wypl.todo.exception.TodoException;
import com.butter.wypl.todo.service.TodoLoadService;
import com.butter.wypl.todo.service.TodoModifyService;

class TodoControllerTest extends ControllerTest {

	public static final String URI_PATH = "/todo/v1/todos";
	@MockBean
	private TodoModifyService todoModifyService;
	@MockBean
	private TodoLoadService todoLoadService;
	@Autowired
	private TodoController todoController;

	private AuthMember authMember;
	private Member member;

	/*
	 * 1. 할일등록
	 * 2. 할일조회
	 * 3.할일수정
	 * 4.할일삭제
	 * 5.할일체크
	 * */

	@BeforeEach
	void initMember() {
		authMember = AuthMember.from(1);
		member = MemberFixture.CHOI_MIN_JUN.toMemberWithId(1);
	}

	@Test
	@DisplayName("할일 등록")
	void createTodo() throws Exception {
		//given
		String content = "운동가기";
		String json = convertToJson(new TodoSaveResquest(content));

		willDoNothing().
			given(todoModifyService).createTodo(any(TodoSaveResquest.class), anyInt());

		//when
		ResultActions actions = mockMvc.perform(
			RestDocumentationRequestBuilders.post(URI_PATH)
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
		);

		//then
		actions.andDo(print())
			.andDo(document("todo/createTodo",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("content").type(JsonFieldType.STRING).description("Todo 내용")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
				)))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("할일등록시 데이터가 올바르지 않은경우")
	void todoExceptionDataNotValid () {
	    //given
		String content = "";
		TodoSaveResquest request = mock(TodoSaveResquest.class);
		//when
		when(request.content()).thenReturn(content);

	    //then
		assertThatThrownBy(() -> todoController.createTodo(authMember, request))
			.isInstanceOf(TodoException.class)
			.hasMessage(TodoErrorCode.CLIENT_DATA_NOT_VALID.getMessage());
	}

	@Test
	@DisplayName("할일등록시 데이터 길이가 765 넘는경우")
	void todoExceptionOutOfRange() {
		//given
		String content = "*";

		TodoSaveResquest request = mock(TodoSaveResquest.class);
		//when
		when(request.content()).thenReturn(content.repeat(766));

		//then
		assertThatThrownBy(() -> todoController.createTodo(authMember, request))
			.isInstanceOf(TodoException.class)
			.hasMessage(TodoErrorCode.DATA_LENGTH_OUT_OF_RANGE.getMessage());
	}

	@Test
	@DisplayName("할일 목록 조회")
	void getTodos() throws Exception {
		//given
		List<TodoItem> todoItems = List.of(new TodoItem(1, "운동", true),
			new TodoItem(2, "공부", false));
		TodoResponse todoResponse = new TodoResponse(todoItems.size(), member.getId(), member.getNickname(), todoItems);

		given(todoLoadService.getTodos(anyInt())).willReturn(todoResponse);

		//when
		ResultActions actions = mockMvc.perform(
			RestDocumentationRequestBuilders.get(URI_PATH)
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
		);

		//then
		actions.andDo(print())
			.andDo(document("todo/getTodos",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("body.todo_count").type(JsonFieldType.NUMBER).description("Todo 개수"),
					fieldWithPath("body.member_id").type(JsonFieldType.NUMBER).description("작성자 ID"),
					fieldWithPath("body.nick_name").type(JsonFieldType.STRING).description("작성자 닉네임"),
					fieldWithPath("body.todos[].todo_id").type(JsonFieldType.NUMBER).description("Todo ID"),
					fieldWithPath("body.todos[].content").type(JsonFieldType.STRING).description("Todo 내용"),
					fieldWithPath("body.todos[].is_completed").type(JsonFieldType.BOOLEAN).description("Todo 완료여부")
				)))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("할일 수정")
	void updateTodo() throws Exception {
		//given
		TodoUpdateRequest request = new TodoUpdateRequest("수정합니다");
		String json = convertToJson(request);

		willDoNothing()
			.given(todoModifyService).updateTodo(any(TodoUpdateRequest.class), anyInt(), anyInt());

		//when
		ResultActions actions = mockMvc.perform(
			RestDocumentationRequestBuilders.patch(URI_PATH + "/{todoId}", 1)
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
		);

		//then
		actions.andDo(print())
			.andDo(document("todo/updateTodo",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("todoId").description("Todo Id")
				),
				requestFields(
					fieldWithPath("content").type(JsonFieldType.STRING).description("Todo 내용")
				))
			)
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("할일 삭제")
	void deleteTodo() throws Exception {
		//given
		willDoNothing()
			.given(todoModifyService).deleteTodo(anyInt(), anyInt());

		//when
		ResultActions actions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete(URI_PATH + "/{todoId}", 1)
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
		);

		//then
		actions.andDo(print())
			.andDo(document("todo/deleteTodo",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("todoId").description("Todo Id")
				))
			)
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("할일 체크")
	void checkTodo () throws Exception {
	    //given
		willDoNothing()
			.given(todoModifyService).toggleTodo(anyInt(), anyInt());

	    //when
		ResultActions actions = mockMvc.perform(
			RestDocumentationRequestBuilders.patch(URI_PATH + "/check/{todoId}", 1)
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
		);

		//then
		actions.andDo(print())
			.andDo(document("todo/checkTodo",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("todoId").description("Todo Id")
				))
			)
			.andExpect(status().isOk());
	}

}