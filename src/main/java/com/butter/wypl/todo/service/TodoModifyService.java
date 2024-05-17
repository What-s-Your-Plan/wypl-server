package com.butter.wypl.todo.service;

import com.butter.wypl.todo.data.request.TodoSaveResquest;
import com.butter.wypl.todo.data.request.TodoUpdateRequest;

public interface TodoModifyService {
	void createTodo(final TodoSaveResquest request, final int memberId);
	void updateTodo(final TodoUpdateRequest request, final int todoId, final int memberId);
	void deleteTodo(final int todoId, final int memberId);
	void toggleTodo(final int todoId, final int memberId);
}
