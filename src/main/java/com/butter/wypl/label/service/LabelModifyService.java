package com.butter.wypl.label.service;

import com.butter.wypl.label.data.request.LabelRequest;
import com.butter.wypl.label.data.response.LabelIdResponse;
import com.butter.wypl.label.data.response.LabelResponse;

public interface LabelModifyService {
	LabelResponse createLabel(int memberId, LabelRequest labelRequest);

	LabelResponse updateLabel(int memberId, int labelId, LabelRequest labelRequest);

	LabelIdResponse deleteLabel(int labelId, int memberId);
}
