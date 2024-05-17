package com.butter.wypl.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.butter.wypl.member.domain.SocialMember;

public interface SocialMemberRepository extends JpaRepository<SocialMember, Integer> {
}
