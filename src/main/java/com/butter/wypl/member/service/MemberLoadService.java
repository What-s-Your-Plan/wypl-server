package com.butter.wypl.member.service;

import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.member.data.response.FindMemberProfileInfoResponse;
import com.butter.wypl.member.data.response.FindTimezonesResponse;
import com.butter.wypl.member.data.response.MemberColorsResponse;
import com.butter.wypl.member.data.response.MemberSearchResponse;
import com.butter.wypl.member.repository.query.data.MemberSearchCond;

public interface MemberLoadService {
	FindTimezonesResponse findAllTimezones(final AuthMember authMember);

	FindMemberProfileInfoResponse findProfileInfo(final AuthMember authMember, final int memberId);

	MemberColorsResponse findColors(final AuthMember authMember);

	MemberSearchResponse searchMembers(
			final AuthMember authMember,
			final MemberSearchCond cond
	);
}
