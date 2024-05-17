package com.butter.wypl.schedule.service;

import com.butter.wypl.schedule.data.response.ScheduleDetailResponse;
import com.butter.wypl.schedule.data.response.ScheduleResponse;

public interface ScheduleReadService {

	ScheduleDetailResponse getDetailScheduleByScheduleId(int memberId, int scheduleId);

	ScheduleResponse getScheduleByScheduleId(int memberId, int scheduleId);
}
