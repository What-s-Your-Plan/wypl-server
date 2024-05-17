package com.butter.wypl.auth.service;

import org.springframework.transaction.annotation.Transactional;

import com.butter.wypl.auth.exception.AuthErrorCode;
import com.butter.wypl.auth.exception.AuthException;
import com.butter.wypl.auth.utils.SignInMapper;
import com.butter.wypl.global.annotation.FacadeService;
import com.butter.wypl.infrastructure.ouath.OAuthMember;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.domain.OauthProvider;
import com.butter.wypl.sidetab.domain.SideTab;
import com.butter.wypl.member.domain.SocialMember;
import com.butter.wypl.member.repository.MemberRepository;
import com.butter.wypl.sidetab.repository.SideTabRepository;
import com.butter.wypl.member.repository.SocialMemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@FacadeService
public class SignInService {
	private final MemberRepository memberRepository;
	private final SideTabRepository sideTabRepository;
	private final SocialMemberRepository socialMemberRepository;

	public Member signIn(
			final OAuthMember oAuthMember,
			final String provider
	) {
		OauthProvider oauthProvider = getOauthProvider(provider);

		Member member = SignInMapper.toMember(oAuthMember);
		SocialMember socialMember = SignInMapper.toSocialMember(member, oAuthMember, oauthProvider);
		SideTab sideTab = SignInMapper.toSideTab(member);

		memberRepository.save(member);
		socialMemberRepository.save(socialMember);
		sideTabRepository.save(sideTab);

		return member;
	}

	private OauthProvider getOauthProvider(String provider) {
		if (OauthProvider.GOOGLE.equalsName(provider)) {
			return OauthProvider.GOOGLE;
		}
		throw new AuthException(AuthErrorCode.NO_SUPPORT_PROVIDER);
	}
}
