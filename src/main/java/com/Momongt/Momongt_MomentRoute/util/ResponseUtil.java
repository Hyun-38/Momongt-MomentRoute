package com.Momongt.Momongt_MomentRoute.util;

import com.Momongt.Momongt_MomentRoute.dto.MemberDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 응답 생성 유틸리티 클래스
 * 
 * 공통 에러 응답 생성을 위한 헬퍼 메서드를 제공합니다.
 */
public class ResponseUtil {

    /**
     * 에러 메시지 응답 생성
     * 
     * @param message 에러 메시지
     * @param status HTTP 상태 코드
     * @return ResponseEntity
     */
    public static ResponseEntity<MemberDto.ErrorMessageResponse> errorResponse(String message, HttpStatus status) {
        MemberDto.ErrorMessageResponse errorResponse = new MemberDto.ErrorMessageResponse();
        errorResponse.setMessage(message);
        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * 400 Bad Request 응답 생성
     * 
     * @param message 에러 메시지
     * @return ResponseEntity
     */
    public static ResponseEntity<MemberDto.ErrorMessageResponse> badRequest(String message) {
        return errorResponse(message, HttpStatus.BAD_REQUEST);
    }

    /**
     * 401 Unauthorized 응답 생성
     * 
     * @param message 에러 메시지
     * @return ResponseEntity
     */
    public static ResponseEntity<MemberDto.ErrorMessageResponse> unauthorized(String message) {
        return errorResponse(message, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 409 Conflict 응답 생성
     * 
     * @param message 에러 메시지
     * @return ResponseEntity
     */
    public static ResponseEntity<MemberDto.ErrorMessageResponse> conflict(String message) {
        return errorResponse(message, HttpStatus.CONFLICT);
    }

    /**
     * 500 Internal Server Error 응답 생성
     * 
     * @param message 에러 메시지
     * @return ResponseEntity
     */
    public static ResponseEntity<MemberDto.ErrorMessageResponse> internalServerError(String message) {
        return errorResponse(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

