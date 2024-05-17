package com.butter.wypl.member.service;

import static com.butter.wypl.file.fixture.FileFixture.*;
import static com.butter.wypl.member.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.file.S3ImageProvider;
import com.butter.wypl.global.annotation.MockServiceTest;
import com.butter.wypl.global.common.Color;
import com.butter.wypl.member.data.request.MemberBirthdayUpdateRequest;
import com.butter.wypl.member.data.request.MemberColorUpdateRequest;
import com.butter.wypl.member.data.request.MemberNicknameUpdateRequest;
import com.butter.wypl.member.data.request.MemberTimezoneUpdateRequest;
import com.butter.wypl.member.data.response.MemberBirthdayUpdateResponse;
import com.butter.wypl.member.data.response.MemberColorUpdateResponse;
import com.butter.wypl.member.data.response.MemberNicknameUpdateResponse;
import com.butter.wypl.member.data.response.MemberTimezoneUpdateResponse;
import com.butter.wypl.member.domain.CalendarTimeZone;
import com.butter.wypl.member.repository.MemberRepository;

@MockServiceTest
class MemberModifyServiceTest {

	@InjectMocks
	private MemberServiceImpl memberService;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private S3ImageProvider s3ImageProvider;

	private AuthMember authMember;

	@BeforeEach
	void setUp() {
		authMember = AuthMember.from(0);
	}

	@DisplayName("회원 수정 테스트")
	@Nested
	class MemberUpdateTest {

		@DisplayName("닉네임 수정 테스트")
		@Test
		void updateNicknameTest() {
			/* Given */
			given(memberRepository.findById(any(Integer.class)))
					.willReturn(Optional.of(KIM_JEONG_UK.toMember()));

			MemberNicknameUpdateRequest request = new MemberNicknameUpdateRequest("wypl");

			/* When */
			MemberNicknameUpdateResponse response = memberService.updateNickname(authMember, request);

			/* Then */
			assertThat(response.nickname()).isEqualTo(request.nickname());
		}

		@DisplayName("생일 수정 테스트")
		@Test
		void updateBirthdayTest() {
			/* Given */
			given(memberRepository.findById(any(Integer.class)))
					.willReturn(Optional.of(KIM_JEONG_UK.toMember()));

			MemberBirthdayUpdateRequest request = new MemberBirthdayUpdateRequest(KIM_JEONG_UK.getBirthday());

			/* When */
			MemberBirthdayUpdateResponse response = memberService.updateBirthday(authMember, request);

			/* Then */
			assertThat(response.birthday()).isEqualTo(request.birthday());
		}

		@DisplayName("회원의 타임존을 수정한다.")
		@Test
		void updateTimezoneTest() {
			/* Given */
			MemberTimezoneUpdateRequest request = new MemberTimezoneUpdateRequest(CalendarTimeZone.ENGLAND);
			given(memberRepository.findById(any(Integer.class)))
					.willReturn(Optional.of(KIM_JEONG_UK.toMember()));

			/* When */
			MemberTimezoneUpdateResponse response = memberService.updateTimezone(authMember, request);

			/* Then */
			assertThat(request.timeZone()).isEqualTo(response.timeZone());
		}

		@DisplayName("회원의 프로필 이미지를 수정한다.")
		@Test
		void updateProfileImageTest() {
			/* Given */
			MockMultipartFile multipartFile = PNG_IMAGE.getMockMultipartFile();

			given(memberRepository.findById(any(Integer.class)))
					.willReturn(Optional.of(KIM_JEONG_UK.toMember()));
			given(s3ImageProvider.uploadImage(any(MultipartFile.class)))
					.willReturn(anyString());

			/* When & Then */
			assertThatCode(() -> memberService.updateProfileImage(authMember, multipartFile))
					.doesNotThrowAnyException();
		}

		@DisplayName("회원의 컬러를 수정한다.")
		@ParameterizedTest
		@EnumSource(Color.class)
		void updateColorTest(Color color) {
			/* Given */
			MemberColorUpdateRequest request = new MemberColorUpdateRequest(color);
			given(memberRepository.findById(any(Integer.class)))
					.willReturn(Optional.of(KIM_JEONG_UK.toMember()));

			/* When */
			MemberColorUpdateResponse response = memberService.updateColor(authMember, request);

			/* Then */
			assertThat(response.color()).isEqualTo(color);
		}
	}
}