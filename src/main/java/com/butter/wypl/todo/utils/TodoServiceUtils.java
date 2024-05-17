package com.butter.wypl.todo.utils;

import com.butter.wypl.global.annotation.Generated;
import com.butter.wypl.todo.domain.Todo;
import com.butter.wypl.todo.exception.TodoErrorCode;
import com.butter.wypl.todo.exception.TodoException;
import com.butter.wypl.todo.repository.TodoRepository;

public class TodoServiceUtils {

	@Generated
	private TodoServiceUtils() {

	}

	public static Todo findById(final TodoRepository repository, final int id) {
		return repository.findById(id)
			.orElseThrow(() -> new TodoException(TodoErrorCode.NOT_EXIST_TODO));
	}
}
