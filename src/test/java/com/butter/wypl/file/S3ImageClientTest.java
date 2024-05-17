package com.butter.wypl.file;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.util.FileCopyUtils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.butter.wypl.global.annotation.ServiceTest;
import com.butter.wypl.global.config.S3MockConfig;

import io.findify.s3mock.S3Mock;

@Import(S3MockConfig.class)
@ServiceTest
class S3ImageClientTest {

	private static final String BUCKET_NAME = "butter-wypl";

	@Autowired
	private AmazonS3 amazonS3;

	@BeforeAll
	static void setUp(
			@Autowired S3Mock s3Mock,
			@Autowired AmazonS3 amazonS3
	) {
		s3Mock.start();
		amazonS3.createBucket(BUCKET_NAME);
	}

	@AfterAll
	static void tearDown(
			@Autowired S3Mock s3Mock,
			@Autowired AmazonS3 amazonS3
	) {
		amazonS3.shutdown();
		s3Mock.stop();
	}

	@DisplayName("이미지를 S3에 저장한다.")
	@Test
	void saveImage() throws IOException {
		/* Given */
		String path = "image/image.png";
		String contentType = "image/png";

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(contentType);
		PutObjectRequest putObjectRequest = new PutObjectRequest(
				BUCKET_NAME, path,
				new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)),
				objectMetadata);
		amazonS3.putObject(putObjectRequest);

		/* When */
		S3Object s3Object = amazonS3.getObject(BUCKET_NAME, path);

		/* Then */
		assertThat(s3Object.getObjectMetadata().getContentType()).isEqualTo(contentType);
		assertThat(new String(FileCopyUtils.copyToByteArray(s3Object.getObjectContent()))).isEqualTo("");
	}
}