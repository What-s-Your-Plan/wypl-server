package com.butter.wypl.todo.fixture;

import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.todo.domain.Todo;

import lombok.Getter;

@Getter
public enum TodoFixture {
	ALGORITHM_STUDY(1, MemberFixture.CHOI_MIN_JUN.toMemberWithId(3), "백준 BFS/DFS", false),
	CS_STUDY(2, MemberFixture.CHOI_MIN_JUN.toMemberWithId(3), "Java JVM", false),
	FIND_JOB(3, MemberFixture.CHOI_MIN_JUN.toMemberWithId(3), "가자", false),
	;


	private final int id;
	private final Member member;
	private final String content;
	private final boolean isCompleted;

	TodoFixture(int id, Member member, String content, boolean isCompleted) {
		this.id = id;
    	this.member = member;
    	this.content = content;
    	this.isCompleted = isCompleted;
	}

	public Todo toTodo() {
		return Todo.builder()
			.id(id)
			.member(member)
			.content(content)
			.isCompleted(isCompleted)
			.build();
	}

}
