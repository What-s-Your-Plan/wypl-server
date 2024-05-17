package com.butter.wypl.file.service;

import static com.butter.wypl.file.fixture.FileFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import com.butter.wypl.file.S3ImageProvider;
import com.butter.wypl.file.data.response.ImageUploadResponse;
import com.butter.wypl.global.annotation.MockServiceTest;

@MockServiceTest
class FileServiceTest {
	@InjectMocks
	private FileService fileService;

	@Mock
	private S3ImageProvider provider;

	@DisplayName("사진 업로드에 성공한다.")
	@Test
	void imageUploadTest() {
		/* Given */
		MockMultipartFile multipartFile = PNG_IMAGE.getMockMultipartFile();

		String uploadImageUrl = "imageUrl";

		given(provider.uploadImage(any(MockMultipartFile.class)))
				.willReturn(uploadImageUrl);

		/* When */
		ImageUploadResponse res = fileService.uploadImage(multipartFile);

		/* Then */
		assertThat(res.imageUrl()).isEqualTo(uploadImageUrl);
	}
}