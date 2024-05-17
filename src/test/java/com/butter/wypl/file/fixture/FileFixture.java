package com.butter.wypl.file.fixture;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.mock.web.MockMultipartFile;

import lombok.Getter;

@Getter
public enum FileFixture {
	PNG_IMAGE("image", "png", "image/png", "src/test/resources/image/image.png"),
	WEBP_IMAGE("image", "webp", "image/webp", "src/test/resources/image/image.webp");

	private final String name;
	private final String contentType;
	private final String type;
	private final String path;

	FileFixture(String name, String contentType, String type, String path) {
		this.name = name;
		this.contentType = contentType;
		this.type = type;
		this.path = path;
	}

	public MockMultipartFile getMockMultipartFile() {
		try (FileInputStream fileInputStream = new FileInputStream(path)) {
			return new MockMultipartFile(name, name + "." + contentType, type, fileInputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

