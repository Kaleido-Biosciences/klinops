/*
 * Copyright (c) 2019. Kaleido Biosciences. All Rights Reserved.
 */

package com.kaleido.klinops.repository.timezone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link DateTimeWrapper} entity.
 */
@Repository
public interface DateTimeWrapperRepository extends JpaRepository<DateTimeWrapper, Long> {

}
