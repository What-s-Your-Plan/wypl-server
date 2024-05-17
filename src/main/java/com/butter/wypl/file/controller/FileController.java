package com.butter.wypl.file.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.butter.wypl.auth.annotation.Authenticated;
import com.butter.wypl.auth.domain.AuthMember;
import com.butter.wypl.file.data.response.ImageUploadResponse;
import com.butter.wypl.file.service.FileService;
import com.butter.wypl.global.common.Message;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/file")
@RestController
public class FileController {

	private final FileService fileService;

	@PostMapping("/v1/images")
	public ResponseEntity<Message<ImageUploadResponse>> uploadImage(
			@Authenticated AuthMember authMember,
			@RequestParam("image") MultipartFile file
	) {
		ImageUploadResponse response = fileService.uploadImage(file);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(Message.withBody("사진 저장에 성공하였습니다.", response));
	}
}
