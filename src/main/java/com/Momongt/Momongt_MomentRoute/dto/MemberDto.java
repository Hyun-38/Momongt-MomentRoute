package com.Momongt.Momongt_MomentRoute.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원 정보 응답 DTO")
public class MemberDto {

    @Schema(description = "유저 아이디", example = "1", required = true)
    private Long userId;

    @Schema(description = "이름", example = "HongGilDong", required = true)
    private String name;

    @Schema(description = "학년", example = "3", required = true)
    private Integer grade;

    @Schema(description = "전화번호", example = "010-1234-5678", required = true)
    private String phoneNumber;

    @Schema(description = "계정 생성일시", example = "2025-10-27T14:00:00", required = true)
    private String createdAt;

    @Schema(description = "마지막 정보 수정일시", example = "2025-10-27T14:00:00", required = true)
    private String updatedAt;

    @Schema(description = "생년월일", example = "2003-03-03")
    private String birth;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "회원 정보 수정 요청 DTO")
    public static class UpdateMemberRequest {
        @Schema(description = "수정할 이름", example = "홍길동")
        private String name;

        @Schema(description = "수정할 전화번호", example = "010-9876-5432")
        private String phoneNumber;

        @Schema(description = "수정할 생년월일", example = "2003-03-03")
        private String birth;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "회원 정보 수정 응답 DTO")
    public static class UpdateMemberResponse {
        @Schema(description = "유저 아이디", example = "1", required = true)
        private Long userId;

        @Schema(description = "이름", example = "홍길동", required = true)
        private String name;

        @Schema(description = "학년", example = "3", required = true)
        private Integer grade;

        @Schema(description = "전화번호", example = "010-9876-5432", required = true)
        private String phoneNumber;

        @Schema(description = "생년월일", example = "2003-03-03")
        private String birth;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "비밀번호 변경 요청 DTO")
    public static class ChangePasswordRequest {
        @Schema(description = "현재 비밀번호", example = "oldPassword123", required = true)
        private String currentPassword;

        @Schema(description = "새 비밀번호", example = "newPassword123", required = true)
        private String newPassword;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "비밀번호 변경 응답 DTO")
    public static class ChangePasswordResponse {
        @Schema(description = "성공 메시지", example = "비밀번호가 성공적으로 변경되었습니다.")
        private String message;

        @Schema(description = "오류 메시지", example = "현재 비밀번호가 일치하지 않습니다.")
        private String error;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "로그인 요청 DTO")
    public static class LoginRequest {
        @Schema(description = "사용자 이메일", example = "example1@email.com", required = true)
        private String email;

        @Schema(description = "로그인 비밀번호", example = "yourPassword123!", required = true)
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "로그인 응답 DTO")
    public static class LoginResponse {
        @Schema(description = "Access token", example = "eyJhbGciOiJIUzI1NiJ9...", required = true)
        private String accessToken;

        @Schema(description = "Refresh token", example = "eyJhbGciOiJIUzI1NiJ9...", required = true)
        private String refreshToken;

        @Schema(description = "토큰 타입", example = "Bearer", required = true)
        private String tokenType;

        @Schema(description = "Access token 만료 시간", example = "86400000", required = true)
        private Long expiresIn;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "에러 응답 DTO")
    public static class ErrorMessageResponse {
        @Schema(description = "에러 메시지", example = "Invalid request format.", required = true)
        private String message;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "회원가입 요청 DTO")
    public static class SignUpRequest {
        @Schema(description = "사용자 이메일", example = "example1@email.com", required = true)
        private String email;

        @Schema(description = "사용자 이름", example = "usernameSample", required = true)
        private String username;

        @Schema(description = "사용자 패스워드", example = "yourPassword123!", required = true)
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "회원가입 응답 DTO")
    public static class SignUpResponse {
        @Schema(description = "사용자 ID (PK)", example = "1", required = true)
        private Long userId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "로그아웃 요청 DTO")
    public static class LogoutRequest {
        @Schema(description = "Refresh Token (만료 처리 대상)", example = "eyJhbGciOiJIUzI1NiJ9...", required = true)
        private String refreshToken;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "로그아웃 응답 DTO")
    public static class LogoutResponse {
        @Schema(description = "로그아웃 결과 메시지", example = "Logout successful", required = true)
        private String message;
    }
}

