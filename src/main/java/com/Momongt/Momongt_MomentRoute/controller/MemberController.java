package com.Momongt.Momongt_MomentRoute.controller;

import com.Momongt.Momongt_MomentRoute.dto.MemberDto;
import com.Momongt.Momongt_MomentRoute.entity.Member;
import com.Momongt.Momongt_MomentRoute.repository.MemberRepository;
import com.Momongt.Momongt_MomentRoute.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 API")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public ResponseEntity<MemberDto.SignUpResponse> signUp(@RequestBody MemberDto.SignUpRequest request) {
        return ResponseEntity.ok(memberService.signUp(request));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<MemberDto.LoginResponse> login(@RequestBody MemberDto.LoginRequest request) {
        return ResponseEntity.ok(memberService.login(request));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<MemberDto.LogoutResponse> logout(@RequestBody MemberDto.LogoutRequest request) {
        return ResponseEntity.ok(memberService.logout(request));
    }

    /** 내 정보 조회 */
    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<MemberDto> getMyInfo(@RequestAttribute("userId") Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("회원 없음"));
        return ResponseEntity.ok(memberService.getMyInfo(member));
    }

    /** 내 정보 수정 */
    @PutMapping("/me")
    @Operation(summary = "내 정보 수정", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<MemberDto.UpdateMemberResponse> updateMyInfo(
            @RequestAttribute("userId") Long userId,
            @RequestBody MemberDto.UpdateMemberRequest request) {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("회원 없음"));

        return ResponseEntity.ok(memberService.updateMyInfo(member, request));
    }

    /** 비밀번호 변경 */
    @PutMapping("/password")
    @Operation(summary = "비밀번호 변경", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<MemberDto.ChangePasswordResponse> changePassword(
            @RequestAttribute("userId") Long userId,
            @RequestBody MemberDto.ChangePasswordRequest request) {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("회원 없음"));

        return ResponseEntity.ok(memberService.changePassword(member, request));
    }
}
