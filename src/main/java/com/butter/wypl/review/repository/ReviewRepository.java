package com.butter.wypl.review.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.butter.wypl.review.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

	Review getByReviewId(int reviewId);

	//일정 별 조회
	//오래된순
//	List<Review> getReviewsByMemberScheduleOrderByCreatedAt(MemberSchedule memberSchedule);

	//최신순
//	List<Review> getReviewsByMemberScheduleOrderByCreatedAtDesc(MemberSchedule memberSchedule);

	//리뷰 조회
	//1. 날짜 설정 안한 경우(오래된 순, 모든 리뷰, 무한 스크롤)
//	List<Review> getReviewsOldestAll(
//		@Param("member_id") int memberId,
//		@Param("last_review_id") int lastReviewId
//	);
//
//	//2-1. 날짜 설정 안한 경우(최신순, 모든 리뷰, 무한스크롤, 첫번째)
//	List<Review> getReviewsNewestAll(
//		@Param("member_id") int memberId
//	);
//
//	//2-2. 날짜 설정 안한 경우(최신순, 모든 리뷰, 무한스크롤, 첫번째 이후)
//	List<Review> getReviewsNewestAllAfter(
//		@Param("member_id") int memberId,
//		@Param("last_review_id") int lastReviewId
//	);
//
//	//3. 날짜 설정한 경우(오래된 순, 무한 스크롤)
//	List<Review> getReviewsOldest(
//		@Param("member_id") int memberId,
//		@Param("last_review_id") int lastReviewId,
//		@Param("start_date") LocalDateTime startDate,
//		@Param("end_date") LocalDateTime endDate
//	);
//
//	//4-1. 날짜 설정한 경우(최신순 순, 무한 스크롤, 처음 이후)
//	List<Review> getReviewsNewestAfter(
//		@Param("member_id") int memberId,
//		@Param("last_review_id") int lastReviewId,
//		@Param("start_date") LocalDateTime startDate,
//		@Param("end_date") LocalDateTime endDate
//	);
//
//	//4-2. 날짜 설정한 경우(최신순 순, 무한 스크롤, 처음)
//	List<Review> getReviewsNewest(
//		@Param("member_id") int memberId,
//		@Param("start_date") LocalDateTime startDate,
//		@Param("end_date") LocalDateTime endDate
//	);
}
