package com.butter.wypl.todo.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.butter.wypl.global.annotation.ServiceTest;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.todo.data.response.TodoResponse;
import com.butter.wypl.todo.domain.Todo;
import com.butter.wypl.todo.repository.TodoRepository;

@ServiceTest
class TodoLoadServiceImplTest {

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

	@Test
	@DisplayName("할일 목록 조회")
	void getTodos () {
	    //given
		List<Todo> list = dummyData();
		given(memberRepository.findById(anyInt()))
			.willReturn(Optional.of(member));
		given(todoRepository.findByMemberAndDeletedAtIsNull(any(Member.class)))
			.willReturn(list);

	    //when
		TodoResponse result = todoService.getTodos(member.getId());

		//then
		assertThat(result.todoCount()).isEqualTo(list.size());
		assertThat(result.memberId()).isEqualTo(member.getId());
		assertThat(result.todos()).allMatch(todo -> todo.content().startsWith("운동가기"));
	}

	List<Todo> dummyData() {
		return Stream.iterate(0, i -> i < 5, i -> i + 1)
			.map(i -> Todo.builder()
				.id(i)
				.member(member)
				.content("운동가기" + i)
				.build())
			.toList();
	}
}