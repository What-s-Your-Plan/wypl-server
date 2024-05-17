package com.butter.wypl.file.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImageUploadResponse(
		@JsonProperty("image_url")
		String imageUrl
) {
}
