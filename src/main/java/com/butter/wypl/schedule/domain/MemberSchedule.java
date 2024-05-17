package com.butter.wypl.schedule.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;

import com.butter.wypl.global.common.BaseEntity;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.review.domain.Review;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@SQLRestriction("deleted_at is null")
public class MemberSchedule extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_schedule_id")
	private int memberScheduleId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "schedule_id", nullable = false)
	private Schedule schedule;

	@Column(name = "write_review", nullable = false)
	private boolean writeReview;

	@OneToMany(mappedBy = "memberSchedule", fetch = FetchType.LAZY)
	private List<Review> reviews;

	public static MemberSchedule of(int memberScheduleId, Member memberWithId, Schedule schedule) {
		return MemberSchedule.builder()
			.memberScheduleId(memberScheduleId)
			.member(memberWithId)
			.schedule(schedule)
			.reviews(new ArrayList<>())
			.writeReview(false)
			.build();
	}

	public static MemberSchedule of(Member member, Schedule schedule) {
		return MemberSchedule.builder()
			.member(member)
			.schedule(schedule)
			.reviews(new ArrayList<>())
			.writeReview(false)
			.build();
	}

	public void writeReview() {
		writeReview = true;
	}

}
