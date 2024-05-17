package com.butter.wypl.member.fixture;

import java.time.LocalDate;
import java.util.ArrayList;

import com.butter.wypl.global.common.Color;
import com.butter.wypl.member.domain.CalendarTimeZone;
import com.butter.wypl.member.domain.Member;

import lombok.Getter;

@Getter
public enum MemberFixture {
	KIM_JEONG_UK(LocalDate.of(1998, 11, 24), "workju1124@gmail.com", "김세이", null, Color.labelBrown),
	JO_DA_MIN(LocalDate.of(1999, 8, 6), "jdm080620@gmail.com", "댬니", null, Color.labelBlue),
	JWA_SO_YEON(LocalDate.of(1998, 10, 5), "thdus981005@naver.com", "좌랑둥이", null, Color.labelCharcoal),
	CHOI_MIN_JUN(LocalDate.of(1994, 10, 14), "hitobi1014@gmail.com", "모코코", null, Color.labelLavender),
	LEE_JI_WON(LocalDate.of(1997, 11, 4), "leeji7031@gmail.com", "뚱이", null, Color.labelRed),
	HAN_JI_WON(LocalDate.of(1999, 8, 3), "jiwons0803@naver.com", "지롱이", null, Color.labelNavy);

	private final LocalDate birthday;
	private final String email;
	private final String nickname;
	private final String image;
	private final Color color;

	MemberFixture(LocalDate birthday, String email, String nickname, String image, Color color) {
		this.birthday = birthday;
		this.email = email;
		this.nickname = nickname;
		this.image = image;
		this.color = color;
	}

	public Member toMember() {
		return Member.builder()
			.email(email)
			.nickname(nickname)
			.profileImage(image)
			.color(color)
			.timeZone(CalendarTimeZone.KOREA)
			.memberGroups(new ArrayList<>())
			.build();
	}

	public Member toMemberWithId(int id) {
		return Member.builder()
			.id(id)
			.email(email)
			.nickname(nickname)
			.profileImage(image)
			.color(color)
			.memberGroups(new ArrayList<>())
			.timeZone(CalendarTimeZone.KOREA)
			.build();
	}
}
