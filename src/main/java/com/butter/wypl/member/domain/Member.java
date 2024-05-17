package com.butter.wypl.member.domain;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.butter.wypl.global.common.BaseEntity;
import com.butter.wypl.global.common.Color;
import com.butter.wypl.group.domain.MemberGroup;
import com.butter.wypl.infrastructure.weather.WeatherRegion;
import com.butter.wypl.member.exception.MemberErrorCode;
import com.butter.wypl.member.exception.MemberException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private int id;

	@Column(name = "email", length = 50, unique = true, nullable = false)
	private String email;

	@Column(name = "birthday")
	private LocalDate birthday;

	@Column(name = "nickname", length = 20, nullable = false)
	private String nickname;

	@Column(name = "profile_image", length = 100)
	private String profileImage;

	@Enumerated(EnumType.STRING)
	@Column(name = "color", length = 20, nullable = false)
	private Color color;

	@Enumerated(EnumType.STRING)
	@Column(name = "timezone", length = 10, nullable = false)
	private CalendarTimeZone timeZone;
	
	@OneToMany(mappedBy = "member")
	private List<MemberGroup> memberGroups;

	public WeatherRegion getWeatherRegion() {
		return Arrays.stream(CalendarTimeZone.values())
			.flatMap(calendarTimeZone -> Arrays.stream(WeatherRegion.values())
				.filter(weatherRegion -> calendarTimeZone.getTimeZone()
					.getDisplayName()
					.equals(weatherRegion.getTimeZone()))
			).findFirst()
			.orElse(WeatherRegion.KOREA);
	}

	public void changeBirthday(final LocalDate newBirthday) {
		validateBirthday(newBirthday);
		this.birthday = newBirthday;
	}

	private void validateBirthday(final LocalDate birthday) {
		if (birthday.plusDays(1L).isAfter(LocalDate.now())) {
			throw new MemberException(MemberErrorCode.BIRTHDAYS_CANNOT_BE_IN_THE_FUTURE);
		}
	}

	public void changeNickname(final String newNickname) {
		validateNickname(newNickname);
		this.nickname = newNickname;
	}

	private void validateNickname(final String newNickname) {
		if (newNickname == null || newNickname.isBlank()) {
			throw new MemberException(MemberErrorCode.NICKNAME_IS_NOT_BLANK);
		}
		if (newNickname.length() > 20) {
			throw new MemberException(MemberErrorCode.TOO_LONG_NICKNAME);
		}
	}

	public void changeTimezone(final CalendarTimeZone newTimezone) {
		timeZone = newTimezone;
	}

	public void changeProfileImage(final String newProfileImage) {
		profileImage = newProfileImage;
	}

	public void changeColor(final Color newColor) {
		color = newColor;
	}
}
