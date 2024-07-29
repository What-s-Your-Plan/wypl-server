package com.butter.wypl.todo.domain;

import com.butter.wypl.global.common.BaseEntity;
import com.butter.wypl.member.domain.Member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
@Table(name = "todo_tbl")
public class Todo extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "todo_id")
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(nullable = false, length = 765)
	private String content;

	@Column(name = "is_completed", nullable = false)
	private boolean isCompleted;

	public void updateContent(String content) {
		this.content = content;
	}

	public void toggleTodo() {
		this.isCompleted = !this.isCompleted;
	}
}
