package com.butter.wypl.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.butter.wypl.member.domain.Member;
import com.butter.wypl.todo.domain.Todo;

public interface TodoRepository extends JpaRepository<Todo, Integer> {

	List<Todo> findByMemberAndDeletedAtIsNull(Member member);
}
