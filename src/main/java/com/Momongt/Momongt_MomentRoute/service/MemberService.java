package com.Momongt.Momongt_MomentRoute.service;

import com.Momongt.Momongt_MomentRoute.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    public MemberDto getMyInfo() {
        // TODO: JWT 토큰에서 사용자 ID 추출 및 회원 정보 조회 로직 구현
        // 현재는 빈 응답 반환
        return new MemberDto();
    }

    public MemberDto.UpdateMemberResponse updateMyInfo(MemberDto.UpdateMemberRequest request) {
        // TODO: JWT 토큰에서 사용자 ID 추출 및 회원 정보 수정 로직 구현
        // 현재는 빈 응답 반환
        return new MemberDto.UpdateMemberResponse();
    }

    public MemberDto.ChangePasswordResponse changePassword(MemberDto.ChangePasswordRequest request) {
        // TODO: JWT 토큰에서 사용자 ID 추출 및 비밀번호 변경 로직 구현
        // 현재는 빈 응답 반환
        return new MemberDto.ChangePasswordResponse();
    }

    public MemberDto.LoginResponse login(MemberDto.LoginRequest request) {
        // TODO: 이메일/비밀번호 검증 및 JWT 토큰 발급 로직 구현
        // 현재는 빈 응답 반환
        return new MemberDto.LoginResponse();
    }

    public MemberDto.SignUpResponse signUp(MemberDto.SignUpRequest request) {
        // TODO: 회원가입 로직 구현 (이메일 중복 체크, 비밀번호 암호화, 사용자 저장 등)
        // 현재는 빈 응답 반환
        return new MemberDto.SignUpResponse();
    }

    public MemberDto.LogoutResponse logout(MemberDto.LogoutRequest request) {
        // TODO: Refresh Token 검증 및 만료 처리 로직 구현
        // 현재는 빈 응답 반환
        return new MemberDto.LogoutResponse();
    }
}

