package com.butter.wypl.file.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.butter.wypl.file.S3ImageProvider;
import com.butter.wypl.file.data.response.ImageUploadResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FileService {
	private final S3ImageProvider provider;

	public ImageUploadResponse uploadImage(MultipartFile file) {
		String url = provider.uploadImage(file);

		return new ImageUploadResponse(url);
	}
}
