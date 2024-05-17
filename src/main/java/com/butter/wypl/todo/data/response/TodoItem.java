package com.butter.wypl.todo.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TodoItem(
	@JsonProperty("todo_id")
	int todoId,
	@JsonProperty("content")
	String content,
	@JsonProperty("is_completed")
	boolean isCompleted
) {
}
