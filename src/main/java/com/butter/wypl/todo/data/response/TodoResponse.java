package com.butter.wypl.todo.data.response;

import java.util.List;

import com.butter.wypl.member.domain.Member;
import com.butter.wypl.todo.domain.Todo;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TodoResponse(
	@JsonProperty("todo_count")
	int todoCount,
	@JsonProperty("member_id")
	int memberId,
	@JsonProperty("nick_name")
	String nickName,
	@JsonProperty("todos")
	List<TodoItem> todos
) {

	public static TodoResponse of(List<Todo> todos, Member member) {
		List<TodoItem> todoItems = todos.stream()
			.map(todo -> new TodoItem(todo.getId(), todo.getContent(), todo.isCompleted()))
			.toList();

		return new TodoResponse(
			todoItems.size(),
			member.getId(),
			member.getNickname(),
			todoItems
		);
	}
}
