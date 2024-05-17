package com.butter.wypl.global.common.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
class WebInfoControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void getProfile() {
		String profile = restTemplate.getForObject("/profile", String.class);
		Assertions.assertThat(profile).isEqualTo("test");
	}
}