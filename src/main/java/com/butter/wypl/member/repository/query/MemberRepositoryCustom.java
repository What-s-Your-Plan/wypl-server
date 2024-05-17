package com.butter.wypl.member.repository.query;

import java.util.List;

import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.repository.query.data.MemberSearchCond;

public interface MemberRepositoryCustom {
	List<Member> findBySearchCond(MemberSearchCond cond);

	List<Member> findAllActiveMembers();
}
