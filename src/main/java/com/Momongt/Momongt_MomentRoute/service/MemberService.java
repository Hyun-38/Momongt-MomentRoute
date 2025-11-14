package com.Momongt.Momongt_MomentRoute.service;

import com.Momongt.Momongt_MomentRoute.dto.MemberDto;
import com.Momongt.Momongt_MomentRoute.entity.Member;
import com.Momongt.Momongt_MomentRoute.entity.Role;
import com.Momongt.Momongt_MomentRoute.jwt.JwtTokenProvider;
import com.Momongt.Momongt_MomentRoute.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /** 회원가입 */
    public MemberDto.SignUpResponse signUp(MemberDto.SignUpRequest request) {
        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .birth(request.getBirth())
                .role(Role.USER)
                .build();

        memberRepository.save(member);

        return MemberDto.SignUpResponse.builder()
                .userId(member.getId())
                .email(member.getEmail())
                .build();
    }

    /** 로그인 */
    public MemberDto.LoginResponse login(MemberDto.LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("회원 없음"));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new RuntimeException("비밀번호 불일치");
        }

        String accessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getEmail(), member.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId(), member.getEmail(), member.getRole());

        return MemberDto.LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /** 로그아웃 */
    public MemberDto.LogoutResponse logout(MemberDto.LogoutRequest request) {
        return MemberDto.LogoutResponse.builder()
                .message("로그아웃 성공")
                .build();
    }

    /** 내 정보 조회 */
    public MemberDto getMyInfo(Member member) {
        return MemberDto.fromEntity(member);
    }

    /** 내 정보 수정 */
    public MemberDto.UpdateMemberResponse updateMyInfo(Member member, MemberDto.UpdateMemberRequest request) {
        member.setName(request.getName());
        member.setPhoneNumber(request.getPhoneNumber());
        member.setBirth(request.getBirth());

        memberRepository.save(member);

        return MemberDto.UpdateMemberResponse.fromEntity(member);
    }

    /** 비밀번호 변경 */
    public MemberDto.ChangePasswordResponse changePassword(Member member, MemberDto.ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            throw new RuntimeException("현재 비밀번호 불일치");
        }

        member.setPassword(passwordEncoder.encode(request.getNewPassword()));
        memberRepository.save(member);

        return MemberDto.ChangePasswordResponse.builder()
                .message("비밀번호 변경 완료")
                .build();
    }
}
