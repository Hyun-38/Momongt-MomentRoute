package com.Momongt.Momongt_MomentRoute.repository;

import com.Momongt.Momongt_MomentRoute.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이메일로 회원 찾기용 메서드
    Optional<Member> findByEmail(String email);

    // 이메일 중복 체크 자동 가능 (existsByEmail)
    boolean existsByEmail(String email);
}
