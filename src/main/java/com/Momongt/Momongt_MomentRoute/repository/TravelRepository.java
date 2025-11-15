package com.Momongt.Momongt_MomentRoute.repository;

import com.Momongt.Momongt_MomentRoute.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TravelRepository extends JpaRepository<Plan, Long> {

    /** 특정 사용자의 모든 여행 조회 */
    List<Plan> findByUserId(Long userId);

    /** 특정 사용자의 특정 여행 조회 */
    Optional<Plan> findByIdAndUserId(Long id, Long userId);
}
