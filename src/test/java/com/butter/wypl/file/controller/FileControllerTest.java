package com.butter.wypl.file.controller;

import static com.butter.wypl.file.fixture.FileFixture.*;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import com.butter.wypl.file.data.response.ImageUploadResponse;
import com.butter.wypl.file.service.FileService;
import com.butter.wypl.global.common.ControllerTest;

class FileControllerTest extends ControllerTest {
	@Autowired
	private FileController fileController;

	@MockBean
	private FileService fileService;

	@DisplayName("사용자가 프로필 이미지를 수정한다.")
	@Test
	void updateProfileImageTest() throws Exception {
		/* Given */
		String newProfileImageUrl = "aws.image.url";

		given(fileService.uploadImage(any(MultipartFile.class)))
				.willReturn(new ImageUploadResponse(newProfileImageUrl));

		givenMockLoginMember();

		/* When */
		ResultActions actions = mockMvc.perform(
				RestDocumentationRequestBuilders.multipart("/file/v1/images")
						.file(PNG_IMAGE.getMockMultipartFile())
						.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER_VALUE)
		);

		/* Then */
		actions.andDo(print())
				.andDo(document("file/upload-image",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						requestParts(
								partWithName("image").description("업로드 요청한 이미지")
						),
						responseFields(
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("응답 메시지"),
								fieldWithPath("body.image_url").type(JsonFieldType.STRING)
										.description("업로드한 이미지 URL")
						)
				))
				.andExpect(status().isCreated());
	}
}