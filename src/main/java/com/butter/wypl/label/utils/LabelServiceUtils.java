package com.butter.wypl.label.utils;

import com.butter.wypl.label.domain.Label;
import com.butter.wypl.label.exception.LabelErrorCode;
import com.butter.wypl.label.exception.LabelException;
import com.butter.wypl.label.repository.LabelRepository;

public class LabelServiceUtils {

	public static Label getLabelByLabelId(
		final LabelRepository labelRepository,
		final int labelId
	) {
		return labelRepository.findByLabelId(labelId)
			.orElseThrow(() -> new LabelException(LabelErrorCode.NOT_FOUND));
	}
}
