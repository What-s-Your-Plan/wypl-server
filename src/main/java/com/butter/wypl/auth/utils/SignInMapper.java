package com.butter.wypl.auth.utils;

import com.butter.wypl.global.annotation.EntityMapper;
import com.butter.wypl.global.annotation.Generated;
import com.butter.wypl.global.common.Color;
import com.butter.wypl.global.exception.CallConstructorException;
import com.butter.wypl.infrastructure.ouath.OAuthMember;
import com.butter.wypl.member.domain.CalendarTimeZone;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.domain.OauthProvider;
import com.butter.wypl.member.domain.SocialMember;
import com.butter.wypl.sidetab.domain.SideTab;

@EntityMapper
public class SignInMapper {

	@Generated
	private SignInMapper() {
		throw new CallConstructorException();
	}

	public static Member toMember(
			final OAuthMember oAuthMember
	) {
		return Member.builder()
				.email(oAuthMember.email())
				.nickname(oAuthMember.getEmailPrefix())
				.profileImage(oAuthMember.profileImage())
				.timeZone(CalendarTimeZone.KOREA)
				.color(Color.labelBrown)
				.build();
	}

	public static SocialMember toSocialMember(
			final Member member,
			final OAuthMember oAuthMember,
			final OauthProvider oauthProvider
	) {
		return SocialMember.builder()
				.member(member)
				.OauthId(oAuthMember.subject())
				.oauthProvider(oauthProvider)
				.build();
	}

	public static SideTab toSideTab(
			final Member member
	) {
		return SideTab.from(member);
	}
}
