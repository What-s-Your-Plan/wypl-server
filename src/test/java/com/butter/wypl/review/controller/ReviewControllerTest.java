package com.butter.wypl.review.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import com.butter.wypl.global.common.ControllerTest;
import com.butter.wypl.member.domain.Member;
import com.butter.wypl.member.fixture.MemberFixture;
import com.butter.wypl.review.data.request.ReviewCreateRequest;
import com.butter.wypl.review.data.request.ReviewType;
import com.butter.wypl.review.data.request.ReviewUpdateRequest;
import com.butter.wypl.review.data.response.ReviewDetailResponse;
import com.butter.wypl.review.data.response.ReviewIdResponse;
import com.butter.wypl.review.data.response.ReviewListResponse;
import com.butter.wypl.review.data.response.ReviewResponse;
import com.butter.wypl.review.domain.Review;
import com.butter.wypl.review.fixture.ReviewContentsFixture;
import com.butter.wypl.review.fixture.ReviewFixture;
import com.butter.wypl.review.service.ReviewModifyService;
import com.butter.wypl.review.service.ReviewReadService;
import com.butter.wypl.schedule.domain.MemberSchedule;
import com.butter.wypl.schedule.domain.Schedule;
import com.butter.wypl.schedule.fixture.ScheduleFixture;

public class ReviewControllerTest extends ControllerTest {

	private final ReviewController reviewController;

	@MockBean
	private ReviewModifyService reviewModifyService;

	@MockBean
	private ReviewReadService reviewReadService;

	@Autowired
	public ReviewControllerTest(ReviewController reviewController) {
		this.reviewController = reviewController;
	}

	@Test
	@DisplayName("리뷰 생성")
	void createReview() throws Exception {
		// Given
		Schedule schedule = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
		MemberSchedule memberSchedule = MemberSchedule.builder()
			.member(MemberFixture.JWA_SO_YEON.toMember())
			.schedule(schedule)
			.build();
		Review review = ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule);
		ReviewCreateRequest reviewCreateRequest = ReviewCreateRequest.builder()
			.title(review.getTitle())
			.scheduleId(schedule.getScheduleId())
			.contents(ReviewContentsFixture.REVIEW_CONTENTS.toReviewContents().getContents())
			.build();

		given(reviewModifyService.createReview(anyInt(), any(ReviewCreateRequest.class)))
			.willReturn(new ReviewIdResponse(1));

		String json = convertToJson(reviewCreateRequest);

