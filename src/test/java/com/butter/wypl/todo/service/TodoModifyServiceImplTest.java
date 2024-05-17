package com.butter.wypl.todo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.butter.wypl.global.annotation.ServiceTest;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.todo.data.request.TodoSaveResquest;
import com.butter.wypl.todo.data.request.TodoUpdateRequest;
import com.butter.wypl.todo.domain.Todo;
import com.butter.wypl.todo.exception.TodoErrorCode;
import com.butter.wypl.todo.exception.TodoException;
import com.butter.wypl.todo.repository.TodoRepository;

@ServiceTest
class TodoModifyServiceImplTest {

	@InjectMocks
	private TodoServiceImpl todoService;
	@Mock
	private TodoRepository todoRepository;
	@Mock
	private MemberRepository memberRepository;

	private Member member;

	@BeforeEach
	void initMember() {
		member = MemberFixture.CHOI_MIN_JUN.toMember();
	}

	/*
	* 1.할일등록
	* 2.할일수정
	* 3.할일삭제
	* 4.할일체크
	* */

	@Test
	@DisplayName("할일을 생성한다.")
	void createTodoServiceTest () {
	    //given
		String content = "취업하기";

		given(memberRepository.findById(anyInt()))
			.willReturn(Optional.of(member));

		ArgumentCaptor<Todo> captor = ArgumentCaptor.forClass(Todo.class);
		when(todoRepository.save(captor.capture()))
			.thenAnswer(i -> i.getArguments()[0]);

		//when
		TodoSaveResquest request = new TodoSaveResquest(content);
		todoService.createTodo(request,member.getId());

		//then
		Todo savedTodo = captor.getValue();
		assertThat(savedTodo).isNotNull();
		assertThat(savedTodo.getMember()).isEqualTo(member);
		assertThat(savedTodo.getContent()).isEqualTo(content);
	}

	@Test
	@DisplayName("할일을 수정한다.")
	void updateTodoServiceTest () {
	    //given
		int todoId = 1;
		Todo todo = Todo.builder()
			.id(todoId)
			.member(member)
			.content("취업")
			.isCompleted(false)
			.build();
		String originContent = todo.getContent();

		given(memberRepository.findById(anyInt()))
			.willReturn(Optional.of(member));

		given(todoRepository.findById(anyInt()))
			.willReturn(Optional.of(todo));

		String updateContent = "어디로 취업하지?";

	    //when
		TodoUpdateRequest request = new TodoUpdateRequest(updateContent);
		todoService.updateTodo(request, todoId, member.getId());

	    //then
		assertThat(todo.getContent()).isNotEqualTo(originContent);
		assertThat(todo.getContent()).isEqualTo(updateContent);
	}

	@Test
	@DisplayName("할일을 삭제한다.")
	void deleteTodoServiceTest () {
	    //given
		int todoId = 1;
		Todo todo = Todo.builder()
			.id(todoId)
			.member(member)
			.content("테스트")
			.build();

		given(memberRepository.findById(anyInt()))
			.willReturn(Optional.of(member));

		given(todoRepository.findById(anyInt()))
    		.willReturn(Optional.of(todo));

		LocalDateTime originDeletedAt = todo.getDeletedAt();

		//when
		todoService.deleteTodo(todoId, member.getId());

	    //then
		assertThat(originDeletedAt).isNull();
		assertThat(todo.getDeletedAt()).isNotNull();
	}

	@Test
	@DisplayName("할일삭제시 작성자와 현재 로그인 회원 정보가 다른경우")
	void deleteTodoDiffMember () {
	    //given
		int todoId = 1;
		Member authorMember = MemberFixture.CHOI_MIN_JUN.toMemberWithId(1);
		Member diffMember = MemberFixture.JO_DA_MIN.toMemberWithId(2);

		Todo todo = Todo.builder()
			.id(todoId)
			.member(authorMember)
			.content("삭제")
			.build();
		given(memberRepository.findById(anyInt()))
			.willReturn(Optional.of(diffMember));
		given(todoRepository.findById(anyInt()))
			.willReturn(Optional.of(todo));

	    //when
	    //then
		assertThatThrownBy(() -> todoService.deleteTodo(todoId, diffMember.getId()))
			.isInstanceOf(TodoException.class)
			.hasMessage(TodoErrorCode.TODO_AUTHOR_DIFFERENT.getMessage());
	}

	@Test
	@DisplayName("할일을 체크한다.")
	void toggleTodo () {
	    //given
		int todoId = 1;
		Todo todo = Todo.builder()
			.id(todoId)
			.content("체크")
			.member(member)
			.build();

		given(memberRepository.findById(anyInt()))
			.willReturn(Optional.of(member));
		given(todoRepository.findById(anyInt()))
			.willReturn(Optional.of(todo));

		boolean originCompleted = todo.isCompleted();
		//when
		todoService.toggleTodo(todoId, member.getId());

	    //then
		assertThat(originCompleted).isNotEqualTo(todo.isCompleted());
	}

}