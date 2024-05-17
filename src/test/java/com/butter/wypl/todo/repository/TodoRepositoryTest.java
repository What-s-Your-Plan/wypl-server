package com.butter.wypl.todo.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.butter.wypl.global.annotation.JpaRepositoryTest;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.todo.domain.Todo;
import com.butter.wypl.todo.fixture.TodoFixture;

@JpaRepositoryTest
class TodoRepositoryTest {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private TodoRepository todoRepository;

	private Member member;

	@BeforeEach
	void init() {
		member = memberRepository.save(MemberFixture.CHOI_MIN_JUN.toMemberWithId(1));
	}

	@Test
	@DisplayName("할 일 생성")
	void createTodo() throws Exception {
		//given
		Todo entity = Todo.builder()
			.member(member)
			.content("알고리즘 풀기")
			.build();

		//when
		Todo savedTodo = todoRepository.save(entity);

		//then
		assertThat(savedTodo).isNotNull();
		assertThat(savedTodo.isCompleted()).isFalse();
	}

	@Test
	@DisplayName("할 일 수정")
	void updateTodo() throws Exception {
		//given
		Todo entity = TodoFixture.ALGORITHM_STUDY.toTodo();
		String preContent = entity.getContent();

		//when
		String updatedContent = "백준 말고 프로그래머스";
		entity.updateContent(updatedContent);

		//then
		assertThat(preContent).isNotEqualTo(entity.getContent());
		assertThat(entity.getContent()).isEqualTo(updatedContent);
	}

	@Test
	@DisplayName("할 일 삭제, 논리삭제함")
	void deleteTodo() throws Exception {
		//given
		Todo entity = TodoFixture.CS_STUDY.toTodo();
		LocalDateTime deletedAt = entity.getDeletedAt();

		//when
		entity.delete();

		//then
		assertThat(deletedAt).isNull();
		assertThat(entity.getDeletedAt()).isNotNull();
	}

	@Test
	@DisplayName("할 일 목록조회")
	void getTodos() throws Exception {
		//given
		saveTodos();

		//when
		List<Todo> result = todoRepository.findByMemberAndDeletedAtIsNull(member);

		//then
		assertThat(result).isNotNull();
		assertThat(result.size()).isEqualTo(5);
	}

	/**
	 * 테스트 더미 데이터 저장을 위한 메소드
	 */
	void saveTodos() {
		Stream.iterate(0, i -> i < 5, i -> i + 1)
			.forEach(idx ->
				{
					todoRepository.save(
						Todo.builder()
							.member(member)
							.content("알고리즘풀기" + idx)
							.build()
					);
				}
			);
	}

	@Test
	@DisplayName("할 일 완료")
	void doneTodo() throws Exception {
		//given
		saveTodos();
		List<Todo> todos = todoRepository.findByMemberAndDeletedAtIsNull(member);

		//when
		boolean completed = todos.get(0).isCompleted();
		todos.get(0).toggleTodo();

		//then
		assertThat(completed).isNotEqualTo(todos.get(0).isCompleted());
	}
}