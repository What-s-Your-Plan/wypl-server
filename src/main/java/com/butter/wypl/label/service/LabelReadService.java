package com.butter.wypl.label.service;

import com.butter.wypl.label.data.response.AllLabelListResponse;
import com.butter.wypl.label.data.response.LabelListResponse;
import com.butter.wypl.label.data.response.LabelResponse;

public interface LabelReadService {
	LabelResponse getLabelByLabelId(int labelId);

	LabelListResponse getLabelsByMemberId(int memberId);

	AllLabelListResponse getAllLabelsByMemberId(int memberId);
}
