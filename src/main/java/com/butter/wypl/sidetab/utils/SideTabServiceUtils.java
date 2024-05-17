package com.butter.wypl.sidetab.utils;

import com.butter.wypl.global.annotation.Generated;
import com.butter.wypl.sidetab.domain.SideTab;
import com.butter.wypl.sidetab.exception.SideTabErrorCode;
import com.butter.wypl.sidetab.exception.SideTabException;
import com.butter.wypl.sidetab.repository.SideTabRepository;

public class SideTabServiceUtils {

	@Generated
	private SideTabServiceUtils() {
	}

	public static SideTab findById(final SideTabRepository repository, final int id) {
		return repository.findById(id)
				.orElseThrow(() -> new SideTabException(SideTabErrorCode.NOT_EXIST_SIDE));
	}
}
