package com.butter.wypl.global.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import io.findify.s3mock.S3Mock;
import lombok.RequiredArgsConstructor;

@Profile({"test"})
@RequiredArgsConstructor
@TestConfiguration
public class S3MockConfig {

	@Bean(name = "s3Mock")
	public S3Mock s3Mock() {
		return new S3Mock.Builder()
				.withPort(18080)
				.withInMemoryBackend()
				.build();
	}

	@Primary
	@Bean(name = "amazonS3", destroyMethod = "shutdown")
	public AmazonS3Client amazonS3() {
		AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder
				.EndpointConfiguration("http://127.0.0.1:18080", Regions.AP_NORTHEAST_2.name());
		return (AmazonS3Client)AmazonS3ClientBuilder
				.standard()
				.withPathStyleAccessEnabled(true)
				.withEndpointConfiguration(endpoint)
				.withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
				.build();
	}
}
