package com.butter.wypl.schedule.service;

import org.springframework.transaction.annotation.Transactional;

import com.butter.wypl.global.annotation.FacadeService;
import com.butter.wypl.schedule.domain.Repetition;
import com.butter.wypl.schedule.respository.RepetitionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@FacadeService
public class RepetitionService {

	private final RepetitionRepository repetitionRepository;

	@Transactional
	public Repetition createRepetition(Repetition repetition) {
		return repetitionRepository.save(repetition);
	}

	@Transactional
	public void deleteRepetition(Repetition repetition) {
		if (repetition == null) {
			return;
		}

		repetition.delete();
	}

}
