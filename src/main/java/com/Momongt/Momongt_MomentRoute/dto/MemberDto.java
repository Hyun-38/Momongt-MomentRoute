package com.Momongt.Momongt_MomentRoute.dto;

import com.Momongt.Momongt_MomentRoute.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "회원 정보 DTO")
public class MemberDto {

    @Schema(description = "유저 아이디", example = "1", required = true)
    private Long userId;

    @Schema(description = "이름", example = "HongGilDong", required = true)
    private String name;

    @Schema(description = "전화번호", example = "010-1234-5678", required = true)
    private String phoneNumber;

    @Schema(description = "생년월일", example = "2003-03-03")
    private String birth;

    @Schema(description = "계정 생성일시")
    private String createdAt;

    @Schema(description = "마지막 정보 수정일시")
    private String updatedAt;

    /** Member → MemberDto 변환 */
    public static MemberDto fromEntity(Member member) {
        return MemberDto.builder()
                .userId(member.getId())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .birth(member.getBirth())
                .createdAt(member.getCreatedAt() != null ? member.getCreatedAt().toString() : null)
                .updatedAt(member.getUpdatedAt() != null ? member.getUpdatedAt().toString() : null)
                .build();
    }

    /* ============================================================
        회원가입
    ============================================================ */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUpRequest {
        private String email;
        private String password;
        private String name;
        private String phoneNumber;
        private String birth;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "회원가입 응답 DTO")
    public static class SignUpResponse {
        private Long userId;
        private String email;
    }

    /* ============================================================
        로그인
    ============================================================ */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "로그인 요청 DTO")
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "로그인 응답 DTO")
    public static class LoginResponse {
        private String accessToken;
        private String refreshToken;
        private String tokenType;
        private Long expiresIn;
    }

    /* ============================================================
        로그아웃
    ============================================================ */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogoutRequest {
        private String refreshToken;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LogoutResponse {
        private String message;
    }

    /* ============================================================
        회원 정보 수정
    ============================================================ */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "회원 정보 수정 요청 DTO")
    public static class UpdateMemberRequest {
        private String name;
        private String phoneNumber;
        private String birth;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "회원 정보 수정 응답 DTO")
    public static class UpdateMemberResponse {
        private Long userId;
        private String name;
        private String phoneNumber;
        private String birth;
        private String email;

        public static UpdateMemberResponse fromEntity(Member m) {
            return new UpdateMemberResponse(
                    m.getId(),
                    m.getName(),
                    m.getPhoneNumber(),
                    m.getBirth(),
                    m.getEmail()
            );
        }
    }

    /* ============================================================
        비밀번호 변경
    ============================================================ */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "비밀번호 변경 요청 DTO")
    public static class ChangePasswordRequest {
        private String currentPassword;
        private String newPassword;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "비밀번호 변경 응답 DTO")
    public static class ChangePasswordResponse {
        private String message;
    }

    /* ============================================================
        공통 에러 메시지
    ============================================================ */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "에러 메시지 응답 DTO")
    public static class ErrorMessageResponse {
        private String message;
    }
}
