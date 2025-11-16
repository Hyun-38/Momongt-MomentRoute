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

    @Schema(description = "유저 아이디", example = "1")
    private Long userId;

    @Schema(description = "이메일", example = "user@example.com")
    private String email;

    @Schema(description = "이름", example = "HongGilDong")
    private String name;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "가입 일시", example = "2024-11-16T14:30:00")
    private String createdAt;

    @Schema(description = "마지막 정보 수정일시", example = "2024-11-16T14:30:00")
    private String updatedAt;

    /** Member → MemberDto 변환 */
    public static MemberDto fromEntity(Member member) {
        return MemberDto.builder()
                .userId(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
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
    @Schema(description = "회원가입 요청 DTO")
    public static class SignUpRequest {
        @Schema(description = "이메일", example = "user@example.com")
        private String email;

        @Schema(description = "비밀번호", example = "password123")
        private String password;

        @Schema(description = "이름", example = "홍길동")
        private String name;

        @Schema(description = "전화번호", example = "010-1234-5678")
        private String phoneNumber;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "회원가입 응답 DTO")
    public static class SignUpResponse {
        @Schema(description = "회원 ID")
        private Long userId;

        @Schema(description = "이메일")
        private String email;

        @Schema(description = "가입 일시")
        private String createdAt;
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
        private String email;
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
        @Schema(description = "이메일")
        private String email;

        @Schema(description = "이름")
        private String name;

        @Schema(description = "전화번호")
        private String phoneNumber;
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
        private String email;

        public static UpdateMemberResponse fromEntity(Member m) {
            return new UpdateMemberResponse(
                    m.getId(),
                    m.getName(),
                    m.getPhoneNumber(),
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
        @Schema(description = "현재 비밀번호")
        private String oldPassword;

        @Schema(description = "새 비밀번호")
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
        회원 탈퇴
    ============================================================ */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "회원 탈퇴 응답 DTO")
    public static class DeleteMemberResponse {
        private String message;
    }
}
