package com.butter.wypl.review.domain;

import com.butter.wypl.member.domain.Member;
import com.butter.wypl.schedule.domain.ScheduleInfo;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

import com.butter.wypl.global.common.BaseEntity;
import com.butter.wypl.review.data.request.ReviewCreateRequest;
import com.butter.wypl.review.exception.ReviewErrorCode;
import com.butter.wypl.review.exception.ReviewException;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLRestriction("deleted_at is null")
@Table(name = "review_tbl")
public class Review extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id")
	private int reviewId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schedule_info_id")
	private ScheduleInfo scheduleInfo;

	@Column(nullable = false, length = 50)
	private String title;


	@Builder
	public Review(int reviewId, String title) {
		validateTitle(title);
		this.reviewId = reviewId;
		this.title = title;
	}

	public static Review of(ReviewCreateRequest reviewCreateRequest) {
		return Review.builder()
			.title(reviewCreateRequest.title())
			.build();
	}

	public void updateTitle(String title) {
		validateTitle(title);
		this.title = title;
	}

	public void validationOwnerByMemberId(int memberId) {
		if (member.getId() != memberId) {
			throw new ReviewException(ReviewErrorCode.NOT_PERMISSION_TO_REVIEW);
		}
	}

	private void validateTitle(String title) {
		if (title == null || title.length() > 30 || title.isEmpty()) {
			throw new ReviewException(ReviewErrorCode.NOT_APPROPRIATE_TITLE);
		}
	}
}
