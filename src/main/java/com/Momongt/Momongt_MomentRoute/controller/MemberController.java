package com.Momongt.Momongt_MomentRoute.controller;

import com.Momongt.Momongt_MomentRoute.dto.MemberDto;
import com.Momongt.Momongt_MomentRoute.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "내 정보 조회",
            description = "현재 로그인한 사용자의 정보를 조회합니다. 인증이 필요합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공적으로 내 정보 조회",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberDto.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"userId\": 1,\n" +
                                            "  \"name\": \"HongGilDong\",\n" +
                                            "  \"grade\": 3,\n" +
                                            "  \"phoneNumber\": \"010-1234-5678\",\n" +
                                            "  \"createdAt\": \"2025-10-27T14:00:00\",\n" +
                                            "  \"updatedAt\": \"2025-10-27T14:00:00\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "인증 실패 (토큰이 없거나 유효하지 않음)"),
            @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/me")
    public ResponseEntity<MemberDto> getMyInfo(
            @Parameter(description = "JWT 토큰", required = true, hidden = true)
            @RequestHeader("Authorization") String authorization
    ) {
        MemberDto response = memberService.getMyInfo();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "내 정보 수정",
            description = "현재 로그인한 사용자의 정보를 수정합니다. 인증이 필요합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공적으로 정보 수정됨",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberDto.UpdateMemberResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"userId\": 1,\n" +
                                            "  \"name\": \"홍길동\",\n" +
                                            "  \"grade\": 3,\n" +
                                            "  \"phoneNumber\": \"010-9876-5432\",\n" +
                                            "  \"birth\": \"2003-03-03\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (e.g., name이 너무 길거나, phoneNumber 형식이 틀림)"),
            @ApiResponse(responseCode = "401", description = "인증 실패 (토큰이 없거나 유효하지 않음)"),
            @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/me")
    public ResponseEntity<MemberDto.UpdateMemberResponse> updateMyInfo(
            @Parameter(description = "JWT 토큰", required = true, hidden = true)
            @RequestHeader("Authorization") String authorization,
            @RequestBody MemberDto.UpdateMemberRequest request
    ) {
        MemberDto.UpdateMemberResponse response = memberService.updateMyInfo(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "비밀번호 변경",
            description = "현재 로그인한 사용자의 비밀번호를 변경합니다. 인증이 필요합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "비밀번호가 정상적으로 변경됨",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberDto.ChangePasswordResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"message\": \"비밀번호가 성공적으로 변경되었습니다.\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (예: 현재 비밀번호 불일치, 유효성 검사 실패 등)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberDto.ChangePasswordResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"error\": \"현재 비밀번호가 일치하지 않습니다.\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "인증 실패 (토큰이 없거나 만료된 경우)")
    })
    @PutMapping("/password")
    public ResponseEntity<MemberDto.ChangePasswordResponse> changePassword(
            @Parameter(description = "JWT 토큰", required = true, hidden = true)
            @RequestHeader("Authorization") String authorization,
            @RequestBody MemberDto.ChangePasswordRequest request
    ) {
        MemberDto.ChangePasswordResponse response = memberService.changePassword(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "로그인",
            description = "이메일과 비밀번호로 로그인하여 JWT 토큰을 발급받습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberDto.LoginResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"accessToken\": \"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGFtcGxlMUBlbWFpbC5jb20iLCJpZCI6Miwicm9sZSI6IlVTRVIiLCJpYXQiOjE3NTg1NDU0NzIsImV4cCI6MjY0NzU3ODE3Nn0.cjWWo0YuYR9X_89TPZpMtpZ3feMdIlbD6-9WsEKGaxc\",\n" +
                                            "  \"refreshToken\": \"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGFtcGxlMUBlbWFpbC5jb20iLCJpZCI6Miwicm9sZSI6IlVTRVIiLCJpYXQiOjE3NTg1NDU0NzIsImV4cCI6MzY4NjgwNzEwNH0.XZXcg6U2P_O4PMyfy-ABILHVzz86IddU5U3X9ikmWDo\",\n" +
                                            "  \"tokenType\": \"Bearer\",\n" +
                                            "  \"expiresIn\": 86400000\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 형식",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberDto.ErrorMessageResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"message\": \"Invalid request format.\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "이메일이 이미 존재함",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberDto.ErrorMessageResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"message\": \"Email already exists.\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberDto.ErrorMessageResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"message\": \"Internal server error.\"\n" +
                                            "}"
                            )
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<MemberDto.LoginResponse> login(
            @RequestBody MemberDto.LoginRequest request
    ) {
        MemberDto.LoginResponse response = memberService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "회원가입",
            description = "새로운 사용자를 등록합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberDto.SignUpResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"userId\": 1\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping("/signup")
    public ResponseEntity<MemberDto.SignUpResponse> signUp(
            @RequestBody MemberDto.SignUpRequest request
    ) {
        MemberDto.SignUpResponse response = memberService.signUp(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "로그아웃",
            description = "Refresh Token을 만료 처리하여 로그아웃합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberDto.LogoutResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"message\": \"Logout successful\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberDto.ErrorMessageResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"message\": \"Invalid request format.\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않거나 만료된 토큰",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberDto.ErrorMessageResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"message\": \"Invalid or expired token.\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MemberDto.ErrorMessageResponse.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"message\": \"Internal server error.\"\n" +
                                            "}"
                            )
                    )
            )
    })
    @PostMapping("/logout")
    public ResponseEntity<MemberDto.LogoutResponse> logout(
            @RequestBody MemberDto.LogoutRequest request
    ) {
        MemberDto.LogoutResponse response = memberService.logout(request);
        return ResponseEntity.ok(response);
    }
}
