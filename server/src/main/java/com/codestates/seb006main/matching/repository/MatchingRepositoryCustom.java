package com.codestates.seb006main.matching.repository;

import com.codestates.seb006main.matching.entity.Matching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface MatchingRepositoryCustom {
    List<Matching> findClosedMatching();
    Page<Matching> findAllNotMatched(PageRequest pageRequest);
}
