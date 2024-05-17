package com.butter.wypl.sidetab.domain.embedded;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

import com.butter.wypl.member.exception.MemberErrorCode;
import com.butter.wypl.member.exception.MemberException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DDayWidget {
	private static final LocalDate START_DATE = LocalDate.of(1970, 1, 1);
	private static final LocalDate END_DATE = LocalDate.of(2199, 12, 31);

	@Column(name = "title", length = 20)
	private String title;

	@Column(name = "d_day")
	private LocalDate value;

	private DDayWidget(
			final String title,
			final LocalDate value
	) {
		this.title = title;
		this.value = value;
	}

	public static DDayWidget of(
			final String title,
			final LocalDate value
	) {
		validateTitle(title);
		validateDate(value);
		return new DDayWidget(title, value);
	}

	private static void validateTitle(final String newTitle) {
		if (newTitle == null) {
			return;
		}
		if (newTitle.length() > 20) {
			throw new MemberException(MemberErrorCode.TOO_LONG_CONTENT);
		}
	}

	private static void validateDate(final LocalDate localDate) {
		if (localDate.isBefore(START_DATE) || localDate.isAfter(END_DATE)) {
			throw new MemberException(MemberErrorCode.INVALID_DATE);
		}
	}

	public String getDDay(TimeZone timeZone) {
		ZoneId zoneId = timeZone.toZoneId();
		ZonedDateTime zonedNow = ZonedDateTime.now(zoneId);
		LocalDate today = zonedNow.toLocalDate();
		long between = ChronoUnit.DAYS.between(today, value);
		if (between < 0) {
			return "D+" + Math.abs(between);
		}
		if (between > 0) {
			return "D-" + Math.abs(between);
		}
		return "D-DAY";
	}
}
