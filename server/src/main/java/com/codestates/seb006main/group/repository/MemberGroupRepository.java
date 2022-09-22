package com.codestates.seb006main.group.repository;

import com.codestates.seb006main.group.entity.MemberGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberGroupRepository extends JpaRepository<MemberGroup,Long> {
}
