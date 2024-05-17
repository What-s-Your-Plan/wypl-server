package com.butter.wypl.member.utils;

import java.util.List;

import com.butter.wypl.global.annotation.Generated;
import com.butter.wypl.global.exception.CallConstructorException;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.exception.MemberErrorCode;
import com.butter.wypl.member.exception.MemberException;
import com.butter.wypl.member.repository.MemberRepository;

public class MemberServiceUtils {

	@Generated
	private MemberServiceUtils() {
		throw new CallConstructorException();
	}

	public static Member findById(
		final MemberRepository memberRepository,
		final int id
	) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new MemberException(MemberErrorCode.NOT_EXIST_MEMBER));
	}

	public static Member findByEmail(
		final MemberRepository memberRepository,
		final String email
	) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new MemberException(MemberErrorCode.NOT_EXIST_MEMBER));
	}

	public static void validateOwnership(
		final Member member,
		final int checkMemberId
	) {
		if (member.getId() != checkMemberId) {
			throw new MemberException(MemberErrorCode.PERMISSION_DENIED);
		}
	}

	public static List<Member> findAllActiveMembers(
		final MemberRepository memberRepository
	) {
		return memberRepository.findAllActiveMembers();
	}
}
