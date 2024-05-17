package com.butter.wypl.auth.domain;

import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {
}
