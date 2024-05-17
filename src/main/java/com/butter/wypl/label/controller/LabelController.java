package com.butter.wypl.label.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.butter.wypl.auth.annotation.Authenticated;
import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.global.common.Message;
import com.butter.wypl.label.data.request.LabelRequest;
import com.butter.wypl.label.data.response.AllLabelListResponse;
import com.butter.wypl.label.data.response.LabelIdResponse;
import com.butter.wypl.label.data.response.LabelListResponse;
import com.butter.wypl.label.data.response.LabelResponse;
import com.butter.wypl.label.service.LabelModifyService;
import com.butter.wypl.label.service.LabelReadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/label/v1/labels")
public class LabelController {

	private final LabelModifyService labelModifyService;
	private final LabelReadService labelReadService;

	@GetMapping("/{labelId}")
	public ResponseEntity<Message<LabelResponse>> getLabelByLabelId(
		@PathVariable int labelId
	) {
		return ResponseEntity
			.ok().body(
				Message.withBody("라벨 id로 라벨 조회 성공", labelReadService.getLabelByLabelId(labelId))
			);
	}

	@GetMapping
	public ResponseEntity<Message<LabelListResponse>> getLabelsByMemberId(
		@Authenticated AuthMember authMember
	) {
		return ResponseEntity
			.ok().body(
				Message.withBody("멤버 id로 라벨 리스트 조회 성공", labelReadService.getLabelsByMemberId(authMember.getId()))
			);
	}

	@PostMapping
	public ResponseEntity<Message<LabelResponse>> createLabel(
		@Authenticated AuthMember authMember,
		@RequestBody LabelRequest labelRequest
	) {
		return ResponseEntity
			.ok().body(
				Message.withBody("라벨 생성 성공", labelModifyService.createLabel(authMember.getId(), labelRequest))
			);
	}

	@PatchMapping("/{labelId}")
	public ResponseEntity<Message<LabelResponse>> updateLabel(
		@Authenticated AuthMember authMember,
		@PathVariable int labelId,
		@RequestBody LabelRequest labelRequest
	) {
		return ResponseEntity
			.ok().body(
				Message.withBody("라벨 수정 성공", labelModifyService.updateLabel(authMember.getId(), labelId, labelRequest))
			);
	}

	@DeleteMapping("/{labelId}")
	public ResponseEntity<Message<LabelIdResponse>> deleteLabel(
		@Authenticated AuthMember authMember,
		@PathVariable("labelId") int labelId
	) {
		return ResponseEntity
			.ok().body(
				Message.withBody("라벨 삭제 성공", labelModifyService.deleteLabel(labelId, authMember.getId()))
			);
	}

	@GetMapping("/main")
	public ResponseEntity<Message<AllLabelListResponse>> getAllLabelByMember(
		@Authenticated AuthMember authMember
	) {
		return ResponseEntity.ok(
			Message.withBody("라벨+그룹 조회 성공", labelReadService.getAllLabelsByMemberId(authMember.getId()))
		);
	}
}
