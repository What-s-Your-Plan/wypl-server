package com.butter.wypl.sidetab.domain;

import java.time.LocalDate;

import com.butter.wypl.member.domain.Member;
import com.butter.wypl.sidetab.domain.embedded.DDayWidget;
import com.butter.wypl.sidetab.domain.embedded.GoalWidget;
import com.butter.wypl.sidetab.domain.embedded.MemoWidget;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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
public class SideTab {
	@Id
	@Column(name = "member_id")
	private int id;

	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Embedded
	private MemoWidget memo;

	@Embedded
	private DDayWidget dDay;

	@Embedded
	private GoalWidget goal;

	private SideTab(Member member) {
		this.member = member;
		memo = MemoWidget.from("");
		dDay = DDayWidget.of("디데이", LocalDate.now());
		goal = GoalWidget.from("");
	}

	public static SideTab from(Member member) {
		return new SideTab(member);
	}

	public int getMemberId() {
		return member.getId();
	}

	public String getMemo() {
		return memo.getValue();
	}

	public void updateMemo(final MemoWidget memo) {
		this.memo = memo;
	}

	public String getGoal() {
		return goal.getValue();
	}

	public void updateGoal(final GoalWidget newGoal) {
		goal = newGoal;
	}

	public void updateDDay(final DDayWidget newDDay) {
		this.dDay = newDDay;
	}

	public String getDDayTitle() {
		return dDay.getTitle();
	}

	public LocalDate getDDayDate() {
		return dDay.getValue();
	}
}
