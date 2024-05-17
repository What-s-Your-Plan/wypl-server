package com.butter.wypl.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.repository.query.MemberRepositoryCustom;

public interface MemberRepository extends JpaRepository<Member, Integer>,
		MemberRepositoryCustom {
	Optional<Member> findByEmail(String email);

	@Query("SELECT m FROM Member m "
		+ "JOIN FETCH m.memberGroups mg "
		+ "JOIN FETCH mg.group g "
		+ "WHERE m.id IN :idList")
	List<Member> findAllById(List<Integer> idList);
}
