/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.repository;

import com.kaleido.klinops.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