		// When
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/review/v1/reviews")
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
		);

		// Then
		resultActions.andDo(print())
			.andDo(document("review/create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("title").type(JsonFieldType.STRING).description("리뷰 제목"),
					fieldWithPath("schedule_id").type(JsonFieldType.NUMBER).description("리뷰와 연결된 일정의 인덱스"),
					fieldWithPath("contents").description("리뷰에 컨텐츠 리스트"),
					fieldWithPath("contents[].*").description("컨텐츠 내용")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
					fieldWithPath("body.review_id").type(JsonFieldType.NUMBER).description("생성한 리뷰의 엔덱스")
				)))
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("리뷰 수정")
	void updateReview() throws Exception {
		// Given
		Schedule schedule = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
		MemberSchedule memberSchedule = MemberSchedule.builder()
			.member(MemberFixture.JWA_SO_YEON.toMember())
			.schedule(schedule)
			.build();
		Review review = ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule);
		ReviewUpdateRequest reviewUpdateRequest = ReviewUpdateRequest.builder()
			.title(review.getTitle())
			.scheduleId(schedule.getScheduleId())
			.contents(ReviewContentsFixture.REVIEW_CONTENTS.toReviewContents().getContents())
			.build();

		given(reviewModifyService.updateReview(anyInt(), anyInt(), any(ReviewUpdateRequest.class)))
			.willReturn(new ReviewIdResponse(1));

		String json = convertToJson(reviewUpdateRequest);

		// When
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.patch("/review/v1/reviews/{reviewId}", 1)
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
		);

		// Then
		resultActions.andDo(print())
			.andDo(document("review/update",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("reviewId").description("수정할 리뷰 인덱스")
				),
				requestFields(
					fieldWithPath("title").type(JsonFieldType.STRING).description("리뷰 제목"),
					fieldWithPath("schedule_id").type(JsonFieldType.NUMBER).description("리뷰와 연결된 일정의 인덱스"),
					fieldWithPath("contents").description("리뷰에 컨텐츠 리스트"),
					fieldWithPath("contents[].*").description("컨텐츠 내용")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
					fieldWithPath("body.review_id").type(JsonFieldType.NUMBER).description("수정한 리뷰의 엔덱스")
				)))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("리뷰 삭제")
	void deleteReview() throws Exception {
		// Given

		given(reviewModifyService.deleteReview(anyInt(), anyInt()))
			.willReturn(new ReviewIdResponse(1));

		// When
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.delete("/review/v1/reviews/{reviewId}", 1)
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		resultActions.andDo(print())
			.andDo(document("review/delete",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("reviewId").description("리뷰 id")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
					fieldWithPath("body.review_id").type(JsonFieldType.NUMBER).description("수정한 리뷰의 엔덱스")
				)))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("리뷰 상세 조회")
	void getDetailReview() throws Exception {
		// Given
		Schedule schedule = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
		Member member1 = MemberFixture.JWA_SO_YEON.toMember();
		MemberSchedule memberSchedule = MemberSchedule.builder()
			.member(member1)
			.schedule(schedule)
			.build();
		Review review = ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule);

		given(reviewReadService.getDetailReview(anyInt(), anyInt()))
			.willReturn(ReviewDetailResponse.of(review, schedule,
				List.of(member1), ReviewContentsFixture.REVIEW_CONTENTS.toReviewContents().getContents()));

		// When
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/review/v1/reviews/detail/{reviewId}", 1)
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		resultActions.andDo(print())
			.andDo(document("review/getDetailReview",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("reviewId").description("리뷰 id")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
					fieldWithPath("body.review_id").type(JsonFieldType.NUMBER).description("리뷰의 엔덱스"),
					fieldWithPath("body.title").type(JsonFieldType.STRING).description("리뷰 제목"),
					fieldWithPath("body.schedule.schedule_id").type(JsonFieldType.NUMBER).description("일정 id"),
					fieldWithPath("body.schedule.title").type(JsonFieldType.STRING).description("일정 제목"),
					fieldWithPath("body.schedule.description").type(JsonFieldType.STRING).description("일정 묘사"),
					fieldWithPath("body.schedule.start_date").type(JsonFieldType.STRING).description("일정 시작 일"),
					fieldWithPath("body.schedule.end_date").type(JsonFieldType.STRING).description("일정 끝 일"),
					fieldWithPath("body.schedule.category").type(JsonFieldType.STRING).description("일정 종류"),
					fieldWithPath("body.schedule.group_id").optional()
						.type(JsonFieldType.NUMBER)
						.description("그룹 일정의 그룹 id"),
					fieldWithPath("body.schedule.label").optional().description("일정 라벨"),
					fieldWithPath("body.schedule.label.label_id").optional()
						.type(JsonFieldType.NUMBER)
						.description("라벨 인덱스"),
					fieldWithPath("body.schedule.label.member_id").optional()
						.type(JsonFieldType.NUMBER)
						.description("라벨 주인의 인덱스"),
					fieldWithPath("body.schedule.label.title").optional()
						.type(JsonFieldType.STRING)
						.description("라벨 제목"),
					fieldWithPath("body.schedule.label.color").optional()
						.type(JsonFieldType.STRING)
						.description("라벨 색상"),
					fieldWithPath("body.schedule.member_count").type(JsonFieldType.NUMBER).description("스케줄의 참여 멤버 수"),
					fieldWithPath("body.schedule.members[].member_id").type(JsonFieldType.NUMBER)
						.description("참여 멤버의 인덱스"),
					fieldWithPath("body.schedule.members[].nickname").type(JsonFieldType.STRING)
						.description("참여 멤버의 닉네임"),
					fieldWithPath("body.schedule.members[].profile_image").type(JsonFieldType.STRING).optional()
						.description("참여 멤버의 프로필 이미지"),
					fieldWithPath("body.contents").description("리뷰에 컨텐츠 리스트"),
					fieldWithPath("body.contents[].*").description("컨텐츠 내용")
				)))
			.andExpect(status().isOk());

	}

	@Test
	@DisplayName("멤버 별 리뷰 목록 조회")
	void getReviewsByMember() throws Exception {
		// Given
		Schedule schedule = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
		Member member1 = MemberFixture.JWA_SO_YEON.toMember();
		MemberSchedule memberSchedule = MemberSchedule.builder()
			.member(member1)
			.schedule(schedule)
			.build();
		Review review1 = ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule);
		Review review2 = ReviewFixture.STUDY_REVIEW_2.toReviewWithMemberSchedule(memberSchedule);

		given(reviewReadService.getReviews(anyInt(), anyInt(), any(ReviewType.class), any(LocalDate.class),
			any(LocalDate.class)))
			.willReturn(ReviewListResponse.from(List.of(
				ReviewResponse.from(review1,
					ReviewContentsFixture.REVIEW_CONTENTS.toReviewContents().getContents().get(0)),
				ReviewResponse.from(review2, ReviewContentsFixture.REVIEW_CONTENTS.toReviewContents().getContents()
					.get(1))
			)));
		// When
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/review/v1/reviews/{type}", ReviewType.OLDEST)
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
				.queryParam("lastReviewId", "1")
				.queryParam("startDate", "2024-05-10")
				.queryParam("endDate", "2024-05-11")
		);

		// Then
		resultActions.andDo(print())
			.andDo(document("review/getReviewsByMember",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				queryParameters(
					parameterWithName("lastReviewId").optional().description("가장 나중에 조회한 회고 인덱스"),
					parameterWithName("startDate").optional().description("시작 기준일"),
					parameterWithName("endDate").optional().description("끝 기준일"),
					parameterWithName("date").optional().description("기준 날짜")
				),
				pathParameters(
					parameterWithName("type").description("최신순 or 오래된 순")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
					fieldWithPath("body.review_count").type(JsonFieldType.NUMBER).description("리뷰 개수"),
					fieldWithPath("body.reviews[].review_id").type(JsonFieldType.NUMBER).description("리뷰 인덱스"),
					fieldWithPath("body.reviews[].created_at").type(JsonFieldType.STRING).description("리뷰 생성일"),
					fieldWithPath("body.reviews[].title").type(JsonFieldType.STRING).description("리뷰 제목"),
					fieldWithPath("body.reviews[].thumbnail_content.*").description("리뷰의 썸네일 컨텐츠")
				)))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("일정 별 리뷰 목록 조회")
	void getReviewsBySchedule() throws Exception {
		// Given
		Schedule schedule = ScheduleFixture.PERSONAL_SCHEDULE.toSchedule();
		Member member1 = MemberFixture.JWA_SO_YEON.toMember();
		MemberSchedule memberSchedule = MemberSchedule.builder()
			.member(member1)
			.schedule(schedule)
			.build();
		Review review1 = ReviewFixture.STUDY_REVIEW.toReviewWithMemberSchedule(memberSchedule);
		Review review2 = ReviewFixture.STUDY_REVIEW_2.toReviewWithMemberSchedule(memberSchedule);

		given(reviewReadService.getReviewsByScheduleId(anyInt(), anyInt(), any(ReviewType.class)))
			.willReturn(ReviewListResponse.from(List.of(
				ReviewResponse.from(review1,
					ReviewContentsFixture.REVIEW_CONTENTS.toReviewContents().getContents().get(0)),
				ReviewResponse.from(review2, ReviewContentsFixture.REVIEW_CONTENTS.toReviewContents().getContents()
					.get(1))
			)));
		// When
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/review/v1/reviews/{type}/{scheduleId}", ReviewType.OLDEST, 1)
				.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
				.contentType(MediaType.APPLICATION_JSON)
		);

		// Then
		resultActions.andDo(print())
			.andDo(document("review/getReviewsByMember",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				pathParameters(
					parameterWithName("type").description("최신순 or 오래된 순"),
					parameterWithName("scheduleId").description("검색하고 싶은 스케줄 인덱스")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
					fieldWithPath("body.review_count").type(JsonFieldType.NUMBER).description("리뷰 개수"),
					fieldWithPath("body.reviews[].review_id").type(JsonFieldType.NUMBER).description("리뷰 인덱스"),
					fieldWithPath("body.reviews[].created_at").type(JsonFieldType.STRING).description("리뷰 생성일"),
					fieldWithPath("body.reviews[].title").type(JsonFieldType.STRING).description("리뷰 제목"),
					fieldWithPath("body.reviews[].thumbnail_content.*").description("리뷰의 썸네일 컨텐츠")
				)))
			.andExpect(status().isOk());
	}
}
