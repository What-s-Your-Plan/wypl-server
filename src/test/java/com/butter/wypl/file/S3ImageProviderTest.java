package com.butter.wypl.file;

import static com.butter.wypl.file.fixture.FileFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.butter.wypl.global.annotation.MockServiceTest;
import com.butter.wypl.global.exception.CustomException;
import com.butter.wypl.global.exception.GlobalErrorCode;

@MockServiceTest
class S3ImageProviderTest {

	@InjectMocks
	private S3ImageProvider s3ImageService;

	@Mock
	private AmazonS3Client amazonS3Client;

	@DisplayName("S3에 이미지를 업로드한다.")
	@Test
	void s3ImageSaveTest() throws MalformedURLException {
		/* Given */
		URL url = new URL("http://aws.image.png");
		given(amazonS3Client.getUrl(eq(null), anyString()))
				.willReturn(url);

		MockMultipartFile multipartFile = PNG_IMAGE.getMockMultipartFile();

		/* When & Then */
		assertThatCode(() -> s3ImageService.uploadImage(multipartFile))
				.doesNotThrowAnyException();
	}

	@DisplayName("잘못된 확장자 형식은 예외를 던진다.")
	@Test
	void checkFileExtensionTest() {
		/* Given */
		MockMultipartFile multipartFile = WEBP_IMAGE.getMockMultipartFile();

		/* When & Then */
		assertThatThrownBy(() -> s3ImageService.uploadImage(multipartFile))
				.isInstanceOf(CustomException.class)
				.hasMessageContaining(GlobalErrorCode.NOT_ALLOWED_EXTENSION.getMessage());
	}
}