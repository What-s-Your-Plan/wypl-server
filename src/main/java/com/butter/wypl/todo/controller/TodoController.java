package com.butter.wypl.todo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.butter.wypl.auth.annotation.Authenticated;
import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.global.common.Message;
import com.butter.wypl.todo.data.request.TodoSaveResquest;
import com.butter.wypl.todo.data.request.TodoUpdateRequest;
import com.butter.wypl.todo.data.response.TodoResponse;
import com.butter.wypl.todo.exception.TodoErrorCode;
import com.butter.wypl.todo.exception.TodoException;
import com.butter.wypl.todo.service.TodoLoadService;
import com.butter.wypl.todo.service.TodoModifyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todo/v1/todos")
public class TodoController {

	private final TodoModifyService todoModifyService;
	private final TodoLoadService todoLoadService;
	/*
	 * 1. 할일등록
	 * 2. 할일조회
	 * 3.할일수정
	 * 4.할일삭제
	 * 5.할일완료
	 * */
	@PostMapping
	public ResponseEntity<Message<Void>> createTodo(
		@Authenticated AuthMember authMember,
		@RequestBody TodoSaveResquest request) {

		if (!StringUtils.hasText(request.content())) {
			throw new TodoException(TodoErrorCode.CLIENT_DATA_NOT_VALID);
		}
		if (request.content().length() > 765) {
			throw new TodoException(TodoErrorCode.DATA_LENGTH_OUT_OF_RANGE);
		}

		todoModifyService.createTodo(request, authMember.getId());
		return ResponseEntity.ok(Message.onlyMessage("Todo 생성 성공"));
	}

	@GetMapping
	public ResponseEntity<Message<TodoResponse>> getTodos(@Authenticated AuthMember authMember) {
		return ResponseEntity.ok(
			Message.withBody("Todo 목록 조회 성공", todoLoadService.getTodos(authMember.getId()))
		);
	}

	@PatchMapping("/{todoId}")
	public ResponseEntity<Message<Void>> updateTodo(
		@Authenticated AuthMember authMember,
		@RequestBody TodoUpdateRequest request,
		@PathVariable("todoId") int todoId
	) {
		todoModifyService.updateTodo(request, todoId, authMember.getId());
		return ResponseEntity.ok(
			Message.onlyMessage("Todo 수정 성공")
		);
	}

	@DeleteMapping("/{todoId}")
	public ResponseEntity<Message<Void>> deleteTodo(
		@Authenticated AuthMember authMember,
		@PathVariable("todoId") int todoId
	) {
		todoModifyService.deleteTodo(todoId, authMember.getId());
		return ResponseEntity.ok(
			Message.onlyMessage("Todo 삭제 성공")
		);
	}

	@PatchMapping("/check/{todoId}")
	public ResponseEntity<Message<Void>> checkTodo(
		@Authenticated AuthMember authMember,
		@PathVariable("todoId") int todoId
	) {
		todoModifyService.toggleTodo(todoId, authMember.getId());
		return ResponseEntity.ok(
			Message.onlyMessage("Todo 체크 성공")
		);
	}
}
