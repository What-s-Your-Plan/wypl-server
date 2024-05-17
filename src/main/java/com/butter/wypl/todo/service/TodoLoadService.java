package com.butter.wypl.todo.service;

import com.butter.wypl.todo.data.response.TodoResponse;

public interface TodoLoadService {
	TodoResponse getTodos(final int memberId);
}
